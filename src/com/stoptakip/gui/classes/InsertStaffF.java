package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.StaffMySQL;
import com.stoptakip.dto.models.Staff;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertStaffF extends JDialog implements IDialog {

    public InsertStaffF(){ initFrame(); }

    @Override
    public void initFrame() {
        JPanel panel = initPanel();

        add(panel);
        setTitle("Personel Ekle");
        pack();
        setLocationRelativeTo(null);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(5,2));
        panel.setBorder(BorderFactory.createTitledBorder("Personel Bilgileri "));

        //Personel bilgileri components
        JLabel nameLabel = new JLabel("İsim");
        panel.add(nameLabel);
        JTextField nameField = new JTextField(15);
        panel.add(nameField);
        JLabel eMailLabel = new JLabel("E-Mail");
        panel.add(eMailLabel);
        JTextField eMailField = new JTextField(20);
        panel.add(eMailField);
        JLabel addressLabel = new JLabel("Adres");
        panel.add(addressLabel);
        JTextField addressField = new JTextField(20);
        panel.add(addressField);
        JLabel phoneNumberLabel = new JLabel("Telefon");
        panel.add(phoneNumberLabel);
        JTextField phoneNumberField = new JTextField(11);
        panel.add(phoneNumberField);
        JButton insertButton = new JButton("EKLE");
        panel.add(insertButton);
        JButton cancelButton = new JButton("İPTAL");
        panel.add(cancelButton);

        //Personel ekleme işlemleri
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText().isEmpty() || eMailField.getText().isEmpty() || addressField.getText().isEmpty() || phoneNumberField.getText().isEmpty()
                        || nameField.getText().isBlank() || eMailField.getText().isBlank() || addressField.getText().isBlank() || phoneNumberField.getText().isBlank())
                    JOptionPane.showMessageDialog(null, "Lütfen Gerekli Alanları Doldurunuz");
                else{
                    Staff entity = new Staff();

                    entity.setName(nameField.getText());
                    entity.setE_mail(eMailField.getText().trim());
                    entity.setAddress(addressField.getText());
                    entity.setPhone(phoneNumberField.getText().trim());
                    entity.setActivated(true);

                    if(new StaffMySQL().insert(entity)) {
                        JOptionPane.showMessageDialog(null, entity.getName() + " " + " Eklendi");

                        nameField.setText("");
                        nameField.requestFocusInWindow();
                        eMailField.setText("");
                        addressField.setText("");
                        phoneNumberField.setText("");
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return panel;
    }
}
