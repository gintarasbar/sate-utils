package edu.tfai.sate2.synthetic.file;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Merges spectrum files
 *
 */
@Slf4j
public class MergeFiles {
    /**
     * List of sorted files
     */
    private ArrayList<FileRecord> records = new ArrayList<FileRecord>();

    /**
     * Merges all in the directory
     *
     * @param path
     */
    public MergeFiles(String path) {
        try {
            File directory = new File(path);
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (!file.getAbsolutePath().toLowerCase().endsWith(".dat")
                            && !file.getAbsolutePath().toLowerCase().endsWith(".txt"))
                        continue;
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    StringTokenizer st;
                    String str = "";
                    String wave = "";
                    while ((str = br.readLine()) != null && wave.equals("")) {
                        st = new StringTokenizer(str, " \t\n\r");
                        if (st.countTokens() > 1)
                            wave = st.nextToken();
                    }
                    br.close();
                    if (wave == null || wave.equals(""))
                        return;
                    FileRecord fileRecord = new FileRecord();
                    fileRecord.setFileName(file);
                    fileRecord.setWavelength(Double.parseDouble(wave));
                    records.add(fileRecord);
                }
            }
        } catch (Exception e) {
            log.error("Error merging files {}", e);
        }
    }

    public static void mergeFiles(String outputName, boolean append, String... files) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputName), append));
            for (String file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = "";
                while ((str = br.readLine()) != null) {
                    bw.append(str + "\n");
                }
                br.close();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("Error merging files {}", e);
        }
    }

    /**
     * Merge files
     *
     * @param outputName output file name
     */
    public void mergeFiles(String outputName) {
        try {
            Collections.sort(records, new Comparator<FileRecord>() {
                public int compare(FileRecord a1, FileRecord a2) {
                    if (a1.getWavelength() < a2.getWavelength())
                        return -1;
                    if (a1.getWavelength() > a2.getWavelength())
                        return 1;
                    return 0;
                }
            });

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputName)));
            double lastWave = 0;
            for (FileRecord file : records) {
                BufferedReader br = new BufferedReader(new FileReader(file.getFileName()));
                String str = "";
                while ((str = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(str, " \t");
                    if (st.countTokens() > 1) {
                        double wave = Double.parseDouble(st.nextToken());
                        if (wave >= lastWave) {
                            bw.append(str + "\n");
                            lastWave = wave;
                        }
                    }
                }
                br.close();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("Error merging files {}",e);
        }
    }

}
