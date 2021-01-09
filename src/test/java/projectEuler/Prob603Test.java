package projectEuler;

import math.ModuloCalculator;
import org.junit.Assert;
import org.junit.Test;

public class Prob603Test {

    @Test
    public void solveWithoutMod() {
        {
            final Prob603 solver = new Prob603(ModuloCalculator.getWithoutMod(), 1, "2024");
            Assert.assertEquals(2304, solver.solve());
        }
        {
            final Prob603 solver = new Prob603(ModuloCalculator.getWithoutMod(), 2, "23");
            Assert.assertEquals(2966, solver.solve());
        }
    }

    @Test
    public void solveWithMod() {
        final long MOD_BASE = 1_000_000_007L;
        {
            final Prob603 solver = new Prob603(new ModuloCalculator(MOD_BASE), 1, "2024");
            Assert.assertEquals(2304, solver.solve());
        }
        {
            final Prob603 solver = new Prob603(ModuloCalculator.getWithoutMod(), 2, "23");
            Assert.assertEquals(2966, solver.solve());
        }
    }
}