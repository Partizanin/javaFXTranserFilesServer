package sample.company;

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


}
