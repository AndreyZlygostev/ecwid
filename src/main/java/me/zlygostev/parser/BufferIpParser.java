package me.zlygostev.parser;

import me.zlygostev.ThreadUnsafe;

import java.util.Arrays;

@ThreadUnsafe
public class BufferIpParser {
    private final int[] octets = new int[4];

    public int[] parse(byte[] buffer, int fromIndex, int len) /*throws IpFormatException*/ {
        int octetIndex = 0;

        Arrays.fill(octets, 0);
        byte c = ' ';
        for (int i = fromIndex; i < fromIndex + len; i++) {
            c = buffer[i];
            if (c >= '0' && c <= '9') {
                octets[octetIndex] = octets[octetIndex] * 10 + c - '0';
                assert octets[octetIndex] >= 0 && octets[octetIndex] <= 255;
            } else if (c == '.') {
                octetIndex++;
            } /*else if (c == '\n') {
                break;
            }*/ else {
                assert false;//throw new IpFormatException();
            }
        }
        assert octetIndex == 3 && c != '.';
        return octets;
    }

    @Override
    public String toString() {
        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
    }
}

