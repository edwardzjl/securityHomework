package com.edwardlol.autoanswertest;

import math.FFT;

/**
 * Created by edwardlol on 15/3/4.
 */
public class DataBlock {
    private double[] block;

    public DataBlock(short[] buffer, int blockSize, int bufferReadSize) {
        block = new double[blockSize];

        for (int i = 0; i < blockSize && i < bufferReadSize; i++) {
            block[i] = (double) buffer[i];
        }
    }

    public Spectrum FFT() {
        return new Spectrum(FFT.magnitudeSpectrum(block));
    }
}
