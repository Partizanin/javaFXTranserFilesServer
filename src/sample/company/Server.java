package sample.company;

import sample.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private Controller controller;
    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public Server() {
    }

    public Server(Controller controller) {
        this.controller = controller;
    }


    public void start() {
        String logMessage = "";
        logMessage = df.format(new Date()) + " Запуск сервера ";
        controller.sendLog(logMessage);
        System.out.println(logMessage);

        int bytesRead;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logMessage = df.format(new Date()) + " Створення підключення";
        controller.sendLog(logMessage);
        System.out.println(logMessage);

        while (true) {
            Socket clientSocket = null;
            logMessage = df.format(new Date()) + " Очікування клієнта\n";
            controller.sendLog(logMessage);
            System.out.println(logMessage);

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream in = null;
            try {
                in = clientSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataInputStream clientData = new DataInputStream(in);
            logMessage = df.format(new Date()) + " Підключення клієнта " + clientSocket.getLocalSocketAddress();
            controller.sendLog(logMessage);
            System.out.println(logMessage);


            String fileName = null;
            try {
                fileName = clientData.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String filePath = "D:\\test\\result\\" + fileName;

            OutputStream output = null;
            try {
                output = new FileOutputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            logMessage = df.format(new Date()) + " Прийнято файл " + fileName;
            controller.sendLog(logMessage);
            System.out.println(logMessage);

            long size = 0;
            try {
                size = clientData.readLong();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            try {
                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            logMessage = df.format(new Date()) + " Записано файл " + filePath + "\n";
            controller.sendLog(logMessage);
            System.out.println(logMessage);

            // Closing the FileOutputStream handle
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.sendMessage("Файл успешно принят","green");
        }

    }
}

