// DocumentEditor.java
public class DocumentEditor {
    public static void main(String[] args) {
        // Test array of different file types
        String[] files = {"report.docx", "invoice.pdf", "notes.txt"};

        for (String file : files) {
            try {
                // The factory decides which processor to create at runtime
                DocumentProcessor processor = DocumentProcessorFactory.getProcessor(file);
                
                // Execute operations
                processor.loadDocument(file);
                processor.saveDocument(file);
                System.out.println("-----------------------------------");
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}