package hackerrank;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DeciBinaryCacheTest {

    @Test
    public void deciBinariesAreAccurate() {
        final DeciBinaryCache cache = new DeciBinaryCache(10);
        final Set<Long> deciBinaryFor4 = new HashSet<>(cache.getDeciBinaryFor(4));
        Assert.assertEquals(Sets.newHashSet(4L, 12L, 20L, 100L), deciBinaryFor4);
    }

    @Test
    public void deciBinariesAreOrdered() {
        final DeciBinaryCache cache = new DeciBinaryCache(10);
        final List<Long> deciBinaryFor4 = cache.getDeciBinaryFor(4);
        Assert.assertEquals(Lists.newArrayList(4L, 12L, 20L, 100L), deciBinaryFor4);
    }

    @Test
    public void compareToOfDeciBinaryForSameDecimal() {
        final DeciBinaryCache.DeciBinary smallDeciBinary = new DeciBinaryCache.DeciBinary(4, 4L);
        final DeciBinaryCache.DeciBinary largeDeciBinary = new DeciBinaryCache.DeciBinary(4, 12L);
        Assert.assertTrue(smallDeciBinary.compareTo(largeDeciBinary) < 0);
        Assert.assertTrue(largeDeciBinary.compareTo(smallDeciBinary) > 0);
    }
}
