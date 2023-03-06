import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {

    private static Socket clientSocket;
    private BufferedReader reader;
    private Scanner scanner;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static volatile boolean exit = false;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startClient();
    }

    private void startClient() throws IOException {

        try {
            clientSocket = new Socket("localhost", 4004);
            reader = new BufferedReader(new InputStreamReader(System.in));
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            ReadMsg readMsg = new ReadMsg();
            readMsg.start();

            WriteMsg writeMsg = new WriteMsg();
            writeMsg.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (!exit) {
                    str = in.readLine();
                    if (str.equals("-exit")) {
                        System.out.println("Press Enter to exit...");
                        exit = true;
                        in.close();
                        break;
                    } else {
                        System.out.println(str);
                    }
                }
            } catch (SocketException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // нить отправляющая сообщения с консоли на сервер
    private class WriteMsg extends Thread {
        @Override
        public void run() {
            while (!exit) {
                String userWord;
                try {
                    userWord = scanner.nextLine();

                    if (userWord.equals("-exit")) {
                        out.write("-exit" + "\n");
                        out.flush();
                        out.close();
                        break;
                    } else if (userWord.trim().startsWith("-file")) {
                        fileTransfer(userWord);
                    } else {
                        out.write(userWord);
                        out.write("\n");
                    }
                    out.flush();
                } catch (IllegalStateException e) {

                } catch (SocketException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void fileTransfer(String userWord) throws IOException {
        try {
            out.write(userWord + "\n");
            out.flush();

            String fileName = userWord.trim().split(" ")[1];
            File fileToSend = new File(fileName);

            FileInputStream fis = new FileInputStream(fileToSend);
            OutputStream os = clientSocket.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(fileToSend.getName());
            dos.writeLong(fileToSend.length());
            int bytes;
            byte[] buffer = new byte[8 * 1024];
            while ((bytes = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytes);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
