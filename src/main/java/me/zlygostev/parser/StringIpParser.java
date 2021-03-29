package me.zlygostev.parser;

import me.zlygostev.ThreadUnsafe;

import java.util.Arrays;

@ThreadUnsafe
public class StringIpParser {
    private final int[] octets = new int[4];

    public int[] parse(String ip) /*throws IpFormatException*/ {
        int octetIndex = 0;

        Arrays.fill(octets, 0);
        char c = ' ';
        for (int i = 0; i < ip.length(); i++) {
            c = ip.charAt(i);
            if (c >= '0' && c <= '9') {
                octets[octetIndex] = octets[octetIndex] * 10 + c - '0';
                assert octets[octetIndex] >= 0 && octets[octetIndex] <= 255;
            } else if (c == '.') {
                octetIndex++;
            } else {
                assert false;//throw new IpFormatException();
            }
        }
        assert octetIndex == 3;
        assert c != '.';
        return octets;
    }

    @Override
    public String toString() {
        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
    }
}
