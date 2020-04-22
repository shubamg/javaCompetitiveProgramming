package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class InputReader {
    private final BufferedReader br;
    private StringTokenizer st;

    public InputReader(final BufferedReader br) {
        this.br = br;

    }

    public String next() {
        while (st == null || !st.hasMoreElements()) {
            try {
                final String line = br.readLine();
                if (line == null) {
                    throw new NoSuchElementException();
                }
                st = new StringTokenizer(line);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    // Assumes chars are not concatenated
    public char nextChar() {
        final String str = next();
        assert str.length() == 1;
        return str.charAt(0);
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    /**
     * Converts a decimal number into long[]
     * Eg: 12345.678 --> {12345, 678}
     * @return - long array of size 2
     */
    public long[] nextDecimal() {
        final String fractionString = next();
        return Arrays.stream(fractionString.split("\\.")).mapToLong(Long::valueOf).toArray();
    }
}