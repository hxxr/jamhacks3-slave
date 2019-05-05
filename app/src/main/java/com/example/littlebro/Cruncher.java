package com.example.littlebro;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Cruncher {

    // Compute standard deviation.
    public static double deviation(List<Double> list) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = list.size();
        for (double num : list)
            sum += num;
        double mean = sum/length;
        for(double num: list)
            standardDeviation += Math.pow(num - mean, 2);
        return Math.sqrt(standardDeviation/length);
    }

    // Compute arithmetic mean.
    public static double mean(List<Double> list) {
        double sum = 0.0;
        for (double num : list)
            sum += num;
        return sum/(double)list.size();
    }

    // Compute median.
    public static double median(List<Double> list) {
        Collections.sort(list);
        if (list.size() % 2 != 0)
            return list.get(list.size() / 2);
        return (list.get((list.size() - 1) / 2) + list.get(list.size() / 2)) / 2.0;
    }

    // Compute minimum.
    public static double minimum(List<Double> list) {
        Collections.sort(list);
        return list.get(0);
    }

    // Compute maximum.
    public static double maximum(List<Double> list) {
        Collections.sort(list);
        return list.get(list.size()-1);
    }

    private Cruncher() { }
}
