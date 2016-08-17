package sample.company;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 16:13.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
public class Utils {
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public static void main(String[] args) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("192.168.2.213", 21);
            System.out.println(ftpClient.getReplyString());
            System.out.println("login: " + ftpClient.login("anonymous", "123123"));/*todo:"login:anonymous; password: """ is connected to ftp*/
            System.out.println("changeWorkingDirectory: " +
                    ftpClient.changeWorkingDirectory("incoming\\ASRK\\in"));

            for (FTPFile ftpFile : ftpClient.listDirectories()) {
                System.out.println(ftpFile);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDateTime() {
        return df.format(new Date());
    }

    public String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public int getLogCounter(long length) {
        int divider = 1;
        double divedResult = length / (1024d * divider);

        while (divedResult >= 16000) {
            divedResult = length / (1024d * divider);
            divider++;
        }

        return divider;
    }

    private void sendFileToFTP() {
        /*ftp://10.209.11.213/incoming/ASRK/in*/
        String server = "\\10.209.11.213\\incoming\\ASRK\\in\\";
        int port = 21;
        String user = "guest";
        String pass = "123123";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("D:/Test/Projects.zip");

            String firstRemoteFile = "Projects.zip";
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

            // APPROACH #2: uploads second file using an OutputStream
            File secondLocalFile = new File("E:/Test/Report.doc");
            String secondRemoteFile = "test/Report.doc";
            inputStream = new FileInputStream(secondLocalFile);


            System.out.println("Start uploading second file");
            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;

            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();

            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                System.out.println("The second file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
