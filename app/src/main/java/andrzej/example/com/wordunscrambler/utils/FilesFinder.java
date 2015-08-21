package andrzej.example.com.wordunscrambler.utils;

import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrzej on 21.08.15.
 */
public class FilesFinder {
    String path;

    public FilesFinder(String path) {
        this.path = path;
    }


    public List<File> parseDirectory(String dirPath) {

        String curPath = dirPath;

        List<File> files = new ArrayList<>();
        List<String> dirContents = getContents(curPath);



        for (String element : dirContents) {

            if (element.startsWith("/")) {
                element = element.substring(1);
            }

            File file = null;

            if (curPath.endsWith("/")) {
                file = new File(curPath + element);
            } else {
                file = new File(curPath + "/" + element);
            }

            if (file.isDirectory()) {
                List<File> tempFiles = parseDirectory(file.toString());

                if (tempFiles.size() > 0)
                    files.addAll(tempFiles);
            } else {
                String extension = FilenameUtils.getExtension(file.getName());
                if (Arrays.asList(PathObject.WHITELISTED_EXTENSIONS).contains(extension))
                    files.add(file);
            }
        }

        return files;
    }

    public String getPath(String dir) {

        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }

        if (getPath().endsWith("/")) {
            return getPath() + dir;
        } else {
            return getPath() + "/" + dir;
        }
    }

    private List<String> getContents(String path) {
        // Read all files sorted into the values-array
        List<String> values = new ArrayList();
        File dir = new File(path);

        if (dir.canRead()) {
            String[] list = dir.list();
            if (list != null) {
                for (String file : list) {
                    if (!file.startsWith(".")) {
                        values.add(file);
                    }
                }
            }
            Collections.sort(values);
        }

        return values;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
