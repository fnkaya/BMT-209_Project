package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.AccountMySQL;
import com.stoptakip.dao.daoconcrete.StaffMySQL;
import com.stoptakip.dto.models.Staff;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FLogin extends JDialog implements IDialog {

    public FLogin() { initFrame(); }

    //Giriş yapan kullancı yetkisini aktarmak için
    static Staff userEntity;

    @Override
    public void initFrame() {
        JPanel panel = initPanel();

        add(panel);
        setTitle("STOK TAKİP ");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/stock.png"));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //Login ekranı tasarımı
        JPanel northPanel = new JPanel(new BorderLayout());
        panel.add(northPanel, BorderLayout.NORTH);
        JPanel southPanel = new JPanel(new FlowLayout());
        panel.add(southPanel, BorderLayout.SOUTH);
        JPanel northWestPanel = new JPanel(new BorderLayout());
        northPanel.add(northWestPanel, BorderLayout.WEST);
        JPanel northEastPanel = new JPanel(new GridLayout(2,2));
        northPanel.add(northEastPanel, BorderLayout.EAST);

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon("icons/lock.png"));
        northWestPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel eMailLabel = new JLabel("E-Mail", JLabel.CENTER);
        northEastPanel.add(eMailLabel);
        JTextField eMailField = new JTextField(15);
        northEastPanel.add(eMailField);
        JLabel passwordLabel = new JLabel("Şifre", JLabel.CENTER);
        northEastPanel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        northEastPanel.add(passwordField);

        JButton loginButton = new JButton("GİRİŞ");
        loginButton.setIcon(new ImageIcon("icons/enter.png"));
        southPanel.add(loginButton);
        JButton exitButton = new JButton("ÇIKIŞ");
        exitButton.setIcon(new ImageIcon("icons/exit.png"));
        southPanel.add(exitButton);

        //Giriş işlemleri
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userEntity = new StaffMySQL().getByEMail(eMailField.getText()); //Girilen mail adresinin ait olduğu personeli getirir
                if(userEntity != null && new AccountMySQL().getByStaffId(userEntity.getId()) != null){
                    if(new AccountMySQL().getByStaffId(userEntity.getId()).getPassword().equals(passwordField.getText())){
                        new FMain();
                        dispose();
                    }else
                        JOptionPane.showMessageDialog(null, "Hatalı Kullanıcı Adı veya Şifre");
                }else
                    JOptionPane.showMessageDialog(null, "Hesap Bulunamadı");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return panel;
    }
}
