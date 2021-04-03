package ist.meic.pava;

import ist.meic.pava.example.ExampleClass;
import ist.meic.pava.example.ExampleClassExtended;

import java.io.*;

public class Testing {
    public static void main(String[] args) {
        File outputFile = setErrorOutputFile();

        System.err.println("Starting test 1");
        ExampleClass.example1();

        System.err.println("\nStarting test 2");
        ExampleClass.example2();

        System.err.println("\nStarting test 3");
        ExampleClass.example3();

        System.err.println("\nStarting test 4");
        ExampleClass.example4();

        System.err.println("\nStarting test 5");
        ExampleClass.example5();

        System.err.println("\n---------------------------");
        System.err.println("Extended tests");
        System.err.println("---------------------------");

        System.err.println("\nTest for Boxing an Unboxing");
        System.err.println("\nStarting extended test 1");
        ExampleClassExtended.exampleExtended1();

        // compare results of tests to expected results
        compareTestResults(outputFile);
    }

    /**
     * Sets error output to a file
     * @return file where to output all error messages
     */
    public static File setErrorOutputFile() {
        File file = new File("test_results.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        PrintStream ps = new PrintStream(fos);
        System.setErr(ps);
        return file;
    }

    /**
     * Compare results of tests
     * @param outputFile
     */
    public static void compareTestResults(File outputFile) {
        File expectedResults = new File("expected_results.txt");
        FileInputStream fisExpected = null;
        FileInputStream fisResult = null;
        try {
            fisExpected = new FileInputStream(expectedResults);
            fisResult = new FileInputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // read from expected results
        BufferedReader expectedResContent = new BufferedReader(new InputStreamReader(fisExpected));

        // read from actual results
        BufferedReader resContent = new BufferedReader(new InputStreamReader(fisResult));
        StringBuilder sbExpected = new StringBuilder();
        StringBuilder sbResult = new StringBuilder();
        try{
            String resLine;
            while ((resLine = expectedResContent.readLine()) != null) {
                sbExpected.append(resLine);
            }

            String resLine2;
            while ((resLine2 = resContent.readLine()) != null) {
                sbResult.append(resLine2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if(sbExpected.toString().equals(sbResult.toString())) {
            System.out.println("\n---------------------------");
            System.out.println("---- All Tests passed! ----");
            System.out.println("---------------------------");
        } else {
            System.out.println("\n---------------------------");
            System.out.println("------ Failed tests! ------");
            System.out.println("---------------------------");
        }

    }
}
