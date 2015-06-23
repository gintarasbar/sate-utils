package edu.tfai.sate2.utils;

import com.google.common.base.Strings;
import edu.tfai.sate2.exceptions.NoPointsInSpectra;
import edu.tfai.sate2.synthetic.file.MergeFiles;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static java.lang.Double.parseDouble;

@Slf4j
public class SpectraFileUtils {

    public static double getLastXValue(Path file) throws Exception {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile(file.toFile(), "r");
            long fileLength = file.toFile().length();
            String lastLine = "";
            do {
                fileLength--;
                StringBuilder sb = new StringBuilder();

                for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                    fileHandler.seek(filePointer);
                    int readByte = fileHandler.readByte();

                    if (readByte == 0xA) {
                        if (filePointer == fileLength)
                            continue;
                        else
                            break;
                    } else if (readByte == 0xD) {
                        if (filePointer == fileLength - 1)
                            continue;
                        else
                            break;
                    }
                    sb.append((char) readByte);
                }
                lastLine = sb.reverse().toString().trim();
            } while (fileLength > 1 && lastLine.split("\\s+").length != 2);
            if (Strings.isNullOrEmpty(lastLine)) {
                throw new NoPointsInSpectra(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
            }
            return parseDouble(lastLine.split("\\s+")[0]);
        } catch (Exception e) {
            log.error("Error getting last x value {}", e);
            throw e;
        } finally {
            if (fileHandler != null)
                fileHandler.close();
        }
    }

    public static double getFirstXValue(Path file) throws Exception {
        BufferedReader fileHandler = null;
        try {
            fileHandler = new BufferedReader(new FileReader(file.toFile()));
            String line = "";
            while ((line = fileHandler.readLine()) != null) {
                line = line.trim();
                String[] tokens = line.split("\\s+");
                if (tokens.length != 2 || line.contains("=") || line.contains("'") || line.contains("&"))
                    continue;
                else if (tokens.length == 2)
                    break;
            }
            if (Strings.isNullOrEmpty(line)) {
                throw new NoPointsInSpectra(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
            }
            return parseDouble(line.split("\\s+")[0]);
        } catch (Exception e) {
            log.error("Error getting first x value {}", e);
            throw e;
        } finally {
            if (fileHandler != null)
                fileHandler.close();
        }
    }

    public static double[] getSpectraRange(Path file) throws Exception {
        double minmax[] = new double[2];
        minmax[0] = SpectraFileUtils.getFirstXValue(file);
        minmax[1] = SpectraFileUtils.getLastXValue(file);
        return minmax;
    }

    public static int countLines(Path file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
        int lines = 0;
        while (reader.readLine() != null) {
            lines++;
        }
        reader.close();
        return lines;
    }

    public static int countLines(Path file, double startWave, double endWave) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
        int lines = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length != 2 || line.contains("=") || line.contains("'") || line.contains("&"))
                    continue;
                double value = Double.parseDouble(tokens[0]);
                if (value >= startWave && value <= endWave) {
                    lines++;
                }
            } catch (NumberFormatException e) {
            }
        }
        reader.close();
        return lines;
    }

    public static boolean stringIsNan(String string) {
        if (string.equalsIgnoreCase("nan"))
            return true;
        double wave = Double.parseDouble(string);
        if (Double.isNaN(wave)) {
            return true;
        }
        return false;
    }

    public static String spectraName(String path) {
        String name = new File(path).getName();
        name = name.replace("r_", "");
        name = name.replace("merged_", "");
        // name = name.substring(0, name.length() - 4);
        return name;
    }

    public static String mergeSpectraFiles(String path) {
        //FIXME rewrite using standford library
        MergeFiles merge = new MergeFiles(path);
        String outFile = path + "/" + new File(path).getName();
        merge.mergeFiles(outFile);
        return outFile;
    }


}
