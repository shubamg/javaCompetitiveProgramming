package math;

import java.util.stream.IntStream;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class InterpolatedPolyEvaluator {
    private final long[] x;
    private final long[] y;
    private final long evaluateAt;
    private final ModuloCalculator calculator;

    public InterpolatedPolyEvaluator(final long[] x,
                                     final long[] y,
                                     final long evaluateAt,
                                     final ModuloCalculator calculator) {
        assert x.length == y.length;
        this.x = x;
        this.y = y;
        this.evaluateAt = evaluateAt;
        this.calculator = calculator;
    }

    public long evaluate() {
        assert x.length == y.length;
        return IntStream.range(0, x.length).mapToLong(this::evaluateIthBase).reduce(calculator::add).getAsLong();
    }

    private long evaluateIthBase(final int index) {
        final long numerator = IntStream.range(0, x.length)
                                        .filter(i -> i != index)
                                        .mapToLong(i -> calculator.subtract(evaluateAt, x[i]))
                                        .reduce(calculator::multiply)
                                        .getAsLong();
        final long denominator = IntStream.range(0, x.length)
                                          .filter(i -> i != index)
                                          .mapToLong(i -> calculator.subtract(x[index], x[i]))
                                          .reduce(calculator::multiply)
                                          .getAsLong();
        return calculator.getExactQuotient(calculator.multiply(numerator, y[index]), denominator);
    }
}
