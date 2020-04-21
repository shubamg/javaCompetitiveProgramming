package math;

public class BezoutRepr {
    private final long coeffSmall;
    private final long coeffLarge;
    private final long gcd;
    private final int numCalls;

    public BezoutRepr(final long coeffSmall, final long coeffLarge, final long gcd, final int numCalls) {
        this.coeffSmall = coeffSmall;
        this.coeffLarge = coeffLarge;
        this.gcd = gcd;
        this.numCalls = numCalls;
    }

    public long getCoeffSmall() {
        return coeffSmall;
    }

    public long getCoeffLarge() {
        return coeffLarge;
    }

    public long getGcd() {
        return gcd;
    }

    public int getNumCalls() {
        return numCalls;
    }

    @Override
    public String toString() {
        return "BezoutRepr{" + "coeffSmall=" + coeffSmall + ", coeffLarge=" + coeffLarge + ", gcd=" + gcd
                + ", numCalls=" + numCalls + '}';
    }
}