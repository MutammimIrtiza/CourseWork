package structural;

import java.util.ArrayList;
import java.util.List;

interface FileSystemItem {
    void display(String indent);
    int getSize();
}

class File implements FileSystemItem {
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }
    public void display(String indent) {
        System.out.println(indent + "File: " + name + " (" + size + "KB)");
    }
    public int getSize() {return size;}
}

class Folder implements FileSystemItem {
    private String name;
    private List<FileSystemItem> children = new ArrayList<>();

    public Folder(String name) {this.name = name;}

    public void add(FileSystemItem item) {children.add(item);}
    public void remove(FileSystemItem item) {children.remove(item);}

    public void display(String indent) {
        System.out.println(indent + "Folder: " + name);
        for(FileSystemItem child : children) {
            child.display(indent + "   ");
        }
    }

    public int getSize() {
        int total = 0;
        for(FileSystemItem child : children) {
            total += child.getSize();
        }
        return total;
    }
}

public class composite {
    public static void main(String[] args) {
        Folder root = new Folder("root");
        root.add(new File("readme.md", 5));
        Folder src = new Folder("src");
        src.add(new File("Main.java", 20));
        src.add(new File("Utils.java", 15));
        root.add(src);

        root.display("");
        System.out.println("Total Size: " + root.getSize() + "KB");
    }
}
