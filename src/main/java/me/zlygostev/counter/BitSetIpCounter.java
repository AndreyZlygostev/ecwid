package me.zlygostev.counter;

import me.zlygostev.ThreadUnsafe;

import java.util.Arrays;
import java.util.BitSet;

@ThreadUnsafe
public class BitSetIpCounter implements IpCounter<int[]> {
    private final BitSet[] ips = new BitSet[256];

    private long uniqueCounter;
    private long unUniqueCounter;

    public BitSetIpCounter() {
        Arrays.setAll(ips, set -> new BitSet(1 << 24));
    }

    @Override
    public void count(int[] octets) {
        int bitIndex = 0;
        for (int octetNumber = 1; octetNumber < 4; octetNumber++) {
            bitIndex = (bitIndex << 8) | octets[octetNumber];
        }

        int firstOctet = octets[0];
        if (!ips[firstOctet].get(bitIndex)) {
            ips[firstOctet].set(bitIndex);
            uniqueCounter++;
        } else {
            unUniqueCounter++;
        }
    }

    @Override
    public long getUnique() {
        return uniqueCounter;
    }

    @Override
    public long getTotal() {
        return uniqueCounter + unUniqueCounter;
    }
}
