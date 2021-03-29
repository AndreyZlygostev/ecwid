package me.zlygostev;

import me.zlygostev.counter.HashSetIpCounter;
import me.zlygostev.counter.StringIpCounter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class CounterTest extends AbstractDataProvidedTest {

    @Test(dataProvider = "getFiles")
    private void compareWithSimpleReared(String filePath) throws IOException {
        HashSetIpCounter referenceIpCounter = new HashSetIpCounter();
        StringIpCounter ipCounter = new StringIpCounter();
        List<String> strings = readAll(filePath);
        for (String ip : strings) {
            referenceIpCounter.count(ip);
            ipCounter.count(ip);
        }

        Assert.assertEquals(ipCounter.getTotal(), referenceIpCounter.getTotal());
        Assert.assertEquals(ipCounter.getUnique(), referenceIpCounter.getUnique());
    }

}
