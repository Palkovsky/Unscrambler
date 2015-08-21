package andrzej.example.com.wordunscrambler.models;

/**
 * Created by andrzej on 20.08.15.
 */
public class FileItem {
    private String name;
    private boolean directory;

    public FileItem(String name, boolean directory) {
        this.name = name;
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
}
