package com.omartharwat.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Saves a PNG screenshot into target/screenshots and returns its path. */
public final class ScreenshotUtils {

    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    public static Path capture(WebDriver driver, String testName) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = dir.resolve(safeName + "_" + LocalDateTime.now().format(STAMP) + ".png");
            Files.write(file, bytes);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Could not save the screenshot", e);
        }
    }
}
