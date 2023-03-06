package com.hillel.anatoliibondarenko;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static final int PORT = 4004;
    public static final String FILE_MASK_CLIENT = "Client-";
    public static final String PATH_TO_RECEIVED_FILE = "c:\\server\\download\\";


    public final static List<WorkWithClient> listClients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        // Перевіряємо існування шляху для отримання файлів від клієнтів
        File pathToReceivedFile = new File(PATH_TO_RECEIVED_FILE);
        if (!pathToReceivedFile.exists()) {
            pathToReceivedFile.mkdirs();
        }

        try (ServerSocket server = new ServerSocket(PORT)) {
            new CreateSocketForClient(server).start();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.nextLine().equals("-exit")) {
                    closeALLSocketForClient();
                    break;
                }
            }
        }
    }

    private static void closeALLSocketForClient(){
        String message = "[SERVER] " + "[" + currentTime() + "] " + " Server has stopped working.";

        for (WorkWithClient workWithClient : Server.listClients) {
            workWithClient.send(message);
            workWithClient.send("-exit");
            System.out.printf("User %s was disconnected.\n", workWithClient.getNameClient());
        }
    }

    private static String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }


}