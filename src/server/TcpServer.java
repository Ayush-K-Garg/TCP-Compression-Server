package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import executor.TaskExecutor;

public class TcpServer {

    private final int port;
    private final TaskExecutor executor;
    private final String mode;

    private static final AtomicInteger totalRequests = new AtomicInteger(0);
    private static final AtomicInteger activeRequests = new AtomicInteger(0);

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public TcpServer(int port, TaskExecutor executor, String mode) {
        this.port = port;
        this.executor = executor;
        this.mode = mode;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is waiting for client connections...");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                int requestId = totalRequests.incrementAndGet();

                executor.execute(() -> {

                    activeRequests.incrementAndGet();

                    String startTime = LocalDateTime.now().format(formatter);

                    System.out.println("[" + startTime + "] "
                            + "Request #" + requestId
                            + " STARTED | Active: " + activeRequests.get()
                            + " | Mode: " + mode
                            + " | Thread: " + Thread.currentThread().getName());

                    try {
                        new ClientHandler(clientSocket, mode).handle();
                    } finally {

                        activeRequests.decrementAndGet();

                        String endTime = LocalDateTime.now().format(formatter);

                        System.out.println("[" + endTime + "] "
                                + "Request #" + requestId
                                + " COMPLETED | Active: " + activeRequests.get()
                                + " | Mode: " + mode
                                + " | Thread: " + Thread.currentThread().getName());
                    }
                });
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}