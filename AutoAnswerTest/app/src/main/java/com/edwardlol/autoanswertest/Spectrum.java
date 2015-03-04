package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/3/4.
 */
public class Spectrum {
    private double[] spectrum;
    private int length;

    public Spectrum(double[] spectrum) {
        this.spectrum = spectrum;
        this.length = spectrum.length;
    }

    public void normalize() {
        double maxValue = 0.0;

        for (int i = 0; i < length; ++i) {
            if (maxValue < spectrum[i]) {
                maxValue = spectrum[i];
            }
        }
        if (maxValue != 0) {
            for (int i = 0; i < length; ++i) {
                spectrum[i] /= maxValue;
            }
        }
    }

    public double get(int index) {
        return spectrum[index];
    }
}
