package edu.tfai.sate2.utils;


import com.google.common.io.Resources;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTestUtil {

    public static Path getPath(String file) {
        try {
            return Paths.get(Resources.getResource(file).toURI());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static File urlToFile(URL file) throws Exception {
        return new File(file.toURI());
    }

    public static String urlToPath(URL file) throws Exception {
        return urlToFile(file).toString();
    }
}
