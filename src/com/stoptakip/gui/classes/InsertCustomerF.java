package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CustomerMySQL;
import com.stoptakip.dto.models.Customer;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertCustomerF extends JDialog implements IDialog {

    public InsertCustomerF() { initFrame(); }

    @Override
    public void initFrame() {
        JPanel panel = initPanel();

        add(panel);
        setTitle("Müşteri Ekle");
        pack();
        setLocationRelativeTo(null);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel fieldPanel = new JPanel(new GridLayout(3,2));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        fieldPanel.setBorder(BorderFactory.createTitledBorder("Müşteri Bilgileri "));

        //Müşteri bilgileri components
        JLabel nameLabel = new JLabel("İsim");
        fieldPanel.add(nameLabel);
        JTextField nameField = new JTextField(15);
        fieldPanel.add(nameField);
        JLabel eMailLabel = new JLabel("E-Mail");
        fieldPanel.add(eMailLabel);
        JTextField eMailField = new JTextField(15);
        fieldPanel.add(eMailField);
        JLabel phoneNumberLabel = new JLabel("Telefon");
        fieldPanel.add(phoneNumberLabel);
        JTextField phoneNumberField = new JTextField(15);
        fieldPanel.add(phoneNumberField);
        JTextArea addressArea = new JTextArea(4,1);
        JScrollPane pane = new JScrollPane(addressArea);
        pane.setBorder(BorderFactory.createTitledBorder("Adres"));
        JButton insertButton = new JButton("EKLE");
        buttonPanel.add(insertButton);
        JButton cancelButton = new JButton("İPTAL");
        buttonPanel.add(cancelButton);

        panel.add(fieldPanel, BorderLayout.NORTH);
        panel.add(pane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        //Müşteri ekleme işlemleri
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText().isEmpty() || eMailField.getText().isEmpty() || addressArea.getText().isEmpty() || phoneNumberField.getText().isEmpty()
                        || nameField.getText().isBlank() || eMailField.getText().isBlank() || addressArea.getText().isBlank() || phoneNumberField.getText().isBlank())
                    JOptionPane.showMessageDialog(null, "Lütfen Gerekli Alanları Doldurunuz");
                else {
                    Customer entity = new Customer();

                    entity.setName(nameField.getText());
                    entity.setE_mail(eMailField.getText().trim());
                    entity.setPhone(phoneNumberField.getText().trim());
                    entity.setAddress(addressArea.getText());

                    if (new CustomerMySQL().insert(entity)){
                        JOptionPane.showMessageDialog(null, entity.getName() + " " + " Eklendi");

                        nameField.setText("");
                        eMailField.setText("");
                        phoneNumberField.setText("");
                        addressArea.setText("");

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
