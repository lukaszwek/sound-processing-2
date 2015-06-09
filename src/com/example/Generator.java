package com.example;

import java.util.Random;

public class Generator {
    public static int counter;
    public static double value;
    public static double value_next;
    public static double value_ret;
    public static double step = 10;
    
    public static double lfo_fCyclePosition = 1;

    public static double generateWavSound(double fCyclePosition, String wavetype) {

        if (wavetype.equals("Sinusoidal")){
            return Math.sin(2.0 * Math.PI * fCyclePosition);
        }
        else if (wavetype.equals("Triangular")){
            if (Math.ceil(fCyclePosition*2)%2 == 1){
                return fCyclePosition*2;
            }
            else{
                return (2 - fCyclePosition*2);
            }
        }
        else if (wavetype.equals("Sawtooth")){
            if (Math.ceil(fCyclePosition)%2 == 1){
                return fCyclePosition;
            }
            else{
                return fCyclePosition;
            }
        }
        else if (wavetype.equals("Rectangular")){
            if (Math.ceil(fCyclePosition*2)%2 == 1){
                return 1;
            }
            else{
                return -1;
            }
        }
        else if (wavetype.equals("White Noise")){
            Random r = new Random();
            double rangeMin = -1.00;
            double rangeMax = 1.00;
            return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        }
        else if (wavetype.equals("Red Noise"))
        {
            if (counter == step || counter == 0){
                if (counter == 0){
                    Random r = new Random();
                    double rangeMin = -1.00;
                    double rangeMax = 1.00;
                    value      = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                    value_next = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                    counter++;
                    return value;
                }
                else if (counter == step){
                    counter = 0;
                    return value_next;
                }
            }
            else{
                if (value_next >= value)
                    value_ret = value+counter*Math.abs((value_next-value))/step;
                else
                    value_ret = value-counter*Math.abs((value-value_next))/step;
            }
            counter++;
            return value_ret;
        }
        else if (wavetype.equals("LFO"))
        {
            lfo_fCyclePosition += 0.005;
            if (lfo_fCyclePosition > 1)
                lfo_fCyclePosition -= 1;

            return Math.sin(2.0 * Math.PI * lfo_fCyclePosition);
        }
        else
            return 0;
    }
}
