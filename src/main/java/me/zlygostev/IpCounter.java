package me.zlygostev;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class IpCounter {
    private final byte[] ips = new byte[256 * 256 * 256 / 8 * 256];
    private final AtomicLong uniqueCounter = new AtomicLong();
    private final AtomicLong unUniqueCounter = new AtomicLong();

    public void count(String ip) {
        int result = 0;
        int octet = 0;
        int firstOctet = 0;

        for (int i = 0; i < ip.length(); i++) {
            char c = ip.charAt(i);
            if (c >= '0' && c <= '9') {
                octet = octet * 10 + c - '0';
            } else if (c == '.') {
                result = (result << 8) | octet;
                if (firstOctet == 0) {
                    firstOctet = octet;
                }
                octet = 0;
            } else {
                //todo throw exception
            }
        }
        int index = (result << 5) | (octet / 8);

        synchronized (Byte.valueOf((byte) firstOctet)) {
            byte prevValue = ips[index];
            ips[index] |= (1 << (octet % 8));
            if (prevValue != ips[index]) {
                uniqueCounter.incrementAndGet();
            } else {
                unUniqueCounter.incrementAndGet();
            }
        }
    }

    public void printResults() {
        System.out.printf("Unique/Total: %d/%d%n", uniqueCounter.get(), uniqueCounter.get() + unUniqueCounter.get());
    }

    public static void main(String[] args) throws Exception {
        IpCounter ipCounter = new IpCounter();
        Path path = Paths.get("E:\\ip_addresses\\ip_addresses");
        long start = System.currentTimeMillis();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.lines()
                    .parallel()
                    .unordered()
                    .forEach(ipCounter::count);
            ipCounter.printResults();
        } finally {
            System.out.printf("Time: %f sec", (System.currentTimeMillis() - start) * 1.0 / 1000);
        }
    }
}
