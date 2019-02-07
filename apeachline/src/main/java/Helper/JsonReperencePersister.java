package Helper;
import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

class JsonReferencesPersister implements ReferencesPersister {

    private static final String OUTPUT_FILE_NAME = "references.json";
    private final String outputFolder;


    public JsonReferencesPersister(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public void persist(Map<String, Map<String,References>> references) {
        try {
            File outputFile = new File(outputFolder, OUTPUT_FILE_NAME).getCanonicalFile();
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(outputFile, references);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist references to JSON.", e);
        }
    }

}