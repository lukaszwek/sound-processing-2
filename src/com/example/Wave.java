package com.example;

/**
 * Created by lukaszwieczorek on 12.05.15.
 */
public class Wave {
    private String waveType; // { sinusoidal, triangular, sawtooth, rectangular, whiteNoise, redNoise }
    private double frequency;
    private double amplification;

    private double fCyclePosition=0;
    private double fCycleInc;

    public Wave() {}

    public Wave(String waveType, double frequency, double amplification) {
        this.waveType = waveType;
        this.frequency = frequency;
        this.amplification = amplification;

        this.fCycleInc = this.frequency/44100;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        this.fCycleInc = this.frequency/44100;
    }

    public String getWaveType() {
        return this.waveType;
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
