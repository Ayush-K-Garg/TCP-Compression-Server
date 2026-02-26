package server;

import java.io.*;
import java.net.Socket;
import service.CompressionService;
import service.CompressionResult;

public class ClientHandler {
    private final Socket socket;
    private final String mode;

    public ClientHandler(Socket socket, String mode) {
        this.socket = socket;
        this.mode = mode;
    }

    public void handle() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String request = reader.readLine();
            if (request == null || request.trim().isEmpty()) {
                writer.println("ERROR|Empty request");
                writer.flush();
            } else {
                request = request.trim();
                System.out.println("Received: " + request);

                String response;
                if (request.startsWith("COMPRESS|")) {
                    String filePath = request.substring("COMPRESS|".length()).trim();
                    CompressionResult result = CompressionService.compress(filePath, mode);
                    if (!result.isSuccess()) {
                        response = "ERROR|" + result.getMessage();
                    } else {
                        response = "OK|operation=COMPRESS|mode=" + mode
                                + "|thread=" + Thread.currentThread().getName()
                                + "|original=" + result.getOriginalSize()
                                + "|compressed=" + result.getCompressedSize()
                                + "|ratio=" + result.getRatio()
                                + "|timeMs=" + result.getTimeMs();
                    }
                } else if (request.startsWith("DECOMPRESS|")) {
                    String filePath = request.substring("DECOMPRESS|".length()).trim();
                    CompressionResult result = CompressionService.decompress(filePath);
                    if (!result.isSuccess()) {
                        response = "ERROR|" + result.getMessage();
                    } else {
                        response = "OK|operation=DECOMPRESS|thread=" + Thread.currentThread().getName()
                                + "|timeMs=" + result.getTimeMs();
                    }
                } else {
                    response = "ERROR|Invalid command";
                }

                writer.print(response + "\n");
                writer.flush();
            }

            socket.shutdownOutput();

            Thread.sleep(200);

        } catch (Exception e) {
            System.err.println("Client handling error: " + e.getMessage());
        } finally {
            try {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}