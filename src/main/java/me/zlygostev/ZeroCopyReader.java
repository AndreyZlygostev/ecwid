package me.zlygostev;

import java.io.IOException;
import java.io.InputStream;

public class ZeroCopyReader {
    public static final int BUF_SIZE = 8 * 1024 * 1024;

    interface ReadCallback {
        void readLine(byte[] buf, int offset, int len);
    }

/*    int readChar(InputStream inputStream, byte[] cbuf) throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        int read = inputStream.read(buf);
        for (int i = 0; i < buf.length; i++) {
            cbuf[i] = (char) buf[i];
        }
        return read;
    }*/

    public void read(InputStream inputStream, ReadCallback readCallback) throws IOException {
        int bytesRead;
        byte[] buf = new byte[BUF_SIZE];
        byte[] appendixBuf = new byte[16];
        int appendixLen = 0;

        while ((bytesRead = inputStream.read(buf)) > 0) {
            int index = 0;
            int i0 = 0;
            if (appendixLen > 0) {
                for (; index < bytesRead; index++) {
                    if (buf[index] == '\n') {
                        i0 = ++index;
                        break;
                    }
                    appendixBuf[appendixLen + index] = buf[index];
                }
                readCallback.readLine(appendixBuf, 0, i0 == index ? appendixLen + index - 1 : appendixLen + index);
            }
            for (; index < bytesRead; index++) {
                if (buf[index] == '\n') {
                    readCallback.readLine(buf, i0, index - i0);
                    i0 = index + 1;
                }
            }
            appendixLen = bytesRead - i0;
            if (appendixLen > 0) {
                System.arraycopy(buf, i0, appendixBuf, 0, appendixLen);
            }
        }
    }
}
