package me.zlygostev.counter;

import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBitSetIpCounter implements IpCounter<int[]> {
    private final BitSet[] ips = new BitSet[256];
    private final Object[] locks = new Object[256];

    private final AtomicLong uniqueCounter = new AtomicLong();
    private final AtomicLong unUniqueCounter = new AtomicLong();

    public ConcurrentBitSetIpCounter() {
        Arrays.setAll(ips, set -> new BitSet(1 << 24));
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }

    @Override
    public void count(int[] octets) {
        int bitIndex = 0;
        for (int octetNumber = 1; octetNumber < 4; octetNumber++) {
            bitIndex = (bitIndex << 8) | octets[octetNumber];
        }

        int firstOctet = octets[0];

        synchronized (locks[firstOctet]) {
            if (!ips[firstOctet].get(bitIndex)) {
                ips[firstOctet].set(bitIndex);
                uniqueCounter.incrementAndGet();
            } else {
                unUniqueCounter.incrementAndGet();
            }
        }
    }

    @Override
    public long getUnique() {
        return uniqueCounter.get();
    }

    @Override
    public long getTotal() {
        return uniqueCounter.get() + unUniqueCounter.get();
    }
}
