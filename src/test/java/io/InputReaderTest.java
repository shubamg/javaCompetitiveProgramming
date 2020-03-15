package io;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReaderTest {
    private final BufferedReader br = Mockito.mock(BufferedReader.class);

    @Before
    public void setUp() throws IOException {
        Mockito.when(br.readLine())
               .thenReturn("a b")
               .thenReturn("1 2 ")
               .thenReturn("1.2\t1.3")
               .thenReturn("123456789012345")
               .thenReturn("abc def")
               .thenReturn(null);
    }

    @Test
    public void test() {
        InputReader inputReader = new InputReader(br);
        {
            final char c1 = inputReader.nextChar();
            Assert.assertEquals('a', c1);
            final char c2 = inputReader.nextChar();
            Assert.assertEquals('b', c2);
        }
        {
            final int i1 = inputReader.nextInt();
            Assert.assertEquals(1, i1);
            final int i2 = inputReader.nextInt();
            Assert.assertEquals(2, i2);
        }
        {
            final double DELTA = 0.001;
            final double d1 = inputReader.nextDouble();
            Assert.assertEquals(1.2, d1, DELTA);
            final double d2 = inputReader.nextDouble();
            Assert.assertEquals(1.3, d2, DELTA);
        }
        {
            final long l1 = inputReader.nextLong();
            Assert.assertEquals(123456789012345L, l1);
        }
        {
            final String s1 = inputReader.nextLine();
            Assert.assertEquals("abc", s1);
            final String s2 = inputReader.nextLine();
            Assert.assertEquals("def", s2);
        }
    }
}