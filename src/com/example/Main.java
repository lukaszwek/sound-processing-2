package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Main {

    public static String[] wavetypes = {"Sinusoidal", "Triangular", "Sawtooth", "Rectangular", "White Noise", "Red Noise"};

    public static double frequency;
    public static double amplification;
    public static double percentageFreq;
    public static double resQ;
    public static double cutOffFreq;
    public static LowPassFilter filter;
    public static String wavetype = "Sinusoidal";
    public static ArrayList<Wave> wavesList = new ArrayList<Wave>();
    public static DefaultListModel model = new DefaultListModel();
    public static JList jlistWaves = new JList(model);
    private static AudioThread tones_thread = null;
    private static AudioThread ocean_thread = null;
    private static Wave oceanWave;

    public static void main(String[] args) {
        final JFrame f = new JFrame();
        f.setTitle("Generator");
        f.setLayout(new FlowLayout(FlowLayout.CENTER));
        f.setResizable(false);

        final JPanel jpanelWindow = new JPanel();
        final JPanel jpanelFreqAmpli = new JPanel();
        final JPanel jpanelFilter = new JPanel();
        JPanel jpanelRemove = new JPanel();

        f.add(jpanelFreqAmpli);
        f.add(jpanelFilter);
        f.add(jpanelWindow);
        f.setSize(250, 655);

        jpanelWindow.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelFreqAmpli.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelFilter.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelRemove.setLayout(new FlowLayout(FlowLayout.CENTER));

        jpanelWindow.setPreferredSize(new Dimension(230, 150));
        jpanelFreqAmpli.setPreferredSize(new Dimension(150, 320));
        jpanelFilter.setPreferredSize(new Dimension(150, 150));

        jlistWaves.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jlistWaves.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jlistWaves.setVisibleRowCount(-1);

        JScrollPane scrollPane = new JScrollPane(jlistWaves);
        scrollPane.setPreferredSize(new Dimension(230, 100));

        final JLabel jlabelFrequency = new JLabel("Frequency");
        final JTextField jtextfieldFrequency = new JTextField(10);
        jtextfieldFrequency.setHorizontalAlignment(JTextField.LEFT);
        jtextfieldFrequency.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    frequency = Double.parseDouble(jtextfieldFrequency.getText());
                } catch (Exception z) {
                    JOptionPane.showMessageDialog(f, "Error! Type numbers.");
                }
            }
        });

        final JLabel jlabelAmplification = new JLabel("Amplification");
        final JTextField jtextfieldAmplification = new JTextField(10);
        jtextfieldAmplification.setText("1");
        amplification=1;
        jtextfieldAmplification.setHorizontalAlignment(JTextField.LEFT);
        jtextfieldAmplification.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    amplification = Double.parseDouble(jtextfieldAmplification.getText());
                } catch (Exception z) {
                    JOptionPane.showMessageDialog(f, "Error! Type numbers.");
                }
            }
        });

        final JLabel jlabelWaveTypes = new JLabel("Wave type");
        final JComboBox jcomboboxWaveTypes = new JComboBox(wavetypes);
        jcomboboxWaveTypes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                wavetype = wavetypes[jcomboboxWaveTypes.getSelectedIndex()];
            }
        });

        final JLabel jlabelResonance = new JLabel("Resonance");
        final JTextField jtextfieldResonance = new JTextField(10);
        jtextfieldResonance.setHorizontalAlignment(JTextField.LEFT);

        jtextfieldResonance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    resQ = Double.parseDouble(jtextfieldResonance.getText());
                } catch (Exception z) {
                    JOptionPane.showMessageDialog(f, "Error! Type numbers.");
                }
            }
        });

        final JLabel jlabelPercentage = new JLabel("% frequency");
        final JTextField jtextfieldPercentage = new JTextField(10);
        jtextfieldPercentage.setText("100");
        percentageFreq = 100;
        jtextfieldPercentage.setHorizontalAlignment(JTextField.LEFT);

        jtextfieldPercentage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    percentageFreq = Double.parseDouble(jtextfieldPercentage.getText());
                } catch (Exception z) {
                    JOptionPane.showMessageDialog(f, "Error! Type numbers.");
                }
            }
        });

        final JLabel jlabelCutOff = new JLabel("Cut off frequency");
        final JTextField jtextfieldCutOff = new JTextField(10);
        jtextfieldCutOff.setHorizontalAlignment(JTextField.LEFT);

        jtextfieldCutOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    cutOffFreq = Double.parseDouble(jtextfieldCutOff.getText());
                } catch (Exception z) {
                    JOptionPane.showMessageDialog(f, "Error! Type numbers.");
                }
            }
        });

        JButton jbuttonGenerate = new JButton("Generate");
        jbuttonGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                percentageFreq = Double.parseDouble(jtextfieldPercentage.getText());
                String print = "Frequency: " + String.valueOf(frequency) + ", wavetype: " + wavetype + ", amplification: " + String.valueOf(amplification);
                System.out.println(print);
                Wave newWave = new Wave(wavetype, frequency, amplification, percentageFreq);
                wavesList.add(newWave); //add wave to array jlistWaves
                model.add(wavesList.size() - 1, newWave.getWaveType() + " " + newWave.getBaseFreq() + "*" + newWave.getPercentageFreq() + "% / " + newWave.getAmplification()); //add description of wave to jlist
            }
        });

        JButton jbuttonUpdate = new JButton("UPDATE");
        jbuttonUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i=0; i<wavesList.size(); i++) {
                    wavesList.get(i).setFrequency(frequency);
                    model.set(i, wavesList.get(i).getWaveType() + " " + wavesList.get(i).getBaseFreq() + "*" + wavesList.get(i).getPercentageFreq() + "% / " + wavesList.get(i).getAmplification()); //add description of wave to jlist
                }
            }
        });

        final JButton jbuttonTurnOnFilter = new JButton("On");
        jbuttonTurnOnFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tones_thread.lpfilter == null) {
                    tones_thread.lpfilter = new LowPassFilter();
                    tones_thread.lpfilter.setResonanceQ(resQ);
                    tones_thread.lpfilter.setCutOff(cutOffFreq);
                    tones_thread.lpfilter.setSamplingFreq(44100);
                    jbuttonTurnOnFilter.setText("Off");
                } else {
                    tones_thread.lpfilter = null;
                    jbuttonTurnOnFilter.setText("On");
                }
            }
        });

        JButton jbuttonRemove = new JButton("Remove");
        jbuttonRemove.addActionListener(new ActionListener() {  // actionlistener for
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jlistWaves.getSelectedIndex();
                if (selectedIndex != -1) {
                    wavesList.remove(selectedIndex);        // remove wave from arraylist
                    model.removeElementAt(selectedIndex);   // remove wave from jlist
                }
            }
        });

        final JButton jbuttonPlay = new JButton("PLAY");
        jbuttonPlay.addActionListener(new ActionListener() {  // actionlistener for
            public void actionPerformed(ActionEvent e) {
                if (tones_thread == null) {
                    tones_thread = new AudioThread(wavesList);
                    tones_thread.BUFFER_DURATION = 0.100;
                    tones_thread.lpfilter = filter;
                    tones_thread.start();
                    jbuttonPlay.setText("STOP");
                } else {
                    tones_thread.exit();
                    tones_thread = null;
                    jbuttonPlay.setText("PLAY");
                }
            }
        });

        final JButton jbuttonOcean = new JButton("Generate ocean");
        jbuttonOcean.addActionListener(new ActionListener() {  // actionlistener for
            public void actionPerformed(ActionEvent e) {
                if (ocean_thread == null) {
                    Wave oceanWave = new Wave(); //create new wave
                    oceanWave.setFrequency(400); //set its frequency
                    oceanWave.setWaveType("White Noise"); //set its type
                    oceanWave.setAmplification(1);
                    wavesList.add(oceanWave); //add wave to array list
                    model.add(wavesList.size() - 1, "Ocean"); //add description of wave to jlist

                    ocean_thread = new AudioThread(wavesList);
                    ocean_thread.BUFFER_DURATION = 0.08;
                    ocean_thread.ocean = 1;
                    ocean_thread.start();
                    jbuttonOcean.setText("STOP");
                } else {
                    int index=wavesList.size()-1;
                    wavesList.remove(index);
                    model.removeElementAt(index);
                    ocean_thread.ocean=0;
                    ocean_thread.exit();
                    ocean_thread = null;
                    jbuttonOcean.setText("Generate ocean");
                }
            }
        });

        jpanelWindow.add(scrollPane);
        jpanelWindow.add(jpanelRemove);

        jpanelRemove.add(jbuttonRemove);
        jpanelRemove.add(jbuttonPlay);

        jpanelFreqAmpli.add(jlabelFrequency);
        jpanelFreqAmpli.add(jtextfieldFrequency);
        jpanelFreqAmpli.add(jlabelAmplification);
        jpanelFreqAmpli.add(jtextfieldAmplification);
        jpanelFreqAmpli.add(jbuttonOcean);
        jpanelFreqAmpli.add(jlabelPercentage);
        jpanelFreqAmpli.add(jtextfieldPercentage);
        jpanelFreqAmpli.add(jlabelWaveTypes);
        jpanelFreqAmpli.add(jcomboboxWaveTypes);
        jpanelFreqAmpli.add(jbuttonGenerate);
        jpanelFreqAmpli.add(jbuttonUpdate);
        jpanelFreqAmpli.add(jbuttonOcean);

        jpanelFilter.add(jlabelResonance);
        jpanelFilter.add(jtextfieldResonance);
        jpanelFilter.add(jlabelCutOff);
        jpanelFilter.add(jtextfieldCutOff);
        jpanelFilter.add(jbuttonTurnOnFilter);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true); // show the main frame and start the application
    }
}
