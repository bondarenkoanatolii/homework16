package com.hillel.anatoliibondarenko;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class SocketForClient extends Thread {
    private final Socket socket;
    private final String nameClient;
    private final LocalDateTime dateTimeEntry;

    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет

    public SocketForClient(Socket socket, String nameClient, LocalDateTime dateTimeEntry) throws IOException {
        this.socket = socket;
        this.nameClient = nameClient;
        this.dateTimeEntry = dateTimeEntry;
    }

    public String getNameClient() {
        return nameClient;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            sendMessageAboutEntry();

            while (true) {
                String word = in.readLine();
                if (word.equals("-exit")) {
                    // Переправляємо "-exit" на вхід клієнта, щоб
                    sendMessageAboutExitClient();
                    this.send("-exit");
                    this.getSocket().close();
                    break;
                } else if (word.trim().startsWith("-file"))
                    receiveFile();
                else sendMessageAllClients(word);
            }
        } catch (NullPointerException e) {

        } catch (SocketException e ) {

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageAboutEntry() {
        String message = "[SERVER] " + "[" + currentTime() + "] " + getNameClient() + " connected successfully.";
        for (SocketForClient socketForClient : Server.listClients) {
            if (!socketForClient.equals(this))
                socketForClient.send(message);
        }
    }

    private void sendMessageAboutExitClient() {
        String message = "[SERVER] " + "[" + currentTime() + "] " + getNameClient() + " has left us.";
        Server.listClients.remove(this);
        for (SocketForClient socketForClient : Server.listClients) {
            socketForClient.send(message);
        }
    }

    private void sendMessageAllClients(String message) {
        message = "[" + getNameClient() + "] " + "[" + currentTime() + "]: " + message;
        for (SocketForClient socketForClient : Server.listClients) {
            if (!socketForClient.equals(this))
                socketForClient.send(message);
        }
    }

    private void receiveFile() throws IOException {
        try {
            InputStream inReceiveFile = socket.getInputStream();
            DataInputStream clientData = new DataInputStream(inReceiveFile);

            String fileName = clientData.readUTF();
            String fileFullName = Server.PATH_TO_RECEIVED_FILE + fileName;

            OutputStream output = new FileOutputStream(fileFullName);
            long size = clientData.readLong();

            // Великі файли неможливо передати одним масивом.
            // byte[] buffer = new byte[1024];
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            send("[SERVER] " + "[" + currentTime() + "]: File " + fileName + " received by server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void send(String msg) {
        try {
            out.write(msg + "\n");
//            out.write(msg );
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
