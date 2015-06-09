package com.example;

/**
 * Created by lukaszwieczorek on 12.05.15.
 */
public class Wave {
    private String waveType; // { sinusoidal, triangular, sawtooth, rectangular, whiteNoise, redNoise }
    private double frequency;
    private double baseFreq;
    private double amplification;
    private double percentageFreq=100;

    private double fCyclePosition=0;
    private double fCycleInc;

    public Wave() {}

    public Wave(String waveType, double frequency, double amplification, double percentageFreq) {
        this.waveType = waveType;
        this.percentageFreq = percentageFreq;
        this.baseFreq = frequency;
        this.frequency = baseFreq*(percentageFreq/100);
        this.amplification = amplification;

        this.fCycleInc = this.frequency/44100;
//        System.out.println(this.baseFreq + " " + this.frequency + " " + this.fCycleInc);
    }

    public double getFrequency() {
        return frequency;
    }

    public double getBaseFreq() {
        return baseFreq;
    }

    public void setFrequency(double frequency) {
        this.baseFreq = frequency;
        this.frequency = baseFreq*(percentageFreq/100);
        this.fCycleInc = this.frequency/44100;
//        System.out.println(this.baseFreq + " " + this.frequency + " " + this.fCycleInc);
    }

    public String getWaveType() {
        return this.waveType;
    }

    public double getPercentageFreq() {
        return this.percentageFreq;
    }

    public void setWaveType(String waveType) {
        this.waveType = waveType;
    }

    public double getAmplification() {
        return amplification;
    }

    public void setAmplification(double amplification) {
        this.amplification = amplification;
    }

    public double getfCycleInc() {
        return fCycleInc;
    }

    //function for getting current point of the wave
    public double getResult(){
        this.fCyclePosition+=fCycleInc;
        if (this.fCyclePosition > 1)
            this.fCyclePosition -= 1;
        return Generator.generateWavSound(this.fCyclePosition, this.waveType);
    }


}
