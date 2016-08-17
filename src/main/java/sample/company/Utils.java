package sample.company;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 16:13.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
public class Utils {
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

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
        String server = getFTPIpAddress();
        int port = getFTPPort();
        String user = getFTPUserName();
        String pass = getFTPPassword();

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            System.out.println("changeWorkingDirectory: " +
                    ftpClient.changeWorkingDirectory(getFTPFileDirectory()));


            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("D:/test/Projects.zip");

            String firstRemoteFile = "Projects.zip";
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file \"Projects.zip\"is uploaded successfully.");
            }

            // APPROACH #2: uploads second file using an OutputStream
            File secondLocalFile = new File("D:/test/Report.doc");
            String secondRemoteFile = "Report.doc";
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
                System.out.println("The second file \"test/Report.doc\"is uploaded successfully.");
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

    private String getFTPFileDirectory() {
        return getProperties("ftp.filePath");
    }

    private String getFTPPassword() {
        return getProperties("ftp.password");
    }

    private String getFTPUserName() {
        return getProperties("ftp.userName.login");
    }

    private int getFTPPort() {
        return Integer.parseInt(getProperties("ftp.port"));
    }

    private String getProperties(String key) {
        String result = "";
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = getClass().getClassLoader().getResourceAsStream("settings.properties");

            // load a properties file
            prop.load(input);
            // get the property value and print it out

            result = prop.getProperty(key);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private String getFTPIpAddress() {
        return getProperties("ftp.ipAddress");
    }

}
