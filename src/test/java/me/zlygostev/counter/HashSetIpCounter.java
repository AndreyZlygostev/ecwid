package me.zlygostev.counter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class HashSetIpCounter implements IpCounter<String> {
    private final Set<String> uniqueIps = Collections.synchronizedSet(new HashSet<>());
    private final AtomicLong unUniqueCounter = new AtomicLong();

    @Override
    public void count(String ip) {
        if (!uniqueIps.add(ip)) {
            unUniqueCounter.incrementAndGet();
        }
    }

    @Override
    public long getUnique() {
        return uniqueIps.size();
    }

    @Override
    public long getTotal() {
        return unUniqueCounter.get() + uniqueIps.size();
    }
}
