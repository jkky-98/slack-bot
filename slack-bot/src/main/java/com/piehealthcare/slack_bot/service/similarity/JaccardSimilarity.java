package com.piehealthcare.slack_bot.service.similarity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity {

    public static double calculate(String text1, String text2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(text1.split(" ")));
        Set<String> set2 = new HashSet<>(Arrays.asList(text2.split(" ")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}
