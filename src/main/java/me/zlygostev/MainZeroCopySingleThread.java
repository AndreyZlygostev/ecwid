package me.zlygostev;

import me.zlygostev.counter.BitSetIpCounter;
import me.zlygostev.counter.IpCounter;
import me.zlygostev.parser.BufferIpParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainZeroCopySingleThread {
    private IpCounter<int[]> ipCounter;
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log("Input file name must passed throughout 1-st application argument");
            return;
        }
        new MainZeroCopySingleThread().count(args[0]);
    }

    private void count(String fileName) throws IOException {
        long start = System.currentTimeMillis();
        ipCounter = new BitSetIpCounter();
        BufferIpParser parser = new BufferIpParser();
        ZeroCopyReader zeroCopyReader = new ZeroCopyReader();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            zeroCopyReader.read(inputStream, (buf, offset, len) -> {
                int[] ip = parser.parse(buf, offset, len);
                ipCounter.count(ip);
            });
            printResults();
        } finally {
            long duration = System.currentTimeMillis() - start;
            log("Time: %d min %f sec", duration / 60000, (duration % 60000) * 1.0 / 1000);
            if (ipCounter != null && duration > 0) {
                log("%d Ips/msec", ipCounter.getTotal() / duration);
            }
        }
    }

    private void printResults() {
        log("Unique/Total: %d/%d%n", ipCounter.getUnique(), ipCounter.getTotal());
    }

    private static void log(String message, Object... args) {
        System.out.printf(message + "\n", args);
    }
}
