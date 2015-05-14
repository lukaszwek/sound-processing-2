package com.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    public static double resQ;
    public static double cutOffFreq;
    public static LowPassFilter filter;
    public static String wavetype = "Sinusoidal";
    public static ArrayList<Wave> wavesList = new ArrayList<Wave>();
    public static DefaultListModel model = new DefaultListModel();
    public static JList jlistWaves = new JList(model);
    private static AudioThread m_thread;

    public static void main(String[] args) {
        m_thread = new AudioThread(wavesList);
        m_thread.BUFFER_DURATION = 0.100;
        m_thread.lpfilter = filter;
        m_thread.start();

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
        f.setSize(250, 530);

        jpanelWindow.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelFreqAmpli.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelFilter.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpanelRemove.setLayout(new FlowLayout(FlowLayout.CENTER));

        jpanelWindow.setPreferredSize(new Dimension(230, 150));
        jpanelFreqAmpli.setPreferredSize(new Dimension(150, 190));
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
                String print = "Frequency: " + String.valueOf(frequency) + ", wavetype: " + wavetype + ", amplification: " + String.valueOf(amplification);
                System.out.println(print);
                Wave newWave = new Wave(wavetype, frequency, amplification);
                wavesList.add(newWave); //add wave to array jlistWaves
                model.add(wavesList.size() - 1, newWave.getWaveType() + " " + newWave.getFrequency() + " / " + newWave.getAmplification()); //add description of wave to jlist
            }
        });

        JButton jbuttonRemove = new JButton("Remove");   // new instance of button to
        jbuttonRemove.addActionListener(new ActionListener() {  // actionlistener for
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jlistWaves.getSelectedIndex();
                if (selectedIndex != -1) {
                    wavesList.remove(selectedIndex);        // remove wave from arraylist
                    model.removeElementAt(selectedIndex);   // remove wave from jlist
                }
            }
        });

        final JButton jbuttonTurnOnFilter = new JButton("On");
        jbuttonTurnOnFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (m_thread.lpfilter == null) {
                    m_thread.lpfilter = new LowPassFilter();
                    m_thread.lpfilter.setResonanceQ(resQ);
                    m_thread.lpfilter.setCutOff(cutOffFreq);
                    m_thread.lpfilter.setSamplingFreq(44100);
                    jbuttonTurnOnFilter.setText("Off");
                } else {
                    m_thread.lpfilter = null;
                    jbuttonTurnOnFilter.setText("On");
                }
            }
        });

        jpanelWindow.add(scrollPane);
        jpanelWindow.add(jpanelRemove);

        jpanelRemove.add(jbuttonRemove);

        jpanelFreqAmpli.add(jlabelFrequency);
        jpanelFreqAmpli.add(jtextfieldFrequency);
        jpanelFreqAmpli.add(jlabelAmplification);
        jpanelFreqAmpli.add(jtextfieldAmplification);
        jpanelFreqAmpli.add(jlabelWaveTypes);
        jpanelFreqAmpli.add(jcomboboxWaveTypes);
        jpanelFreqAmpli.add(jbuttonGenerate);

        jpanelFilter.add(jlabelResonance);
        jpanelFilter.add(jtextfieldResonance);
        jpanelFilter.add(jlabelCutOff);
        jpanelFilter.add(jtextfieldCutOff);
        jpanelFilter.add(jbuttonTurnOnFilter);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true); // show the main frame and start the application
    }
}
