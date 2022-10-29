package topcoder;

public class BadNeighbors {

    public static void main(String[] args) {
        System.out.println(new BadNeighbors().maxDonations(new int[] {94, 40, 49, 65, 21, 21, 106, 80, 92, 81, 679, 4, 61, 6, 237, 12, 72, 74, 29, 95, 265, 35, 47, 1, 61, 397, 52, 72, 37, 51, 1, 81, 45, 435, 7, 36, 57, 86, 81, 72}));
    }

    public int maxDonations(int[] donations) {
        final int n = donations.length;

        if (n <= 0) {
            return 0;
        }

        if (n == 1) {
            return donations[0];
        }

        return solveForMoreThanOne(donations);
    }

    private int solveForMoreThanOne(final int[] donations) {
        final int n = donations.length;
        final int[] maxDonationIOnwardsExcludingLast = new int[donations.length];
        final int[] maxDonationsIOnwards = new int[donations.length];

        maxDonationIOnwardsExcludingLast[n - 1] = 0;
        maxDonationsIOnwards[n - 1] = donations[n - 1];

        maxDonationIOnwardsExcludingLast[n - 2] = donations[n - 2];
        maxDonationsIOnwards[n - 2] = Math.max(donations[n - 2], maxDonationsIOnwards[ n - 1]);

        for (int i = n - 3; i >= 0; i--) {
            maxDonationIOnwardsExcludingLast[i] = Math.max(maxDonationIOnwardsExcludingLast[i + 2] + donations[i],
                                                           maxDonationIOnwardsExcludingLast[i + 1]);
            maxDonationsIOnwards[i] = Math.max(maxDonationsIOnwards[i + 2] + donations[i], maxDonationsIOnwards[i + 1]);
        }

        return Math.max(maxDonationIOnwardsExcludingLast[0], maxDonationsIOnwards[1]);
    }
}
