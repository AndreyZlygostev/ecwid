package me.zlygostev;

import me.zlygostev.parser.StringIpParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserTest extends AbstractDataProvidedTest{
    StringIpParser ipParser = new StringIpParser();

    @Test
    public void simpleTest() {
        check("123", false);
        check("127.0.0", false);
        check("127.0.0.", false);
        check("127.0.0.500", false);
        check("127.0.0.1", true);
        check("1.2.3.4", true);
        check("-1.2.3.4", false);
    }

    @Test(dataProvider = "getFiles")
    public void bulkTest(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceInputStream(fileName)))) {
             reader.lines().forEach(this::checkParse);
        }
    }

    private void checkParse(String ip) {
        ipParser.parse(ip);
        Assert.assertEquals(ip, ipParser.toString());
    }

    private boolean isValidIpV4(String address) {
        StringIpParser ipParser = new StringIpParser();
        try {
            ipParser.parse(address);
        } catch (/*IpFormatException | */AssertionError ignore) {
            return false;
        }
        return true;
    }

    private void check(String ip, boolean valid) {
        Assert.assertEquals(isValidIpV4(ip), valid);
    }

}
