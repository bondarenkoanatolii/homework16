package com.hillel.anatoliibondarenko;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

import static com.hillel.anatoliibondarenko.Server.*;

class CreateSocketForClient extends Thread {
    private final ServerSocket server;
    private static int idClient;

    public CreateSocketForClient(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = server.accept();
                WorkWithClient workWithClient = new WorkWithClient(socket,
                        FILE_MASK_CLIENT + ++idClient, LocalDateTime.now());
                workWithClient.start();
                listClients.add(workWithClient);
            }
        } catch (SocketException e) {
            System.out.printf("ServerSocket at port %d is closed.", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}