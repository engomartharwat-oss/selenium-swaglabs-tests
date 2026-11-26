package com.omartharwat.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a CSV file from the classpath for data-driven tests.
 * The first line is the header and is skipped. Lines starting with # are ignored.
 * Empty cells are returned as empty strings.
 */
public final class CsvReader {

    private CsvReader() {
    }

    public static Object[][] read(String resourcePath) {
        InputStream in = CsvReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("CSV file not found on the classpath: " + resourcePath);
        }
        List<Object[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resourcePath, e);
        }
        return rows.toArray(new Object[0][]);
    }
}
