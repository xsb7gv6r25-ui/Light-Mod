package com.lightclient.math;

import java.util.SplittableRandom;

public final class ValueNoise {
    private final int[] table = new int[512];
    private final double[] gradients = new double[256];

    public ValueNoise(long seed) {
        SplittableRandom rng = new SplittableRandom(seed);
        for (int i = 0; i < 256; i++) {
            table[i] = i;
            gradients[i] = rng.nextDouble() * 2 - 1;
        }
        for (int i = 255; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = table[i];
            table[i] = table[j];
            table[j] = tmp;
        }
        System.arraycopy(table, 0, table, 256, 256);
    }

    public double sample(double x) {
        int xi = (int) Math.floor(x) & 255;
        double xf = x - Math.floor(x);
        double u = xf * xf * (3 - 2 * xf);
        double a = gradients[table[xi]];
        double b = gradients[table[xi + 1]];
        return a + (b - a) * u;
    }
}
