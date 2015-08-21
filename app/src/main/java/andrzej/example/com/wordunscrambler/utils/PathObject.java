package andrzej.example.com.wordunscrambler.utils;


import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrzej on 20.08.15.
 */
public class PathObject {
    public static final String DEFAULT_PATH = "/";
    public static final String[] WHITELISTED_EXTENSIONS = {"txt"};

    private String path;

    public PathObject(String path) {
        this.path = path;
    }

    public String goUp() {
        if (hasOverridingDirectory()) {
            String path = "";
            String[] pathChunks = getPathChunks();

            if (pathChunks.length > 1) {
                for (int i = 0; i < pathChunks.length - 1; i++) {
                    if (path.endsWith("/"))
                        path += pathChunks[i];
                    else
                        path += "/" + pathChunks[i];
                }
            } else
                path = "/";

            setPath(path);
        } else
            setPath("/");

        return getPath();
    }

    public List<String> getContents() {
        // Read all files sorted into the values-array
        List<String> values = new ArrayList();
        File dir = new File(getPath());

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

    public boolean testFileExtension(String filename) {
        String extension = FilenameUtils.getExtension(filename);

        if (Arrays.asList(WHITELISTED_EXTENSIONS).contains(extension))
            return true;
        return false;
    }

    public String goToDirectory(String directoryName) {
        if (getPath().endsWith("/"))
            setPath(getPath() + directoryName);
        else
            setPath(getPath() + "/" + directoryName);

        return getPath();
    }

    public String getDirectory(String directoryName) {
        if (getPath().endsWith("/"))
            return getPath() + directoryName;
        else
            return getPath() + "/" + directoryName;
    }

    public String overridingDirectory() {
        String[] pathChunks = getPathChunks();
        if (pathChunks.length > 1) {
            return pathChunks[pathChunks.length - 2];
        }
        return "/";
    }

    public String currentDirectory() {
        String[] pathChunks = getPathChunks();


        if (pathChunks.length > 0) {
            return pathChunks[pathChunks.length - 1];
        }
        return "/";
    }

    public boolean hasOverridingDirectory() {
        String[] pathChunks = removeEmptyFieldsFromArray(getPath().split("/"));
        return pathChunks.length > 0;
    }

    private String[] getPathChunks() {
        return removeEmptyFieldsFromArray(getPath().split("/"));
    }

    private String[] removeEmptyFieldsFromArray(String[] inputArray) {
        List<String> list = new ArrayList<String>(Arrays.asList(inputArray));
        list.removeAll(Arrays.asList("", null));
        return list.toArray(new String[list.size()]);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
