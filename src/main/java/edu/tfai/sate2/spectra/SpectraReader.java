package edu.tfai.sate2.spectra;

import com.google.inject.Singleton;
import edu.tfai.sate2.cache.DataCache;
import edu.tfai.sate2.exceptions.SpectraOutOfRange;
import edu.tfai.sate2.synthetic.file.SpectraFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static edu.tfai.sate2.utils.NumberUtil.stringIsNan;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;

@Singleton
@Slf4j
public class SpectraReader {
    private static DataCache<Spectra> dataCache = new DataCache<Spectra>(3, TimeUnit.HOURS, null);


    public Spectra loadSyntheticSpectra(Path file, String lineId) {
        Spectra spectra = null;
        if ((file.toString().endsWith(".xxy") || file.toString().endsWith(".xy"))) {
            try {
                spectra = loadFile(file);
            } catch (Exception e) {
                log.error("Error loading synthetic spectra {}", e);
                throw new IllegalStateException("Error while loading synthetic spectra " + file, e);
            }
        } else {
            throw new IllegalArgumentException(format("Synthetic spectra must end with .xy or .xxy, but was %s", file));
        }

        if (spectra==null || spectra.size() == 0) {
            log.error(format("Error loading synthetic spectra %s", file));
            throw new SpectraOutOfRange(0, spectra.getMinX(), spectra.getMaxX(), lineId);
        }
        return spectra;
    }

    public Spectra loadSpectra(Path file, String lineId, double startWave, double endWave) {

        try {
            Spectra spectra = dataCache.retrieve(spectraKey(file, startWave, endWave));
            if (spectra == null) {
                spectra = readSpectraRange(file, startWave, endWave);
                spectra.setSpectraName(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
                dataCache.store(spectraKey(file, startWave, endWave), spectra);
            } else {
                spectra.setCached(true);
            }

            validateSpectra(file, lineId, startWave, endWave, spectra);
            return spectra;
        } catch (Exception e) {
            log.error("Error loading spectra {}", e);
            throw new IllegalStateException("Error while loading the spectra " + file, e);
        }
    }

    private void validateSpectra(Path file, String lineId, Double startWave, Double endWave, Spectra spectra) {
        if (spectra==null || spectra.size() == 0) {
            log.error(format("Error loading spectra of range[%.2f,%.2f] %s", startWave, endWave, file));
            throw new SpectraOutOfRange(startWave, spectra.getMinX(), spectra.getMaxX(), lineId);
        }
    }

    private String spectraKey(Path file, double startWave, double endWave) {
        return file.toString() + "_" + (int) Math.floor(startWave) + "_" + (int) Math.ceil(endWave);
    }

    private Spectra loadFile(Path file) throws Exception {
        double[] spectraRange = SpectraFileUtils.getSpectraRange(file);
        return readSpectraRange(file, spectraRange[0], spectraRange[1]);
    }

    protected Spectra readSpectraRange(Path file, double startWave, double endWave) throws Exception {
        int count = SpectraFileUtils.countLines(file, startWave, endWave);
        double x[] = new double[count];
        double y[] = new double[count];
        String instrument = "";

        try {
            int pos = 0;
            BufferedReader br = new BufferedReader(new FileReader(file.toFile()));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length != 2 || line.contains("=") || line.contains("'") || line.contains("&")) {
                    if (line.startsWith("INSTRUME=")) {
                        try {
                            instrument = line.split("=")[1].trim().replace("'", "").trim();
                        } catch (Exception e) {
                        }
                    }
                    continue;
                } else if (tokens.length == 2) {
                    String waveString = tokens[0];
                    if (stringIsNan(waveString)) {
                        continue;
                    }
                    double wave = parseDouble(waveString);

                    if (wave >= startWave && wave <= endWave) {
                        x[pos] = wave;
                        String yValueString = tokens[1];
                        if (stringIsNan(yValueString)) {
                            y[pos] = 0.0d;
                        } else {
                            double yValue = parseDouble(yValueString);
                            y[pos] = yValue;
                        }
                        pos++;
                    }
                } else {
                    log.warn("Missing token in spectra file. Token found: {}", tokens.length);
                }
            }
            br.close();
        } catch (Exception e) {
            log.error("Error: {}", e);
            throw new Exception("Error loading spectra: " + file + ". ");

        }
        Spectra spectra = new Spectra(x, y);
        spectra.setInstrument(instrument);
        return spectra;
    }

    public void writeSpectra(Path file, Spectra spectra) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file.toFile()));
            for (int i = 0; i < spectra.size(); i++) {
                bw.write(format("%.5f\t%.5f\n", spectra.getX(i), spectra.getY(i)));
            }

        } catch (Exception e) {
            log.error("Error outputing spectra to file: ", e);
        } finally {
            if (bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public void dispose() {
        dataCache.clearCache();
        dataCache = new DataCache<Spectra>(3, TimeUnit.HOURS, null);
    }

}
