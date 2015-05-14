package com.example;

public class LowPassFilter {
    private static double[][] output;
    private double samplingFrequency;
    private double resonance;
    private double cutOffFrequency;
    public double max;

    public double[][] LPFprocess(double[][] buffer) {
        double y1, y2, x, x1, x2;
        double a0, a1, a2, b1, b2;
        double s, c, alpha, r;
        output = new double[buffer.length][2];

        s = Math.sin(2*Math.PI*cutOffFrequency/samplingFrequency);
        c = Math.cos(2*Math.PI*cutOffFrequency/samplingFrequency);
        alpha = (s/(2*resonance));
        r = (1/(1+alpha));

        a0 = (0.5*(1-c)*r);
        a1 = ((1-c)*r);
        a2 = a0;
        b1 = (-2*c*r);
        b2 = ((1-alpha)*r);

        output[0][0]=0;
        output[0][1]=buffer[0][1];
        output[1][0]=1;
        output[1][1]=buffer[1][1];

        double max1=0;
        for (int i=2; i<buffer.length; i++) {
            x = buffer[i][1];
            x1 = buffer[i-1][1];
            x2 = buffer[i-2][1];
            y1 = output[i-1][1];
            y2 = output[i-2][1];

            output[i][0] = i;
            output[i][1] = (a0*x + a1*x1 + a2*x2 - b1*y1 - b2*y2);
            if (output[i][1]>max1) {
                max1 = output[i][1];
            }
        }

        this.max = max1;
        return output;
    }

    public void setSamplingFreq(double samplingFreq) {
        this.samplingFrequency = samplingFreq;
    }

    public void setResonanceQ(double resonanceFrequency) {
        this.resonance = resonanceFrequency;
    }

    public void setCutOff(double cutOff) {
        this.cutOffFrequency = cutOff;
    }
}
