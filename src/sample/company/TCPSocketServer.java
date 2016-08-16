package sample.company;

import javafx.application.Platform;
import sample.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPSocketServer {
    private ServerSocket serverSocket = null;
    private ServerSocket readServerSocket = null;
    private Socket socket = null;
    private Socket readSocket = null;
    private ObjectInputStream inStream = null;
    private InputStream readInStream = null;
    private OutputStream readOuStream;
    private TransferObject transferObject;
    private Utils utils = new Utils();
    private Controller controller;
    private String errorMessage = "";


    public TCPSocketServer() {

    }

    private void sendLog(final String logMessage) {
        System.out.println(logMessage);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.sendLog(logMessage);
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

    public TCPSocketServer(Controller controller) {
        this.controller = controller;
        communicate();
        communicate2();
    }

   /* public static void main(String[] args) {
        new TCPSocketServer().communicate();
    }*/

    private void communicate() {
        Thread communicateThread = new Thread() {

            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(4445);
                    sendLog(utils.getCurrentDateTime() + " Запуск сервера ");
                    while (true) {
                        sendLog(utils.getCurrentDateTime() + " Очікування підключення ");

                        socket = serverSocket.accept();

                        sendLog(utils.getCurrentDateTime() + " " + socket.getInetAddress() +
                                " Клієнт підключено \n");

                        inStream = new ObjectInputStream(socket.getInputStream());

                        transferObject = (TransferObject) inStream.readObject();

                        if (transferObject.getMessage().equals("stop")) {
                            break;
                        }
                    }
                    socket.close();
                    inStream.close();
                } catch (SocketException se) {
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException cn) {
                    cn.printStackTrace();
                }
            }
        };
        communicateThread.start();
    }

    private void communicate2() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                int fileCounter = -1;
                try {
                    readServerSocket = new ServerSocket(4446);
                    while (true) {
                        readSocket = readServerSocket.accept();
                        readInStream = readSocket.getInputStream();
                        fileCounter++;
                        DataInputStream dataInputStream = new DataInputStream(readInStream);

                        String fileName = null;
                        fileName = dataInputStream.readUTF();
                        String filePath = "D:\\ARMVZ_SL\\Obmen\\Export\\test\\";
                        filePath += fileName;
                        readOuStream = new FileOutputStream(filePath);


                        long size = 0;
                        size = dataInputStream.readLong();
                        long count = 0;
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        File currentFile;
                        do {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (transferObject == null);
                        System.out.println(" fileCounter " + fileCounter);
                        currentFile = transferObject.getFiles().get(fileCounter);
                        int logCounter = utils.getLogCounter(currentFile.length());

                        sendLog("Файл: " + fileName);
                        try {
                            while (size > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                                readOuStream.write(buffer, 0, bytesRead);
                                count += bytesRead;
                                size -= bytesRead;
                                if (count % (1024 * (1024 * logCounter)) == 0) {
                                    sendLog(utils.getCurrentDateTime() +
                                            " Передано: " + utils.readableFileSize(count));
                                }
                            }
                            File file = currentFile;
                            System.out.println(file.getName());
                            sendLog("Всього передано: "
                                    + utils.readableFileSize(count)
                                    + "\n" + "Або: " + file.length() + " байт\n");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        readOuStream.flush();

                        readInStream.close();
                        readOuStream.close();
                        readSocket.close();
                        if (fileCounter >= transferObject.getFiles().size() - 1) {
                            fileCounter = 0;
                        }

                    }

                } catch (SocketException se) {
                    se.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();

    }

    public void closeSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFilesFromStation() {
        /*todo: Отправить запрос на получение файлов из отделения*/
    }

    public String getSocket() {
        return serverSocket.toString() + " " + socket.toString();
    }
}