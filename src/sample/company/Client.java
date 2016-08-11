package sample.company;

import sample.Controller;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client {
    private Controller controller;
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private String serverIpAddress = "127.0.0.1";
    private int serverPort = 4444;

    public Client() {

    }

    public Client(Controller controller) {
        this.controller = controller;
    }

    public void sendFile() {
        File folder = new File("D:\\test");
        ArrayList<File> files = new ArrayList<File>();
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                files.add(file);
            }
        }
        senFiles(files);
    }

    private void senFiles(ArrayList<File> files) {
        String logMessage = "";
        Socket socket = null;
        for (File file : files) {
            try {
                socket = new Socket(serverIpAddress, serverPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                int n = 0;
                byte[] buf = new byte[4092];

                String fileName = file.getName();
                logMessage = df.format(new Date()) + " Знайдено файл: " + fileName;

                System.out.println(logMessage);
                controller.sendLog(logMessage);

                //write file names
                dos.writeUTF(fileName);

                //create new fileinputstream for each file
                FileInputStream fis = new FileInputStream(file);

                //write file to dos
                while ((n = fis.read(buf)) != -1) {
                    dos.write(buf, 0, n);

                }
                dos.flush();
                dos.close();
                logMessage = df.format(new Date()) + " Передано файл: " + fileName;
                controller.sendLog(logMessage);

                System.out.println(logMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        logMessage = df.format(new Date()) + " Файлы успешно переданы!!!";
        controller.sendMessage(logMessage,"green");



    }

}

