package sample.company;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 9:47.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
class TransferObject implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    private ArrayList<File> files = new ArrayList<File>();

    private String message = "";

    public TransferObject(ArrayList<File> files) {
        this.files = files;
    }

    TransferObject(ArrayList<File> files, String message) {
        this.files = files;
        this.message = message;
    }

    public TransferObject(String message) {
        this.message = message;
    }

    public TransferObject() {

    }

    public void addFile(File file){
        files.add(file);
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferObject that = (TransferObject) o;

        return files != null ? files.equals(that.files) : that.files == null && (message != null ? message.equals(that.message) : that.message == null);

    }

    @Override
    public int hashCode() {
        int result = files != null ? files.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransferObject{" +
                "files=" + files +
                ", message='" + message + '\'' +
                '}';
    }
}
