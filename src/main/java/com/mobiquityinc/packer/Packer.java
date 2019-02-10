package com.mobiquityinc.packer;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class Packer {

    public static void main(String[] args) {
        pack("src/main/resources/input.txt");
    }

    public static void pack(String fileName){
        List<Parser.ThingsInput> lines = Parser.parseFile(fileName);
        lines.forEach(line -> {
            Optional<List<Thing>> bestCombination = Combinator.findBestCombination(line.maxWeight, line.things);
            String output = bestCombination.map(c -> c.stream().map(t -> t.id).collect(joining(",")))
                    .orElse("-");
            System.out.println(output);
        });
    }





}
