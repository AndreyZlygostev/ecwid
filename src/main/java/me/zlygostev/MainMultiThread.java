package me.zlygostev;

import me.zlygostev.counter.StringIpCounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainMultiThread {
    private StringIpCounter ipCounter;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log("Input file name must passed throughout 1-st application argument");
        }
        new MainMultiThread().count(args[0]);
    }

    private static void log(String message, Object... args) {
        System.out.printf(message + "\n", args);
    }

    private void count(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        long start = System.currentTimeMillis();
        ipCounter = new StringIpCounter();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.lines()//.limit(1000)
                    .unordered()
                    .parallel()
                    .forEach(ipCounter::count);
            printResults();
        } finally {
            long duration = System.currentTimeMillis() - start;
            log("Time: %d min %f sec", duration / 60000, (duration % 60000) * 1.0 / 1000);
            if (ipCounter != null) {
                log("%d Ips/msec", ipCounter.getTotal() / duration);
            }
        }
    }

    public void printResults() {
        log("Unique/Total: %d/%d%n", ipCounter.getUnique(), ipCounter.getTotal());
    }
}
