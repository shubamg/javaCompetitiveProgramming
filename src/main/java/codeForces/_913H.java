package codeForces;

import io.InputReader;
import math.MathUtils;
import math.ModuloCalculator;
import math.PolynomialCalculator;
import util.Decimal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class _913H {

    private final ModuloCalculator moduloCalculator;
    private final PolynomialCalculator polynomialCalculator;
    private final int N;
    private final Map<Decimal, Long> decimalToLongMap = new HashMap<>();
    private NavigableMap<Decimal, long[]> oldInterval2PolyMap = new TreeMap<>();
    private NavigableMap<Decimal, long[]> newInterVal2PolyMap = new TreeMap<>();
    private final List<Decimal> x_es;
    private static final Decimal ZERO_DECIMAL = Decimal.getInteger(0, true);
    private static final Decimal ONE = Decimal.getInteger(1, true);

    private _913H(final ModuloCalculator moduloCalculator,
                  final PolynomialCalculator polynomialCalculator,
                  final String[] decimalStrings) {
        this.moduloCalculator = moduloCalculator;
        this.polynomialCalculator = polynomialCalculator;
        this.N = decimalStrings.length;
        x_es = Collections.unmodifiableList(Arrays.stream(decimalStrings)
                                                  .map(MathUtils::getDecimal)
                                                  .collect(Collectors.toList()));
        for (final Decimal x : x_es) {
            addLongOfDecimal(x);
        }
    }

    private long solve() {
        init();
        for (int i = 1; i < N; i++) {
            switchMaps();
            final Decimal hardUpperLimit = x_es.get(i);
            final Iterable<Decimal> nextIterPoints = getNewPointsList();
            for (final Decimal point : nextIterPoints) {
                newInterVal2PolyMap.put(point, getIntegrationOfLen1(point, oldInterval2PolyMap));
                decimalToLongMap.computeIfAbsent(point, k -> k.getLong(moduloCalculator));
            }
            //TODO: Shubham -> decimal to long map might be cleared over here
            newInterVal2PolyMap.tailMap(hardUpperLimit).clear();
            final Map.Entry<Decimal, long[]> largestEntry = newInterVal2PolyMap.lastEntry();
            // largestEntry is not null as zero will always be there
            if(!polynomialCalculator.isZero(largestEntry.getValue())) {
                newInterVal2PolyMap.put(hardUpperLimit, PolynomialCalculator.getZeroPolynomial());
            }
        }
        return integrateFully();
    }

    private long integrateFully() {
        long result = 0;
        Decimal ub = null;
        Decimal lb;
        long[] poly = null;
        for(final Map.Entry<Decimal, long[]> entry : newInterVal2PolyMap.entrySet()) {
            lb = ub;
            ub = entry.getKey();
            if (lb != null) {
                result = moduloCalculator.add(result, polynomialCalculator.integrate(poly,
                                                         new long[]{decimalToLongMap.get(lb)},
                                                         new long[]{decimalToLongMap.get(ub)})[0]);
            }
            poly = entry.getValue();
        }
        return result;
    }

    // Integrate from (highestPoint-1) to highestPoint
    private long[] getIntegrationOfLen1(final Decimal highestPoint, final NavigableMap<Decimal, long[]> oldPt2Poly) {
        long[] result = PolynomialCalculator.getZeroPolynomial();
        result = polynomialCalculator.add(result, integrateRightPartition(highestPoint, oldPt2Poly));
        result = polynomialCalculator.add(result, integrateMiddlePartitions(oldPt2Poly, highestPoint));
        result = polynomialCalculator.add(result, integrateLeftPartition(highestPoint, oldPt2Poly));
        return result;
    }

    private long[] integrateRightPartition(final Decimal highestPoint, final NavigableMap<Decimal, long[]> oldPt2Poly) {
        final long[] X = new long[]{1, 0};
        Decimal ub = oldPt2Poly.floorKey(highestPoint); // won't be null
        assert ub != null;
        return polynomialCalculator.integrate(oldPt2Poly.get(ub), new long[]{decimalToLongMap.get(ub)}, X);
    }

    private long[] integrateLeftPartition(final Decimal highestPoint, final NavigableMap<Decimal, long[]> oldPt2Poly) {
        final long[] X_MINUS_1 = new long[]{1, -1};
        final Decimal lowerLimit = oldPt2Poly.floorKey(highestPoint.subtract(ONE));
        if (lowerLimit == null) {
            return PolynomialCalculator.getZeroPolynomial();
        }
        final long[] integrand = oldPt2Poly.get(lowerLimit);
        Decimal upperLimit = oldPt2Poly.higherKey(lowerLimit);
        if (upperLimit == null || upperLimit.compareTo(highestPoint) > 0) {
            return polynomialCalculator.integrate(integrand, X_MINUS_1, new long[]{decimalToLongMap.get(lowerLimit)});
        }
        return polynomialCalculator.integrate(integrand, X_MINUS_1, new long[]{decimalToLongMap.get(upperLimit)});
    }

    private long[] integrateMiddlePartitions(final NavigableMap<Decimal, long[]> oldPt2Poly, Decimal highestPoint) {
        final Decimal lowestPoint = highestPoint.subtract(ONE);
        Decimal ub = oldPt2Poly.floorKey(highestPoint); // won't be null
        assert ub != null;
        Decimal lb = oldPt2Poly.lowerKey(ub);
        long[] result = PolynomialCalculator.getZeroPolynomial();
        while (lb != null && lb.compareTo(lowestPoint) > 0) {
            final long[] integrand = oldPt2Poly.get(lb);
            final long lowerLimit = decimalToLongMap.get(lb);
            final long upperLimit = decimalToLongMap.get(ub);
            final long[] deltaIntegration = polynomialCalculator.integrate(integrand,
                                                                           new long[]{lowerLimit},
                                                                           new long[]{upperLimit});
            result = polynomialCalculator.add(result, deltaIntegration);
            ub = lb;
            lb = oldPt2Poly.lowerKey(lb);
        }
        return result;
    }

    private Iterable<Decimal> getNewPointsList() {
        final Set<Decimal> setOfPoints = new HashSet<>();
        for (final Decimal point : oldInterval2PolyMap.keySet()) {
            setOfPoints.add(point);
            setOfPoints.add(point.add(ONE));
        }
        final List<Decimal> listOfPoints = new ArrayList<>(setOfPoints);
        Collections.sort(listOfPoints);
        return listOfPoints;
    }

    private void switchMaps() {
        final NavigableMap<Decimal, long[]> temp = newInterVal2PolyMap;
        newInterVal2PolyMap = oldInterval2PolyMap;
        oldInterval2PolyMap = temp;
        newInterVal2PolyMap.clear();
    }

    private void init() {
        final Decimal firstUb = min(ONE, x_es.get(0));
        addLongOfDecimal(ZERO_DECIMAL);
        addLongOfDecimal(firstUb);
        final long[] firstPoly = new long[]{1};
        newInterVal2PolyMap.put(ZERO_DECIMAL, firstPoly);
        newInterVal2PolyMap.put(firstUb, PolynomialCalculator.getZeroPolynomial());
    }

    private void addLongOfDecimal(final Decimal firstUb) {
        decimalToLongMap.put(firstUb, firstUb.getLong(moduloCalculator));
    }

    private static Decimal min(final Decimal d1, final Decimal d2) {
        if (d1.compareTo(d2) < 0) {
            return d1;
        }
        return d2;
    }

    public static void main(String[] args) {
        final long BASE = 998244353;
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int N = reader.nextInt();
        final String[] x_es = new String[N];
        final ModuloCalculator moduloCalculator = new ModuloCalculator(BASE);
        final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(moduloCalculator);
        for (int i = 0; i < N; i++) {
            x_es[i] = reader.next();
        }
        final _913H solver = new _913H(moduloCalculator, polynomialCalculator, x_es);
        System.out.println(solver.solve());
    }
}
