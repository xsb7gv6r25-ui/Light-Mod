package com.lightclient.math;

public final class AimModel {
    public static final int FEATURES = 5;

    private final double[] weights = new double[FEATURES];
    private double bias;
    private final double learningRate;
    private final double l2;
    private int samples;

    public AimModel(double learningRate, double l2) {
        this.learningRate = learningRate;
        this.l2 = l2;
        weights[0] = -0.06;
        weights[1] = -0.06;
        weights[2] = -0.10;
        weights[3] = -0.05;
        weights[4] = -0.004;
        bias = 1.2;
    }

    public double predict(double[] x) {
        double z = bias;
        for (int i = 0; i < FEATURES; i++) z += weights[i] * x[i];
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public void train(double[] x, int label) {
        double p = predict(x);
        double err = label - p;
        for (int i = 0; i < FEATURES; i++) {
            weights[i] += learningRate * (err * x[i] - l2 * weights[i]);
        }
        bias += learningRate * err;
        samples++;
    }

    public int samples() {
        return samples;
    }
}
