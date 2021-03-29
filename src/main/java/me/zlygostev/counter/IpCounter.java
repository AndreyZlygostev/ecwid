package me.zlygostev.counter;

public interface IpCounter<IpType> {
    void count(IpType ipOctets);

    long getUnique();

    long getTotal();
}
