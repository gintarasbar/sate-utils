package edu.tfai.sate2.spectra;

import com.google.common.base.Optional;
import com.google.inject.Singleton;
import edu.tfai.sate2.cache.DataCache;
import edu.tfai.sate2.exceptions.SpectraException;
import edu.tfai.sate2.exceptions.SpectraOutOfRange;
import edu.tfai.sate2.synthetic.file.SpectraFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Optional.of;
import static edu.tfai.sate2.model.batch.BatchParameters.USE_CACHE;
import static edu.tfai.sate2.utils.NumberUtil.stringIsNan;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;

@Singleton
@Slf4j
public class SpectraReader {
    private static DataCache<Spectra> dataCache = new DataCache<Spectra>(1, TimeUnit.DAYS);


    public Spectra loadSyntheticSpectra(Path file, String lineId) throws FileNotFoundException {
        return loadSyntheticSpectra(file, lineId, 0, Double.MAX_VALUE);
    }

    public Spectra loadSyntheticSpectra(Path file, String lineId, double minWave, double maxWave) throws FileNotFoundException {
        if (!file.toString().endsWith(".xxy")) {
            throw new IllegalArgumentException("This is not synthetic spectra file " + file.toString());
        }

        Spectra spectra;
        if ((file.toString().endsWith(".xxy") || file.toString().endsWith(".xy"))) {
            try {
                spectra = loadSpectraFileRange(file, minWave, maxWave);
            } catch (FileNotFoundException e) {
                throw e;
            } catch (Exception e) {
                log.error("Error loading synthetic spectra: {}", e.getMessage());
                throw new IllegalStateException("Error while loading synthetic spectra " + file, e);
            }
        } else {
            throw new IllegalArgumentException(format("Synthetic spectra must end with .xy or .xxy, but was %s", file));
        }

        if (spectra == null || spectra.size() == 0) {
            //FIXME unify spectra validation as in observed spectra loading
            log.error(format("Error loading synthetic spectra %s for line %s", file, lineId));
            throw new SpectraOutOfRange(0, minWave, maxWave, lineId);
        }
        return spectra;
    }

    public Spectra loadSpectra(Path file, String lineId, double startWave, double endWave) throws Exception {
        if (file.toString().endsWith(".xxy")) {
            throw new IllegalArgumentException("Calling synthetic spectra with cache " + file.toString());
        }
        try {
            Spectra spectra;
            if (USE_CACHE) {
                spectra = loadSpectraFromCache(file, startWave, endWave);
            } else {
                spectra = loadSpectraAndSetFileName(file, startWave, endWave);
            }
            validateSpectra(file, lineId, startWave, endWave, spectra);
            return spectra;
        } catch (Exception e) {
            if (e instanceof SpectraException) {
                throw e;
            }
            log.debug("Error loading spectra ", e);
            throw new SpectraException(startWave, endWave, lineId);
        }
    }

    private Spectra loadSpectraFromCache(Path file, double startWave, double endWave) throws Exception {
        Optional<Spectra> spectra = dataCache.retrieve(spectraKey(file, startWave, endWave));
        if (!spectra.isPresent()) {
            spectra = of(loadSpectraAndSetFileName(file, startWave, endWave));
            dataCache.store(spectraKey(file, startWave, endWave), spectra.get().copy());
        } else {
            spectra = of(spectra.get().copy());
            spectra.get().setCached(true);
        }
        return spectra.get();
    }

    private Spectra loadSpectraAndSetFileName(Path file, double startWave, double endWave) throws Exception {
        Spectra spectra;
        spectra = loadSpectraFileRange(file, startWave, endWave);
        spectra.setSpectraName(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
        return spectra;
    }

    private void validateSpectra(Path file, String lineId, Double startWave, Double endWave, Spectra spectra) {
        if (spectra == null || spectra.size() == 0) {
            log.debug(format("Error loading spectra for range[%.2f,%.2f] from %s", startWave, endWave, file));
            throw new SpectraOutOfRange(startWave, startWave, endWave, lineId);
        }
    }

    private String spectraKey(Path file, double startWave, double endWave) {
        return file.toString() + "_" + (int) Math.floor(startWave) + "_" + (int) Math.ceil(endWave);
    }

    private Spectra loadSpectraFile(Path file) throws Exception {
        double[] spectraRange = SpectraFileUtils.getSpectraRange(file);
        return loadSpectraFileRange(file, spectraRange[0], spectraRange[1]);
    }

    protected Spectra loadSpectraFileRange(Path file, double startWave, double endWave) throws Exception {
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
            log.debug("Error loading range: ", e);
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
        dataCache = new DataCache<Spectra>(1, TimeUnit.DAYS);
    }

}
