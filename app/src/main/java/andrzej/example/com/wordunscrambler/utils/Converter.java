package andrzej.example.com.wordunscrambler.utils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by andrzej on 21.08.15.
 */
public class Converter {
    public static byte[] fileToBytesArray(File file) {

        FileInputStream fileInputStream = null;

        byte[] bFile = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bFile;
    }
}
