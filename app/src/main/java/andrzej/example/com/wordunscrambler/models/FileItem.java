package andrzej.example.com.wordunscrambler.models;

/**
 * Created by andrzej on 20.08.15.
 */
public class FileItem {
    private String name;
    private String path;
    private boolean directory;

    public FileItem(String name, String path, boolean directory) {
        this.name = name;
        this.path = path;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
