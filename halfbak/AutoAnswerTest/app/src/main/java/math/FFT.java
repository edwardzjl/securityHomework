package math;

/**
 * Created by edwardlol on 15/3/4.
 */
public class FFT {
    private static double[] r_data = null;
    private static double[] i_data = null;

    public static double[] magnitudeSpectrum(double[] realPart) {
        double[] imaginaryPart = new double[realPart.length];

        for (int i = 0; i < imaginaryPart.length; i++) {
            imaginaryPart[i] = 0;
        }
        forwardFFT(realPart, imaginaryPart);

        for (int i = 0; i < realPart.length; i++) {
            realPart[i] = Math.sqrt(r_data[i] * r_data[i] + i_data[i] * i_data[i]);
        }

        return realPart;
    }

    //	 swap Zi with Zj
    private static void swapInt(int i, int j) {
        double tempr;
        int ti;
        int tj;
        ti = i - 1;
        tj = j - 1;
        tempr = r_data[tj];
        r_data[tj] = r_data[ti];
        r_data[ti] = tempr;
        tempr = i_data[tj];
        i_data[tj] = i_data[ti];
        i_data[ti] = tempr;
    }

    private static void bitReverse2() {
        /* bit reversal */
        int n = r_data.length;
        int j = 1;
        int k;

        for (int i = 1; i < n; i++) {
            if (i < j) swapInt(i, j);
            k = n / 2;
            while (k >= 1 && k < j) {
                j = j - k;
                k = k / 2;
            }
            j = j + k;
        }
    }

    public static void forwardFFT(double in_r[], double in_i[]) {
        int id;
        int localN;
        double wtemp, Wjk_r, Wjk_i, Wj_r, Wj_i;
        double theta, tempr, tempi;

        //int numBits = (int) Tools.log2(in_r.length);
        int numBits = (int)(Math.log10(in_r.length) / Math.log10(2.0));

        // Truncate input data to a power of two
        int n = 1 << numBits;
        int nby2;

        // Copy passed references to variables to be used within
        // fft routines & utilities
        r_data = in_r;
        i_data = in_i;

        bitReverse2();
        for (int m = 1; m <= numBits; m++) {
            localN = 1 << m;

            nby2 = localN / 2;
            Wjk_r = 1;
            Wjk_i = 0;

            theta = Math.PI / nby2;

            // for recursive comptutation of sine and cosine
            Wj_r = Math.cos(theta);
            Wj_i = -Math.sin(theta);

            for (int j = 0; j < nby2; j++) {
                // This is the FFT innermost loop
                // Any optimizations that can be made here will yield
                // great rewards.
                for (int k = j; k < n; k += localN) {
                    id = k + nby2;
                    tempr = Wjk_r * r_data[id] - Wjk_i * i_data[id];
                    tempi = Wjk_r * i_data[id] + Wjk_i * r_data[id];

                    // Zid = Zi -C
                    r_data[id] = r_data[k] - tempr;
                    i_data[id] = i_data[k] - tempi;
                    r_data[k] += tempr;
                    i_data[k] += tempi;
                }

                // (eq 6.23) and (eq 6.24)

                wtemp = Wjk_r;

                Wjk_r = Wj_r * Wjk_r - Wj_i * Wjk_i;
                Wjk_i = Wj_r * Wjk_i + Wj_i * wtemp;
            }
        }
        in_r = r_data;
        in_r = i_data;
    }
}
