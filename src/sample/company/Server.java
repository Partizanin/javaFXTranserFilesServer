package sample.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public static void main(String[] args) throws IOException {
        System.out.println(df.format(new Date()) + " Запуск сервера ");

        int bytesRead;
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(4444);
        System.out.println(df.format(new Date()) + " Створення підключення");

        while (true) {
            Socket clientSocket = null;

            System.out.println(df.format(new Date()) + " Очікування клієнта\n");

            clientSocket = serverSocket.accept();

            InputStream in = clientSocket.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            System.out.println(df.format(new Date()) + " Підключення клієнта " + clientSocket.getLocalSocketAddress());


            String fileName = clientData.readUTF();
            String filePath = "D:\\test\\result\\" + fileName;

            System.out.println(df.format(new Date()) + " Прийнято файл " + fileName);
            System.out.println(df.format(new Date()) + " Записано файл " + filePath + "\n");


            OutputStream output = new FileOutputStream(filePath);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            // Closing the FileOutputStream handle
            in.close();
            clientData.close();
            output.close();
        }
    }
}

