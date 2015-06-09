package com.example;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by lukaszwieczorek on 12.05.15.
 */
public class AudioThread extends Thread {
    final static public int SAMPLING_RATE = 44100;
    final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
    static public double BUFFER_DURATION = 0.08;      //About a 100ms buffer
    final static public int PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);

    public static ArrayList<Wave> waves;

    SourceDataLine line;
    public double totalAmplification;
    public boolean bExitThread = false;
    public LowPassFilter lpfilter;
    public static int ocean;


    public AudioThread(ArrayList<Wave> waves){
        this.waves = waves; //get arraylist with all the waves
    }

    //Get the number of queued samples in the SourceDataLine buffer
    private int getLineSampleCount() {
        return line.getBufferSize() - line.available();
    }

    //Continually fill the audio output buffer whenever it starts to get empty, SINE_PACKET_SIZE/2
    //samples at a time, until we tell the thread to exit
    public void run() {
        //Open up the audio output, using a sampling rate of 44100hz, 16 bit samples, mono, and big
        // endian byte ordering.   Ask for a buffer size of at least 2*SINE_PACKET_SIZE
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, PACKET_SIZE*2);
            if (!AudioSystem.isLineSupported(info))
                throw new LineUnavailableException();

            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(format);
            line.start();
        }
        catch (LineUnavailableException e) {
            System.out.println("Line of that type is not available");
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Requested line buffer size = " + PACKET_SIZE*2);
        System.out.println("Actual line buffer size = " + line.getBufferSize());

        ByteBuffer cBuf = ByteBuffer.allocate(PACKET_SIZE);

        while (bExitThread==false) {
            cBuf.clear();                             //Toss out samples from previous pass

            //Update total amplification
            totalAmplification=0;
            for (int wavesNo=0;wavesNo<waves.size();wavesNo++)
            {
                totalAmplification+=waves.get(wavesNo).getAmplification();
            }

            //Generate PACKET_SIZE samples based on the current fCycleInc from fFreq
            Wave wav;
            double result=0;
            double finalResult=0;
            double[][]visualize= new double[PACKET_SIZE/SAMPLE_SIZE][2];
            double[][]visualize2= new double[PACKET_SIZE/SAMPLE_SIZE][2];

            for (int i=0; i < PACKET_SIZE/SAMPLE_SIZE; i++) {
                result=0;
                finalResult=0;
                for (int n=0; n<waves.size();n++){ //read the value at certain point of each wave to be mixed
                    wav=waves.get(n);		//get the n-th wave
//                    result=(wav.getResult()*waves.get(n).getAmplification())/totalAmplification;//get the result and include amplification
                    result=(wav.getResult()*waves.get(n).getAmplification())/totalAmplification;//get the result and include amplification
                    finalResult+=result;		//summing the value at desired point
                }
                visualize[i][0]=i;				//for visualizing the mixed plots
                visualize[i][1]=(finalResult);	// for visualizing mixed plots
                //cBuf.putShort((short)(Short.MAX_VALUE*finalResult)); //pushing data after mixing to a buffer
            }

            if (ocean==1)
            {
                lpfilter=new LowPassFilter();
                lpfilter.setResonanceQ(3);
                double temp = 300 + 200*Generator.generateWavSound(1,"LFO");
                if (temp<0)
                    temp = 0;
//                System.out.println("Cutoff frequency: " + temp);
                lpfilter.setCutOff(temp);
                lpfilter.setSamplingFreq(44100);
                visualize=lpfilter.LPFprocess(visualize);
                for (int cos=0; cos<visualize.length; cos++){
                    cBuf.putShort((short)(Short.MAX_VALUE*visualize[cos][1]/lpfilter.max));
                }
            }
            else if (lpfilter!=null){
                visualize2=lpfilter.LPFprocess(visualize);

                for (int cos=0; cos<visualize2.length; cos++){
                    cBuf.putShort((short)(Short.MAX_VALUE*visualize2[cos][1]/lpfilter.max));
                }
            }
            else
            {
                for (int cos=0; cos<visualize.length; cos++){
                    cBuf.putShort((short)(Short.MAX_VALUE*visualize[cos][1]));
                }
            }

            //Write sine samples to the line buffer
            line.write(cBuf.array(), 0, cBuf.position());

            //Wait here until there are less than SINE_PACKET_SIZE samples in the buffer
            //(Buffer size is 2*SINE_PACKET_SIZE at least, so there will be room for
            // at least SINE_PACKET_SIZE samples when this is true)
            try {
                while (getLineSampleCount() > PACKET_SIZE)
                    Thread.sleep(1);                          // Give UI a chance to run
            }
            catch (InterruptedException e) {                // We don't care about this
            }
        }
        line.drain();
        line.close();
    }

    public void exit() {
        bExitThread=true;
    }
}
