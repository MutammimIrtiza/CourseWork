// DocumentProcessorFactory.java
public class DocumentProcessorFactory {
    
    public static DocumentProcessor getProcessor(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name. Extension missing.");
        }
        
        // Extract the file extension
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        switch (extension) {
            case ".docx":
                return new DocxProcessor();
            case ".pdf":
                return new PdfProcessor();
            case ".txt":
                return new TxtProcessor();
            default:
                throw new UnsupportedOperationException("Unsupported file format: " + extension);
        }
    }
}