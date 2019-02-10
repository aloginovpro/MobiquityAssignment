package com.mobiquityinc.packer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Parser {

    static List<ThingsInput> parseFile(String fileName) {
        try {
            return FileUtils.readLines(new File(fileName), Charset.defaultCharset()).stream()
                    .map(Parser::parseLine).collect(toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file " + fileName, e);
        }
    }

    private static ThingsInput parseLine(String line) {
        String[] split = line.split(" : ");
        double maxWeight = Double.valueOf(split[0]);
        List<Thing> things = Arrays.stream(split[1].split(" ")).map(Parser::parseThing).collect(toList());
        return new ThingsInput(maxWeight, things);
    }

    private static Thing parseThing(String text) {
        String[] split = text.replaceAll("[ ()]", "").split(",");
        return new Thing(split[0], Double.valueOf(split[1]), Integer.valueOf(split[2].replace("€", "")));
    }

    static class ThingsInput {
        final double maxWeight;
        final List<Thing> things;

        ThingsInput(double maxWeight, List<Thing> things) {
            this.maxWeight = maxWeight;
            this.things = things;
        }
    }

}
