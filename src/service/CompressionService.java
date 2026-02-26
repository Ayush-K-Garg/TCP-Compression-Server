package service;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

public class CompressionService {

    public static CompressionResult compress(String filePath, String mode) {

        File inputFile = new File(filePath);

        if (!inputFile.exists()) {
            return new CompressionResult(false, "File does not exist");
        }

        if (inputFile.isDirectory()) {
            return new CompressionResult(false, "Cannot compress a directory");
        }

        if (filePath.endsWith(".gz")) {
            return new CompressionResult(false, "File already appears compressed");
        }

        long originalSize = inputFile.length();
        long startTime = System.currentTimeMillis();

        String threadName = Thread.currentThread().getName();

        String outputPath = filePath + "_"
                + mode + "_"
                + threadName + "_"
                + System.nanoTime()
                + ".gz";

        File outputFile = new File(outputPath);

        try (
                FileInputStream fis = new FileInputStream(inputFile);
                FileOutputStream fos = new FileOutputStream(outputFile);
                GZIPOutputStream gzip = new GZIPOutputStream(fos)
        ) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                gzip.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            return new CompressionResult(false,
                    "Compression failed: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long compressedSize = outputFile.length();

        double ratio = (originalSize == 0)
                ? 0
                : (double) compressedSize / originalSize;

        return new CompressionResult(
                true,
                "Compression successful",
                originalSize,
                compressedSize,
                ratio,
                (endTime - startTime)
        );
    }


    public static CompressionResult decompress(String filePath) {

        File inputFile = new File(filePath);

        if (!inputFile.exists()) {
            return new CompressionResult(false, "File does not exist");
        }

        if (!filePath.endsWith(".gz")) {
            return new CompressionResult(false,
                    "File is not a .gz compressed file");
        }

        long startTime = System.currentTimeMillis();

        String threadName = Thread.currentThread().getName();

        String outputPath = filePath.replace(".gz", "")
                + "_decompressed_"
                + threadName + "_"
                + System.nanoTime();

        File outputFile = new File(outputPath);

        try (
                FileInputStream fis = new FileInputStream(inputFile);
                GZIPInputStream gzipIn = new GZIPInputStream(fis);
                FileOutputStream fos = new FileOutputStream(outputFile)
        ) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            return new CompressionResult(false,
                    "Decompression failed: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();

        return new CompressionResult(
                true,
                "Decompression successful",
                0,
                0,
                0,
                (endTime - startTime)
        );
    }
}