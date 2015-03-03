package math;

/**
 * Created by edwardlol on 15/3/1.
 */
public class Complex {
    private double real_part;
    private double imaginary_part;

    public Complex(double real, double imaginary) {
        this.real_part = real;
        this.imaginary_part = imaginary;
    }

    public String toString() {
        if(imaginary_part < 0) {
            return new String(real_part + " - i" + Math.abs(imaginary_part));
        } else {
            return new String(real_part + " + i" + imaginary_part);
        }
    }

    public static final Complex multiply(Complex c1, Complex c2) {
        double re = c1.real_part * c2.real_part - c1.imaginary_part * c2.imaginary_part;
        double im = c1.real_part * c2.imaginary_part + c1.imaginary_part * c2.real_part;
        return new Complex(re, im);
    }

    public static Complex scale(Complex c, double x) {
        return new Complex(c.real_part * x, c.imaginary_part * x);
    }

    public static final Complex add(Complex c1, Complex c2) {
        double re = c1.real_part + c2.real_part;
        double im = c1.imaginary_part + c2.imaginary_part;
        return new Complex(re, im);
    }

    public static final Complex substract(Complex c1, Complex c2) {
        double re = c1.real_part - c2.real_part;
        double im = c1.imaginary_part - c2.imaginary_part;
        return new Complex(re, im);
    }

    public static Complex conjugate(Complex c) {
        return new Complex(c.real_part, -c.imaginary_part);
    }

    public static double abs(Complex c) {
        return Math.sqrt(c.real_part * c.real_part + c.imaginary_part * c.imaginary_part);
    }

    public static double[] abs(Complex[] c) {
        int N = c.length;
        double[] mag = new double[N];
        for(int i=0; i<N; i++) {
            mag[i] = Math.sqrt(c[i].real_part * c[i].real_part + c[i].imaginary_part * c[i].imaginary_part);
        }
        return mag;
    }

    public static final Complex nthRootOfUnity(int n, int N) {
        double re = Math.cos(2 * Math.PI * n / N);
        double im = Math.sin(2 * Math.PI * n / N);
        return new Complex(re, im);
    }
}
