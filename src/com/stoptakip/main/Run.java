package com.stoptakip.main;

import com.stoptakip.gui.classes.FLogin;
import com.stoptakip.gui.classes.FMain;

import javax.swing.*;

public class Run {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { new FLogin(); }
        });
    }
}
