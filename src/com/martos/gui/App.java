package com.martos.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class App {
    public JPanel main_panel;
    private JButton generate_button;
    private JTextField disk_path;
    private JTextField cd_text;
    private JComboBox cpu_combo;
    private JComboBox disk_type;
    private JTextField nvram_text;
    private JLabel nvram_label;
    private JTextField data_disk_path;
    private JComboBox data_disk_type;
    private JLabel eth_mac_label;
    private JTextField eth_mac_text;

    private String[] cpu_modes = { "64", "128", "256", "512" };

    public App() {
        generate_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cd = cd_text.getText();
                String disk = disk_path.getText();
                String nvram = nvram_text.getText();

                try {
                    PrintWriter writer = new PrintWriter("vax.ini");

                    writer.println(";SIMH CONFIG GENERATED BY MARTOS FOR V4.0");
                    writer.println();
                    writer.println(";NVRAM");
                    writer.println("attach nvr " + nvram);
                    writer.println();
                    writer.println(";CPU");
                    writer.println("set cpu " + cpu_combo.getSelectedItem() + "m");
                    writer.println();
                    writer.println(";DISK");
                    writer.println("set rq0 " + disk_type.getSelectedItem());

                    if (!data_disk_path.getText().isEmpty()) {
                        writer.println("set rq1 " + data_disk_type.getSelectedItem());
                    }

                    writer.println("set rq3 cdrom");

                    if (!disk_path.getText().isEmpty()) writer.println("attach rq0 " + disk);
                    else {
                        JOptionPane.showMessageDialog(null, "Sys disk cannot be empty! Default option loaded.");
                        writer.println("attach rq0 VAXSYS.dsk");
                    }


                    if (!data_disk_path.getText().isEmpty()) {
                        writer.println("attach rq1 " + data_disk_path.getText());
                    }

                    writer.println("attach rq3 " + cd);
                    writer.println();
                    writer.println("set r1 disable");
                    writer.println("set ts disable");
                    writer.println();
                    writer.println(";ETHERNET (Default)");

                    if (!eth_mac_text.getText().isEmpty()) {
                        writer.println("attach xq mac=" + eth_mac_text.getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "Mac address cannot be empty! Default option loaded.");
                        writer.println("attach xq mac=08-00-2B-AA-BB-CC");
                    }

                    writer.println("attach xq eth0");
                    writer.println();
                    writer.println(";END OF CONFIG");
                    writer.println("boot cpu");
                    writer.println();
                    writer.println(";For ethernet, please use simh to find the right network card with : SHOW ETH");

                    writer.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }


            }
        });
    }
}
