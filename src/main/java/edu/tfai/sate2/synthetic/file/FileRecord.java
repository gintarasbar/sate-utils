package edu.tfai.sate2.synthetic.file;

import java.io.File;

/**
 * File record used to sort spectra according to wavelengths
 *
 * @author gintaras
 */
public class FileRecord {
    private File fileName;

    private double wavelength;

    public File getFileName() {
        return fileName;
    }

    public void setFileName(File fileName) {
        this.fileName = fileName;
    }

    public double getWavelength() {
        return wavelength;
    }

    public void setWavelength(double wavelength) {
        this.wavelength = wavelength;
    }
}
