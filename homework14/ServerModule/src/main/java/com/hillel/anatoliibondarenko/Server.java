package com.hillel.anatoliibondarenko;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 4004;
    private static final String FILE_MASK_CLIENT = "Client-";
    public static final String PATH_TO_RECEIVED_FILE = "c:\\server\\download\\";

    private static int idClient;
    public final static List<SocketForClient> listClients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        // Перевіряємо існування папки для отримання файлів від клієнтів
        File pathToReceivedFile = new File(PATH_TO_RECEIVED_FILE);
        if (!pathToReceivedFile.exists()) {
            pathToReceivedFile.mkdirs();
        }

        try (ServerSocket server = new ServerSocket(PORT)) {

            new CreateSocket(server).start();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.nextLine().equals("-exit")) {
                    closeALLSocketForClient();
                    server.close();
                    break;
                }
            }
        }



    }

    private static void closeALLSocketForClient() throws IOException {
        String message = "[SERVER] " + "[" + currentTime() + "] "  + " Server has stopped working.";
        for (SocketForClient socketForClient : Server.listClients) {
            socketForClient.send(message);
            socketForClient.send("-exit");
            socketForClient.getSocket().close();
        }
    }

    private static String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private static class CreateSocket extends Thread {

        private ServerSocket server;

        public CreateSocket(ServerSocket server) {
            this.server = server;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = server.accept();
                    SocketForClient socketForClient = new SocketForClient(socket, FILE_MASK_CLIENT + ++idClient, LocalDateTime.now());
                    socketForClient.start();
                    listClients.add(socketForClient);
                }
            } catch (SocketException e) {
                return;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }





    }
}