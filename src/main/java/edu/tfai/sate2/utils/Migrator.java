package edu.tfai.sate2.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

import static edu.tfai.sate2.utils.StringUtils.readToString;

@Slf4j
public class Migrator {

    public static String migrateFile(Path file) throws Exception {
        String string = readToString(file);
        if (!string.contains("instrumentalProfileFile class=\"sun.nio.fs.UnixPath\"")) {
            string = string.replaceAll("<instrumentalProfileFile>.*</instrumentalProfileFile>", "");
        }

        if (!string.contains("edu.tfai.sate.eqwidth.LineData")) {
            string = string.replaceAll("edu\\.tfai\\.sate\\.eqwidth\\.LineData", "edu.tfai.sate.model.LineData");
        }

        if (!string.contains("edu\\.tfai\\.sate\\.objects\\.LineData")) {
            string = string.replaceAll("edu.tfai.sate.objects.LineData", "edu.tfai.sate.model.LineData");
        }

        if (!string.contains("edu.tfai.sate.eqwidth.Element")) {
            string = string.replaceAll("edu\\.tfai\\.sate\\.eqwidth\\.Element", "edu.tfai.sate.model.Element");
        }

        if (!string.contains("edu\\.tfai\\.sate\\.objects\\.Element")) {
            string = string.replaceAll("edu.tfai.sate.objects.Element", "edu.tfai.sate.model.Element");
        }

        return string;
    }
}
