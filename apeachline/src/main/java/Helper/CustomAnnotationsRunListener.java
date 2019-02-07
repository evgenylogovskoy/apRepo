package Helper;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomAnnotationsRunListener extends RunListener {
    // default output folder
    private static final String SUREFIRE_REPORTS_FOLDER = "target/surefire-reports";

    // key of the 1st map is the test class name, key of the 2nd map the test method name
    private final Map<String, Map<String, References>> references = new HashMap<>();

    private final String outputFolder;

    /**
     * Constructor that defines the output folder to be relative to the current dir at
     * "target/surefire-reports".
     */
    public CustomAnnotationsRunListener() {
        this(SUREFIRE_REPORTS_FOLDER);
    }

    /**
     * Constructor.
     *
     * @param outputFolder output folder to be relative to the current dir
     */
    public CustomAnnotationsRunListener(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @Override
    public void testRunFinished(Result result) {
        if (!references.isEmpty()) {
            new SurefireXmlReferencesPersister(outputFolder).persist(references);
            new JsonReferencesPersister(outputFolder).persist(references);
        }
    }

    @Override
    public void testStarted(Description description) {
        addAllRequirements(description);
        addAllRisks(description);
    }

    private void addAllRequirements(Description description) {
        Class<?> testClass = description.getTestClass();
        String methodName = description.getMethodName();

        TestCaseId classTestCaseIdAnnotation = testClass.getAnnotation(TestCaseId.class);
        TestCaseId methodTestCaseIdAnnotation = description.getAnnotation(TestCaseId.class);
        if (methodTestCaseIdAnnotation != null) {
            addTestCaseId(testClass.getName(), methodName, methodTestCaseIdAnnotation.testCaseId());
        }
        if (classTestCaseIdAnnotation != null) {
            addTestCaseId(testClass.getName(), methodName, classTestCaseIdAnnotation.testCaseId());
        }
    }

    private void addAllRisks(Description description) {
        Class<?> testClass = description.getTestClass();
        String methodName = description.getMethodName();

        Risk classRiskAnnotation = testClass.getAnnotation(Risk.class);
        Risk methodRiskAnnotation = description.getAnnotation(Risk.class);
        if (methodRiskAnnotation != null) {
            addRisks(testClass.getName(), methodName, methodRiskAnnotation.references());
        }
        if (classRiskAnnotation != null) {
            addRisks(testClass.getName(), methodName, classRiskAnnotation.references());
        }
    }

    private void addTestCaseId(String className, String methodName, String[] references) {
        References methodReferences = getMethodReferences(className, methodName);
        Collections.addAll(methodReferences.getRequirements(), references);
    }

    private void addRisks(String className, String methodName, String[] references) {
        References methodReferences = getMethodReferences(className, methodName);
        Collections.addAll(methodReferences.getRisks(), references);
    }

    private References getMethodReferences(String className, String methodName) {
        Map<String, References> classReferences = getClassReferences(className);
        References methodReferences = classReferences.get(methodName);
        if (methodReferences == null) {
            methodReferences = new References();
            classReferences.put(methodName, methodReferences);
        }
        return methodReferences;
    }

    private Map<String, References> getClassReferences(String className) {
        Map<String, References> classReferences = references.get(className);
        if (classReferences == null) {
            classReferences = new HashMap<>();
            references.put(className, classReferences);
        }
        return classReferences;
    }
}
