// DocumentProcessor.java
public interface DocumentProcessor {
    void loadDocument(String fileName);
    void saveDocument(String fileName);
}

// DocxProcessor.java
class DocxProcessor implements DocumentProcessor {
    @Override
    public void loadDocument(String fileName) {
        System.out.println("Loading Word document: " + fileName);
    }

    @Override
    public void saveDocument(String fileName) {
        System.out.println("Saving Word document: " + fileName);
    }
}

// PdfProcessor.java
class PdfProcessor implements DocumentProcessor {
    @Override
    public void loadDocument(String fileName) {
        System.out.println("Loading PDF document: " + fileName);
    }

    @Override
    public void saveDocument(String fileName) {
        System.out.println("Saving PDF document: " + fileName);
    }
}

// TxtProcessor.java
class TxtProcessor implements DocumentProcessor {
    @Override
    public void loadDocument(String fileName) {
        System.out.println("Loading Text document: " + fileName);
    }

    @Override
    public void saveDocument(String fileName) {
        System.out.println("Saving Text document: " + fileName);
    }
}