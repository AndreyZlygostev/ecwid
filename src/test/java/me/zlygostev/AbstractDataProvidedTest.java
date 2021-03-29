package me.zlygostev;

import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractDataProvidedTest {
    InputStream getResourceInputStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream("me.zlygostev/" + fileName);
    }

    List<String> readAll(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceInputStream(filePath)))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    @DataProvider
    public static Object[][] getFiles() {
        return new Object[][]{
                {"test-01.txt"}
        };
    }
}
