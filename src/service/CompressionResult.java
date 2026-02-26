package service;

public class CompressionResult {

    private final boolean success;
    private final String message;
    private long originalSize;
    private long compressedSize;
    private double ratio;
    private long timeMs;

    public CompressionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CompressionResult(boolean success,
                             String message,
                             long originalSize,
                             long compressedSize,
                             double ratio,
                             long timeMs) {

        this.success = success;
        this.message = message;
        this.originalSize = originalSize;
        this.compressedSize = compressedSize;
        this.ratio = ratio;
        this.timeMs = timeMs;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public long getOriginalSize() { return originalSize; }
    public long getCompressedSize() { return compressedSize; }
    public double getRatio() { return ratio; }
    public long getTimeMs() { return timeMs; }
}