package codeJam._2019;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ForeGone {
    private final static char BASE_ASCII_VALUE = '0';
    private final static String OUT_LINE_TMPL = "Case #%d: %s %s\n";
    private final String nString;

    public ForeGone(final String nString) {
        this.nString = nString;
    }

    public String[] getRes() {
        final StringBuilder[] result = {new StringBuilder(), new StringBuilder()};
        final boolean[] firstNonZeroDigitSeen = {false, false};
        for (final char ch : nString.toCharArray()) {
            final int[] digitPartition = breakDigit(ch - BASE_ASCII_VALUE);
            for (int j = 0; j < 2; j++) {
                if (!firstNonZeroDigitSeen[j]) {
                    if (digitPartition[j] == 0) {
                        continue;
                    }
                    firstNonZeroDigitSeen[j] = true;
                }
                result[j].append(digitPartition[j]);
            }
        }
        return new String[]{result[0].toString(), result[1].toString()};
    }

    private int[] breakDigit(final int d) {
        if (d == 4) {
            return new int[]{1, 3};
        }
        return new int[]{d, 0};
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final StringBuilder result = new StringBuilder();
        final int T = reader.nextInt();
        for (int t = 1; t <= T; t++) {
            final String nString = reader.nextLine();
            final ForeGone foreGone = new ForeGone(nString);
            final String[] unformattedResult = foreGone.getRes();
            result.append(formatOutput(t, unformattedResult));
        }
        System.out.println(result);
    }

    private static String formatOutput(final int t, final String[] input) {
        return String.format(OUT_LINE_TMPL, t, input[0], input[1]);
    }
}

