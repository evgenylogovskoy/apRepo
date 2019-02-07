package Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.google.common.base.Joiner;

public final class SurefireXmlReferencesPersister implements ReferencesPersister {

    private final SAXBuilder jdomBuilder = new SAXBuilder();
    private final XPathFactory xPathFactory = XPathFactory.instance();
    private final XMLOutputter outputter = new XMLOutputter();
    private final String outputFolder;

    /**
     * Constructor.
     *
     * @param outputFolder output folder
     */
    public SurefireXmlReferencesPersister(String outputFolder) {
        this.outputFolder = outputFolder;
        outputter.setFormat(Format.getPrettyFormat());
    }

    @Override
    public void persist(Map<String, Map<String, References>> references) {
        for (Entry<String, Map<String, References>> referencesPerClassEntry : references.entrySet()) {
            String className = referencesPerClassEntry.getKey();

            // parse the XML file, find the relevant <testcase> elements, add attributes to it, save the
            // XML file
            try {
                File file = getSurefireXmlFileFor(className);
                Document document = jdomBuilder.build(file);
                for (Entry<String, References> referencesPerMethodEntry : referencesPerClassEntry.getValue()
                        .entrySet()) {
                    addReferencesToDocument(document, referencesPerMethodEntry);
                }
                save(document, file);
            } catch (Exception e) {
                throw new RuntimeException("Failed to persist references to Surefire XML files.", e);
            }
        }
    }

    private void addReferencesToDocument(Document document,
                                         Entry<String, References> referencesPerMethodEntry) {
        Element testcase = getTestcaseElementFromDocument(document, referencesPerMethodEntry.getKey());
        References references = referencesPerMethodEntry.getValue();

        if (!references.getRequirements().isEmpty()) {
            testcase.setAttribute("testCaseId", collectionToString(references.getRequirements()));
        }

        if (!references.getRisks().isEmpty()) {
            testcase.setAttribute("risks", collectionToString(references.getRisks()));
        }
    }

    private String collectionToString(Collection<String> collection) {
        return Joiner.on(',').join(collection);
    }

    private Element getTestcaseElementFromDocument(Document document, String testcaseNaame) {
        XPathExpression<Element> xPathExpression = xPathFactory.compile(
                getTestcaseXPath(testcaseNaame), Filters.element());

        return xPathExpression.evaluate(document).get(0);
    }

    private String getTestcaseXPath(String testcaseNaame) {
        return "//testcase[@name='" + testcaseNaame + "']";
    }

    private void save(Document document, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            outputter.output(document, fos);
        }
    }

    private File getSurefireXmlFileFor(String className) throws IOException {
        return new File(outputFolder, getSurefireXmlFileNameFor(className)).getCanonicalFile();
    }

    private String getSurefireXmlFileNameFor(String className) {
        return "TEST-" + className + ".xml";
    }


}