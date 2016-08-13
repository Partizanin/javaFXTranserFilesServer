package sample.company;

import javafx.application.Platform;
import sample.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class Server {
    private Controller controller;
    private Thread serverThread;
    private ServerSocket serverSocket = null;

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
        String errorMessage = "";

        logMessage = df.format(new Date()) + " Запуск сервера ";
        sendLog(logMessage);
        System.out.println(logMessage);

        serverSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(getProps()[0]));
        } catch (IOException e) {
            errorMessage = e.getMessage();
            sendMessage(errorMessage, "red");
            e.printStackTrace();
        }
        logMessage = df.format(new Date()) + " Створення підключення";
        sendLog(logMessage);
        System.out.println(logMessage);

        serverThread = new Thread() {
            @Override
            public void run() {
                String errorMessage = "";
                String logMessage = "";
                while (true) {
                    Socket clientSocket = null;
                    logMessage = df.format(new Date()) + " Очікування клієнта\n";
                    sendLog(logMessage);
                    System.out.println(logMessage);

                    try {
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }

                    InputStream in = null;
                    try {
                        in = clientSocket.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
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
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }
                    String filePath = getProps()[1];
                    filePath += fileName;

                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(filePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }

                    logMessage = df.format(new Date()) + " Прийнято файл " + fileName;
                    sendLog(logMessage);
                    System.out.println(logMessage);

                    long size = 0;
                    try {
                        size = clientData.readLong();
                    } catch (IOException e) {
                        errorMessage = e.toString()  + " \n" +  Arrays.toString(e.getStackTrace());
                        sendMessage(errorMessage,"red");
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
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }
                    logMessage = df.format(new Date()) + " Записано файл " + filePath + "\n";
                    sendLog(logMessage);
                    System.out.println(logMessage);

                    // Closing the FileOutputStream handle
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }
                    try {
                        clientData.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();
                        sendMessage(errorMessage, "red");
                    }
                    if (errorMessage.length() < 2) {
                        sendMessage("Файл " + fileName + " успішно принятий", "green");
                    }
                }
            }
        };
        serverThread.start();
    }

    private String[] getProps() {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("settings.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serverPort = prop.getProperty("server.port");
        String filesFolderPath = prop.getProperty("files.path");

        return new String[]{serverPort, filesFolderPath};
    }

    public void closeSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSocket() {
        serverThread.interrupt();
        return "serverSocket.isClosed() = " + String.valueOf(serverSocket.isClosed()) + " \nserverThread.isAlive() " + serverThread.isAlive() + "\n";
    }
}


