package com;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

public class Frame extends JFrame implements ActionListener {
    JButton setAlarmButton;
    JLabel Ltime, LHours, LNotifier;
    JTextField TFHours, TFMinutes;
    int  setHours, setMinutes;
    Timer timer,dateSet;
    public static Clip beep;
    File BEEP = new File("beep.wav");

    void Frame() {
        setSize(400, 250);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        Labels();
        Buttons();
        TextFields();
        new DateSetup();

        add(Ltime);
        add(LHours);
        add(LNotifier);
        add(TFHours);
        add(TFMinutes);
        add(setAlarmButton);

        repaint();
    }
    public void dateForLtime() {
        Ltime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    void Labels() {
        LNotifier = new JLabel("Brak ustawienia.");
        LNotifier.setBounds(150, 75, 200, 25);
        LNotifier.setVisible(true);


        Ltime = new JLabel();
        Ltime.setBounds(140, 25, 300, 50);
        Ltime.setVisible(true);

        LHours = new JLabel(":");
        LHours.setBounds(193, 100, 6, 25);
        LHours.setVisible(true);

    }

    static void Sound(File Sound) {

        try {
            beep = AudioSystem.getClip();
            beep.open(AudioSystem.getAudioInputStream(Sound));
            beep.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    void TextFields() {
        TFHours = new JTextField("00");
        TFHours.setBounds(160, 100, 30, 25);
        TFHours.setVisible(true);

        TFMinutes = new JTextField("00");
        TFMinutes.setBounds(200, 100, 30, 25);
        TFMinutes.setVisible(true);

    }

    void Buttons() {
        setAlarmButton = new JButton("Ustaw budzik");
        setAlarmButton.addActionListener(this);
        setAlarmButton.setBounds(125, 150, 150, 30);
    }

    public class TimerSetup {
        public TimerSetup(int seconds) {
            timer = new Timer();
            timer.schedule(new SetupTask(), seconds);
        }

        class SetupTask extends TimerTask {
            public void run() {
                LNotifier.setBounds(100, 75, 200, 25);
                LNotifier.setText("Aby mnie wylaczyc, zamknij program!");
                Sound(BEEP);
                timer.cancel();
            }
        }
    }
    public class DateSetup {
        public DateSetup() {
            dateSet = new Timer();
            dateSet.scheduleAtFixedRate(new SetupTask(), 500,500);
        }

        class SetupTask extends TimerTask {
            public void run() {
                dateForLtime();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object bSource = e.getSource();
        if (bSource == setAlarmButton) {
            if(setHours>23 && setMinutes<60){
                LNotifier.setText("Takiej godziny nie ma.");
            }
            else if(setMinutes>59 && setHours<24) {
                LNotifier.setText("Minuty poza skal�.");
            }
            else if(setHours>23 && setMinutes>59){
                LNotifier.setText("Chyba z�a planeta...");
            }
            else if(setHours<24 && setMinutes<60){
                LNotifier.setText("Budzik ustawiony!");

                setHours = Integer.parseInt(TFHours.getText());
                setMinutes = Integer.parseInt(TFMinutes.getText());

                int hours = new Date(System.currentTimeMillis()).getHours();
                int minutes = new Date(System.currentTimeMillis()).getMinutes();
                int seconds = new Date(System.currentTimeMillis()).getSeconds();
                long TimerSet = ((setHours - hours) * 1000 * 60 * 60) + ((setMinutes - (minutes + 1)) * 1000 * 60) + (60 - seconds) * 1000;
                new TimerSetup((int) TimerSet);
            }

        }
    }
}