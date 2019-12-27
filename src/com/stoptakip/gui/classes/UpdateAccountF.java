package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.AccountMySQL;
import com.stoptakip.dao.daoconcrete.AuthorityMySQL;
import com.stoptakip.dao.daoconcrete.StaffMySQL;
import com.stoptakip.dto.complexmodels.ComplexAccount;
import com.stoptakip.dto.models.Account;
import com.stoptakip.dto.models.Authority;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UpdateAccountF extends JDialog implements IDialog {

    public UpdateAccountF() { initFrame(); }

    ComplexAccount complexAccountEntity;

    @Override
    public void initFrame(){
        JPanel panel = initPanel();
        add(panel);
        setTitle("HESAP DÜZENLE");
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(5,2));

        JLabel nameLabel = new JLabel("Personel");
        panel.add(nameLabel);
        JComboBox accountBox = new JComboBox(new AccountMySQL().getAllComplex().toArray());
        accountBox.insertItemAt("---Hesap Seçiniz---",0);
        accountBox.setSelectedIndex(0);
        panel.add(accountBox);
        JLabel authorityNameLabel = new JLabel("Yetki");
        panel.add(authorityNameLabel);
        JComboBox authorityBox = new JComboBox(new AuthorityMySQL().getAll().toArray());
        authorityBox.insertItemAt("---Yetki Seçiniz---",0);
        authorityBox.setSelectedIndex(0);
        panel.add(authorityBox);
        JLabel passwordLabel = new JLabel("Şifre");
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);
        JLabel passwordAgainLabel = new JLabel("Şifre (Tekrar)");
        panel.add(passwordAgainLabel);
        JPasswordField passwordAgainField = new JPasswordField(15);
        panel.add(passwordAgainField);
        JButton updateButton = new JButton("DÜZENLE");
        panel.add(updateButton);
        JButton deleteButton = new JButton("SİL");
        panel.add(deleteButton);

        accountBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(accountBox.getSelectedIndex() != 0) {
                    complexAccountEntity = (ComplexAccount) accountBox.getSelectedItem();
                    authorityBox.setSelectedIndex(complexAccountEntity.getAuthority_id());
                }
                else
                    authorityBox.setSelectedIndex(0);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(authorityBox.getSelectedIndex() == 0 || authorityBox.getSelectedIndex() == 0 || passwordField.getText().isEmpty()
                            || passwordAgainField.getText().isEmpty())
                        throw new Exception("Bütün Alanları Doldurunuz");

                    if(complexAccountEntity.getAuthority_id() ==1)
                        throw new Exception("Admin Yetkisine Sahip Hesap İçin Düzenleme Yapılamaz");

                    if(!passwordField.getText().equals(passwordAgainField.getText()))
                        throw new Exception("Şifreler Uyuşmuyor");

                    Account entity = new Account();
                    entity.setStaff_id(complexAccountEntity.getStaff_id());
                    entity.setAuthority_id(((Authority) authorityBox.getSelectedItem()).getId());
                    entity.setStaff_id((new StaffMySQL().get(complexAccountEntity.getStaff_id())).getId());
                    entity.setPassword(passwordField.getText());

                    if(new AccountMySQL().update(entity)){
                        JOptionPane.showMessageDialog(null, "Hesap Düzenlendi");

                        accountBox.setSelectedIndex(0);
                        authorityBox.setSelectedIndex(0);
                        passwordField.setText("");
                        passwordAgainField.setText("");
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(complexAccountEntity == null)
                        throw new Exception("Hesap Seçiniz");

                    if(complexAccountEntity.getAuthority_id() == 1)
                        throw new Exception("Admin Yetkisine Sahip Hesap Silinemez");

                    int result = JOptionPane.showConfirmDialog(null,"Hesap Silmek İstiyor musunuz?", "UYARI", JOptionPane.YES_NO_OPTION);
                    if(result == 0 && new AccountMySQL().delete(complexAccountEntity)){
                        JOptionPane.showMessageDialog(null, "Hesap Silindi");

                        accountBox.setModel(new DefaultComboBoxModel(new AccountMySQL().getAllComplex().toArray()));

                        accountBox.setSelectedIndex(0);
                        authorityBox.setSelectedIndex(0);
                        passwordField.setText("");
                        passwordAgainField.setText("");
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
                }
            }
        });

        return panel;
    }
}
