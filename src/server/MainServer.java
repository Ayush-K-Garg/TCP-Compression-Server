package server;

import executor.*;

public class MainServer {

    public static void main(String[] args) {

        int port = 8080;

        if (args.length == 0) {
            System.out.println("Usage: java MainServer [single|perthread|pool]");
            return;
        }

        String mode = args[0].toLowerCase();
        TaskExecutor executor;

        switch (mode) {
            case "single":
                executor = new SingleThreadExecutor();
                break;

            case "perthread":
                executor = new PerThreadExecutor();
                break;

            case "pool":
                executor = new ThreadPoolExecutor(); // we’ll implement next
                break;

            default:
                System.out.println("Invalid mode. Use single | perthread | pool");
                return;
        }

        System.out.println("===================================");
        System.out.println("   TCP Compression Server Started  ");
        System.out.println("   Listening on port: " + port);
        System.out.println("   Execution Mode: " + mode);
        System.out.println("===================================");

        TcpServer tcpServer = new TcpServer(port, executor, mode);        tcpServer.start();
    }
}