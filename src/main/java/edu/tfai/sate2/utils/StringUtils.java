package edu.tfai.sate2.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StringUtils {


    public static String readToString(Path file) throws Exception {
        byte[] encoded = java.nio.file.Files.readAllBytes(file);
        return new String(encoded, Charsets.UTF_8);
    }
}
