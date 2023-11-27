package com.github.tamadalab.nikusa;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * MainTest
 */
public class MainTest {
    // private static String csvFile = getClass().getClassLoader().getResource("sample.csv").toString();

    @Test
    public void testGetFileExtension() {
        String csvExtension = Main.getFileExtension("app/csv-files/sample.csv");
        assertTrue(Objects.equals(csvExtension, "csv"));

    }

    @Test
    public void testReadCsvFile() {
        List<String> repositories = Main.readCsvFile(new File("./src/main/resources/sample.csv"));
        assertTrue(Objects.equals(repositories.get(0), "android-cjj/android-materialrefreshlayout"));
        assertTrue(Objects.equals(repositories.get(1), "sebas77/-obsolete-lightweight-ioc-container-for-unity3d"));
    }

    @Test
    public void tesetReadJsonFile() {
        List<String> repositories = Main.readJsonFile(new File("./src/main/resources/2020_js.json"));
    }

    @Test
    public void testParseFile() {
        List<String> repositories = Main.parseFile("./src/main/resources/sample.csv");
        assertTrue(Objects.equals(repositories.get(0), "android-cjj/android-materialrefreshlayout"));
        assertTrue(Objects.equals(repositories.get(1), "sebas77/-obsolete-lightweight-ioc-container-for-unity3d"));
    }

    @Test 
    public void testParseArgument() {
        String[] args = {"./src/main/resources/sample.csv"};
        List<String> repositories = Main.parseArgument(args);
        assertTrue(Objects.equals(repositories.get(0), "android-cjj/android-materialrefreshlayout"));
        assertTrue(Objects.equals(repositories.get(1), "sebas77/-obsolete-lightweight-ioc-container-for-unity3d"));
    }

    
}