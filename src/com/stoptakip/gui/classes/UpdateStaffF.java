package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.AccountMySQL;
import com.stoptakip.dao.daoconcrete.StaffMySQL;
import com.stoptakip.dto.models.Staff;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UpdateStaffF extends JDialog implements IDialog {

    public UpdateStaffF() { initFrame(); }

    Staff entity;

    @Override
    public void initFrame(){
        JPanel panel = initPanel();
        add(panel);
        setTitle("PERSONEL DÜZENLE");
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
        westPanel.setBorder(BorderFactory.createTitledBorder("Personel Arama"));
        JPanel westNorthPanel = new JPanel(new GridLayout(1,2));
        JLabel nameLabel = new JLabel("Personel");
        westNorthPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        westNorthPanel.add(nameField);
        JPanel westSouthPanel = new JPanel((new BorderLayout()));
        JList staffList = new JList();
        staffList.setListData(new StaffMySQL().getAll().toArray());
        JScrollPane nameAreaScroll = new JScrollPane(staffList);
        westSouthPanel.add(nameAreaScroll);
        westPanel.add(westNorthPanel, BorderLayout.NORTH);
        westPanel.add(westSouthPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new GridLayout(6,2));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Personel Düzenle"));
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
        JLabel isActiveLabel = new JLabel("Durum");
        eastPanel.add(isActiveLabel);
        JCheckBox isActiveCB = new JCheckBox("Aktif");
        isActiveCB.setEnabled(false);
        eastPanel.add(isActiveCB);
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
                staffList.setListData(new StaffMySQL().getAllByName(nameField.getText()).toArray());
            }
        });

        staffList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                entity = (Staff) staffList.getSelectedValue();
                if(!staffList.isSelectionEmpty()) {
                    newNameField.setText(entity.getName()); newNameField.setEnabled(true);
                    newEMailField.setText(entity.getE_mail());   newEMailField.setEnabled(true);
                    newAddressField.setText(entity.getAddress());   newAddressField.setEnabled(true);
                    newPhoneNumberField.setText(entity.getPhone());   newPhoneNumberField.setEnabled(true);
                    isActiveCB.setSelected(entity.getActivated());   isActiveCB.setEnabled(true);
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
                    entity.setActivated(isActiveCB.isSelected());

                    if(new StaffMySQL().update(entity)){
                        if(!entity.getActivated() && new AccountMySQL().getByStaffId(entity.getId() ) != null){
                            new AccountMySQL().delete(new AccountMySQL().getByStaffId(entity.getId()));
                            JOptionPane.showMessageDialog(null, "Personel Pasif Duruma Alındı ve Personele Ait Hesap Silindi");
                        }else
                            JOptionPane.showMessageDialog(null, "Personel Güncellendi");

                        nameField.setText("");
                        staffList.clearSelection();
                        staffList.setListData(new StaffMySQL().getAll().toArray());
                        newNameField.setText("");   newNameField.setEnabled(false);
                        newEMailField.setText("");  newEMailField.setEnabled(false);
                        newAddressField.setText("");    newAddressField.setEnabled(false);
                        newPhoneNumberField.setText("");    newPhoneNumberField.setEnabled(false);
                        isActiveCB.setSelected(false);      isActiveCB.setEnabled(false);
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
