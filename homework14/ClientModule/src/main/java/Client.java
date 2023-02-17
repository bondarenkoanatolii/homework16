import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {

    private static Socket clientSocket;
    private BufferedReader reader;

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
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

//
//            ExecutorService exec = Executors.newFixedThreadPool(2);
//            exec.execute(new ReadMsg());
//            exec.execute(new WriteMsg());
          //  exec.shutdown();
            ReadMsg readMsg = new ReadMsg();
            readMsg.start();

            WriteMsg writeMsg = new WriteMsg();
            writeMsg.start();

//            writeMsg.join();
//            System.out.println("WriteMsg.join");
//            readMsg.join();
   //     }
//        finally {
//            System.out.println("Клиент был закрыт...");
//            clientSocket.close();
//            in.close();
//            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
           // System.out.println("Entry ReadMsg");
            try {
                while (!exit) {
                    str = in.readLine();

                    if (str.equals("-exit")) {
                        System.out.println("Press Enter to exit.");
                        exit = true;
                        while (!reader.ready()) {
                            Thread.sleep(200);
                        }
                        in.close();
                      //  reader.close();
                        break;
                    } else {
                        System.out.println(str);
                    }
                }
            } catch (SocketException e ) {

            }
            catch (IOException  e) {
                e.printStackTrace();
            } /*catch (InterruptedException e) {
                e.printStackTrace();
            }*/ catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Finish read");
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    private class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                   // System.out.println("Input line:");
                    System.out.println("before while()");
                    while (!reader.ready()) {
                      //  Thread.sleep(200);
                       // System.out.print(exit);
                        if (exit) return;
                    }
                    System.out.println("reader.nextLine()");
                    userWord = reader.readLine();
                    System.out.println("after reader.nextLine()");
                    if (userWord.equals("-exit")) {
                        out.write("-exit" + "\n");
                        out.flush();
                        out.close();
                        out = null;
                        break;
                    } else if (userWord.trim().startsWith("-file")) {
                        fileTransfer(userWord);
                    } else {
                        out.write(userWord + "\n"); // отправляем на сервер
                    }
                    out.flush();
                } catch (IllegalStateException e) {

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Finish write");
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
