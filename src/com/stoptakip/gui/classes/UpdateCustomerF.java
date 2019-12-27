package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CustomerMySQL;
import com.stoptakip.dto.models.Customer;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UpdateCustomerF extends JDialog implements IDialog {

    public UpdateCustomerF() { initFrame(); }

    Customer entity;

    @Override
    public void initFrame(){
        JPanel panel = initPanel();
        add(panel);
        setTitle("MÜŞTERİ DÜZENLE");
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(1,2));

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBorder(BorderFactory.createTitledBorder("Müşteri Arama"));
        JPanel westNorthPanel = new JPanel(new GridLayout(1,2));
        JLabel nameLabel = new JLabel("Müşteri");
        westNorthPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        westNorthPanel.add(nameField);
        JPanel westSouthPanel = new JPanel((new BorderLayout()));
        JList staffList = new JList();
        staffList.setListData(new CustomerMySQL().getAll().toArray());
        JScrollPane nameAreaScroll = new JScrollPane(staffList);
        westSouthPanel.add(nameAreaScroll);
        westPanel.add(westNorthPanel, BorderLayout.NORTH);
        westPanel.add(westSouthPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new GridLayout(5,2));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Müşteri Düzenle"));
        JLabel newNamelabel = new JLabel("İsim");
        eastPanel.add(newNamelabel);
        JTextField newNameField = new JTextField(10);
        newNameField.setEnabled(false);
        eastPanel.add(newNameField);
        JLabel newEMailLabel = new JLabel("E-Mail");
        eastPanel.add(newEMailLabel);
        JTextField newEMailField = new JTextField(10);
        newEMailField.setEnabled(false);
        eastPanel.add(newEMailField);
        JLabel newAddressLabel = new JLabel("Adres");
        eastPanel.add(newAddressLabel);
        JTextField newAddressField = new JTextField(10);
        newAddressField.setEnabled(false);
        eastPanel.add(newAddressField);
        JLabel newPhoneNumberLabel = new JLabel("Telefon");
        eastPanel.add(newPhoneNumberLabel);
        JTextField newPhoneNumberField = new JTextField(10);
        newPhoneNumberField.setEnabled(false);
        eastPanel.add(newPhoneNumberField);
        JButton updateButton = new JButton("DÜZENLE");
        updateButton.setEnabled(false);
        eastPanel.add(updateButton);
        JButton cancelButton = new JButton("İPTAL");
        eastPanel.add(cancelButton);

        panel.add(westPanel);
        panel.add(eastPanel);

        nameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {  }

            @Override
            public void keyPressed(KeyEvent e) {  }

            @Override
            public void keyReleased(KeyEvent e) {
                staffList.setListData(new CustomerMySQL().getAllByName(nameField.getText()).toArray());
            }
        });

        staffList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                entity = (Customer) staffList.getSelectedValue();
                if(!staffList.isSelectionEmpty()) {
                    newNameField.setText(entity.getName()); newNameField.setEnabled(true);
                    newEMailField.setText(entity.getE_mail());   newEMailField.setEnabled(true);
                    newAddressField.setText(entity.getAddress());   newAddressField.setEnabled(true);
                    newPhoneNumberField.setText(entity.getPhone());   newPhoneNumberField.setEnabled(true);
                    updateButton.setEnabled(true);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(newNameField.getText().isEmpty() || newEMailField.getText().isEmpty() || newAddressField.getText().isEmpty() || newPhoneNumberField.getText().isEmpty()
                            || newNameField.getText().isBlank() || newEMailField.getText().isBlank() || newAddressField.getText().isBlank() || newPhoneNumberField.getText().isBlank())
                        throw new Exception("Bütün Alanları Doldurun");

                    entity.setName(newNameField.getText());
                    entity.setE_mail(newEMailField.getText().trim());
                    entity.setAddress(newAddressField.getText());
                    entity.setPhone(newPhoneNumberField.getText().trim());

                    if(new CustomerMySQL().update(entity)){
                        JOptionPane.showMessageDialog(null, "Müşteri Güncellendi");

                        nameField.setText("");
                        staffList.clearSelection();
                        staffList.setListData(new CustomerMySQL().getAll().toArray());
                        newNameField.setText("");   newNameField.setEnabled(false);
                        newEMailField.setText("");  newEMailField.setEnabled(false);
                        newAddressField.setText("");    newAddressField.setEnabled(false);
                        newPhoneNumberField.setText("");    newPhoneNumberField.setEnabled(false);
                        updateButton.setEnabled(false);
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
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
