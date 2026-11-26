package com.omartharwat.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads settings from config.properties on the classpath.
 * Any value can be overridden from the command line, for example:
 * mvn test -Dheadless=false -Dbrowser=chrome
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String fromCommandLine = System.getProperty(key);
        if (fromCommandLine != null && !fromCommandLine.isBlank() && !fromCommandLine.startsWith("${")) {
            return fromCommandLine;
        }
        return PROPS.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key).trim());
    }
}
