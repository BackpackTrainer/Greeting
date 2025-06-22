package com.example.demo.bdd.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppendDateToFileName {
    public static String generateTimestampedFilename(String baseName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        return baseName + "-" + timestamp + ".png";
    }

}
