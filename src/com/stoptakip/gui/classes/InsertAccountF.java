package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.AccountMySQL;
import com.stoptakip.dao.daoconcrete.AuthorityMySQL;
import com.stoptakip.dao.daoconcrete.StaffMySQL;
import com.stoptakip.dto.models.Account;
import com.stoptakip.dto.models.Authority;
import com.stoptakip.dto.models.Staff;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertAccountF extends JDialog implements IDialog {

    public InsertAccountF() { initFrame(); }

    @Override
    public void initFrame() {
        JPanel panel = initPanel();

        add(panel);
        setTitle("Hesap Oluştur");
        pack();
        setLocationRelativeTo(null);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(5,2));
        panel.setBorder(BorderFactory.createTitledBorder("Hesap Bilgileri "));

        //Hesap bilgileri components
        JLabel staffLabel = new JLabel("Personel");
        panel.add((staffLabel));
        JComboBox staffBox = new JComboBox(new StaffMySQL().getAllActive().toArray());
        staffBox.insertItemAt("---Personel Seç---",0);
        staffBox.setSelectedIndex(0);
        panel.add(staffBox);
        JLabel authorityLabel = new JLabel("Yetki");
        panel.add(authorityLabel);
        JComboBox authorityBox = new JComboBox(new AuthorityMySQL().getAll().toArray());
        authorityBox.insertItemAt("---Yetki Seç---", 0);
        authorityBox.setSelectedIndex(0);
        panel.add(authorityBox);
        JLabel passwordLabel = new JLabel("Şifre");
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(10);
        panel.add(passwordField);
        JLabel passwordAgainLabel = new JLabel("Şifre Tekrar");
        panel.add(passwordAgainLabel);
        JPasswordField passwordAgainField = new JPasswordField(10);
        panel.add(passwordAgainField);
        JButton insertButton = new JButton("EKLE");
        panel.add(insertButton);
        JButton cancelButton = new JButton("İPTAL");
        panel.add(cancelButton);

        //Hesap ekleme işlemleri
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(staffBox.getSelectedIndex() != 0 && authorityBox.getSelectedIndex() != 0 && !passwordField.getText().isEmpty()
                            && !passwordAgainField.getText().isEmpty() && passwordField.getText().equals(passwordAgainField.getText())){
                        Account entity = new Account();
                        entity.setAuthority_id(((Authority)authorityBox.getSelectedItem()).getId());
                        entity.setStaff_id(((Staff)staffBox.getSelectedItem()).getId());
                        entity.setPassword(passwordField.getText());
                        if(new AccountMySQL().insert(entity)) {
                            JOptionPane.showMessageDialog(null, "Hesap Oluşturuldu");
                            staffBox.setSelectedIndex(0);
                            authorityBox.setSelectedIndex(0);
                            passwordField.setText("");
                            passwordAgainField.setText("");
                        }
                        }else{
                        if(staffBox.getSelectedIndex() == 0 || authorityBox.getSelectedIndex() == 0 || passwordField.getText().isEmpty()
                                && passwordAgainField.getText().isEmpty())
                            throw new Exception("Lütfen Bütün Alanları Doldurunuz");
                        if(!passwordField.getText().equals(passwordAgainField.getText()))
                            throw new Exception("Parolalar uyuşmuyor");
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
