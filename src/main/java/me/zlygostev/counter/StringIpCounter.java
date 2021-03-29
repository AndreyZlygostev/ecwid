package me.zlygostev.counter;

import me.zlygostev.parser.StringIpParser;

public class StringIpCounter implements IpCounter<String> {
    private final IpCounter<int[]> ipCounter = new ConcurrentBitSetIpCounter();
    private final ThreadLocal<StringIpParser> parser = ThreadLocal.withInitial(StringIpParser::new);

    @Override
    public void count(String ip) {
        int[] octets = parser.get().parse(ip);
        ipCounter.count(octets);
    }

    @Override
    public long getUnique() {
        return ipCounter.getUnique();
    }

    @Override
    public long getTotal() {
        return ipCounter.getTotal();
    }
}
