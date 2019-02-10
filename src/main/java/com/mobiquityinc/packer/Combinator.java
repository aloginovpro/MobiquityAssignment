package com.mobiquityinc.packer;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

/*
Things number: N <= 15
All possible combinations number: C = 2^N - 1
max C = about 30k -> can merely go though all the options

Recursive combinations building algorithm explanation:

source: (a, b, c, d)
|                    |         |     |
a________________    b_____    c     d
|          |    |    |    |    |
ab____     ac   ad   bc   bd   cd
|    |     |         |
abc  abd   acd       bcd
|
abcd

combinations: (a, b, c, d, ab, ac, ad, bc, bd, cd, abc, abd, acd, bcd, abcd)

Little improvement: if current combination overweights maxWeight, no need to check its derivative combinations
*/

public class Combinator {
    private static final Comparator<Combination> COST_COMPARATOR = comparing(s -> s.cost);
    private static final Comparator<Combination> WEIGHT_COMPARATOR = comparing(s -> s.weight);
    private static final Comparator<Combination> COST_WEIGHT_COMPARATOR = COST_COMPARATOR
            .thenComparing(WEIGHT_COMPARATOR.reversed());

    static Optional<List<Thing>> findBestCombination(double maxWeight, List<Thing> source) {
        List<Combination> combinations = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            Combination currentSet = new Combination(source.get(i), i);
            addCombination(maxWeight, source, currentSet, combinations);
        }
        return combinations.stream().max(COST_WEIGHT_COMPARATOR).map(c -> c.elements);
    }

    /**
     * adds current combination and all of its derivatives to the list, if they don't overweight maxWeight
     */
    private static void addCombination(double maxWeight, List<Thing> source, Combination current,
           List<Combination> combinations) {
        if (current.weight > maxWeight) {
            return;
        }
        combinations.add(current);
        for (int i = current.maxIndex + 1; i < source.size(); i++) {
            Thing additionalElement = source.get(i);
            Combination extendedCombination = new Combination(
                    ImmutableList.<Thing>builder().addAll(current.elements).add(additionalElement).build(),
                    current.weight + additionalElement.weight,
                    current.cost + additionalElement.cost,
                    i
            );
            addCombination(maxWeight, source, extendedCombination, combinations);
        }
    }

    private static class Combination {
        final List<Thing> elements;
        final double weight;
        final int cost;
        final int maxIndex;

        Combination(Thing thing, int maxIndex) {
            this(ImmutableList.of(thing), thing.weight, thing.cost, maxIndex);
        }

        Combination(List<Thing> elements, double weight, int cost, int maxIndex) {
            this.elements = elements;
            this.weight = weight;
            this.cost = cost;
            this.maxIndex = maxIndex;
        }
    }
}
