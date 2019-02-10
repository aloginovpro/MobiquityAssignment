package com.mobiquityinc.packer;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;


public class CombinatorTest {

    private static final Thing A = new Thing("a", 5.0, 10);
    private static final Thing B = new Thing("b", 5.0, 20);
    private static final Thing C = new Thing("c", 10.0, 10);
    private static final Thing D = new Thing("d", 0.0, 1);

    @Test
    public void testZeroWeight() {
        Optional<List<Thing>> bestCombination = Combinator.findBestCombination(
                0,
                ImmutableList.of(A, B, C)
        );
        assertFalse(bestCombination.isPresent());
    }

    @Test
    public void testSameWeight() {
        Optional<List<Thing>> bestCombination = Combinator.findBestCombination(
                5.0,
                ImmutableList.of(A, B)
        );
        assertTrue(bestCombination.isPresent());
        assertEquals("with same weight, higher cost should be chosen",
                ImmutableList.of(B), bestCombination.get());
    }

    @Test
    public void testSameCost() {
        Optional<List<Thing>> bestCombination = Combinator.findBestCombination(
                10.0,
                ImmutableList.of(A, C)
        );
        assertTrue(bestCombination.isPresent());
        assertEquals("with same cost, lower weight should be chosen",
                ImmutableList.of(A), bestCombination.get());
    }

    @Test
    public void testHighMaxWeight() {
        Optional<List<Thing>> bestCombination = Combinator.findBestCombination(
                100.0,
                ImmutableList.of(A, B, C, D)
        );
        assertTrue(bestCombination.isPresent());
        assertEquals(ImmutableList.of(A, B, C, D), bestCombination.get());
    }

    @Test
    public void testLowMaxWeight() {
        Optional<List<Thing>> bestCombination = Combinator.findBestCombination(
                12.0,
                ImmutableList.of(A, B, C, D)
        );
        assertTrue(bestCombination.isPresent());
        assertEquals(ImmutableList.of(A, B, D), bestCombination.get());
    }

}
