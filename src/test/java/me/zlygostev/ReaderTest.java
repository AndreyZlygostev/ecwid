package me.zlygostev;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ReaderTest extends AbstractDataProvidedTest {

    @Test(dataProvider = "getFiles")
    private void compareWithSimpleReader(String filePath) throws IOException {
        List<String> strings = readAll(filePath);
        int[] index = new int[1];
        ZeroCopyReader zeroCopyReader = new ZeroCopyReader();
        try (InputStream input = getResourceInputStream(filePath)) {
            zeroCopyReader.read(input, (buf, offset, len) -> {
                byte[] bytes = new byte[20];
                System.arraycopy(buf, offset, bytes, 0, len);
                String ip = new String(bytes, 0, len);
                String expectedIp = strings.get(index[0]++);
                Assert.assertEquals(ip, expectedIp);
            });
        }
    }

}
