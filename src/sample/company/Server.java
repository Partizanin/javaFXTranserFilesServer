package sample.company;

import javafx.application.Platform;
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

    public void sendLog(final String log) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.sendLog(log);
            }
        });

    }

    public void sendMessage(final String message, final String color) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.sendMessage(message, color);
            }
        });
    }

    public void start() {
        String logMessage = "";
        logMessage = df.format(new Date()) + " Запуск сервера ";
        sendLog(logMessage);
        System.out.println(logMessage);


        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logMessage = df.format(new Date()) + " Створення підключення";
        sendLog(logMessage);
        System.out.println(logMessage);

        final ServerSocket finalServerSocket = serverSocket;
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                String logMessage = "";
                while (true) {
                    Socket clientSocket = null;
                    logMessage = df.format(new Date()) + " Очікування клієнта\n";
                    sendLog(logMessage);
                    System.out.println(logMessage);

                    try {
                        clientSocket = finalServerSocket.accept();
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
                    sendLog(logMessage);
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
                    sendLog(logMessage);
                    System.out.println(logMessage);

                    long size = 0;
                    try {
                        size = clientData.readLong();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    try {
                        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                            output.write(buffer, 0, bytesRead);
                            size -= bytesRead;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logMessage = df.format(new Date()) + " Записано файл " + filePath + "\n";
                    sendLog(logMessage);
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
                    sendMessage("Файл " + fileName + " успешно принят", "green");
                }
            }
        };
        serverThread.start();
    }
}


