package com.cribl.logcollector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestData {

    public static Integer defaultLastN = 20;

    public static List<String> generateNumberedLines(Integer numLines) {
        List<String> logLines = IntStream.range(1, numLines + 1)
                .mapToObj(i -> "log line " + i)
                .collect(Collectors.toList());
        return logLines;
    }
}
