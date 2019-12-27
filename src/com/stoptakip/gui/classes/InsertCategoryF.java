package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CategoryMySQL;
import com.stoptakip.dto.models.Category;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertCategoryF extends JDialog implements IDialog {

    public InsertCategoryF(){
        initFrame();
    }

    @Override
    public void initFrame() {
        JPanel panel = initPanel();

        add(panel);
        setTitle("KATEGORİ EKLE");
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.setBorder(BorderFactory.createTitledBorder("Kategori Bilgileri "));

        //Kategori ekleme tasarımı
        JLabel categoryLabel = new JLabel("Üst Kategori");
        panel.add(categoryLabel);
        JComboBox categoryBox = new JComboBox(new CategoryMySQL().getAll().toArray());
        categoryBox.insertItemAt("Üst Kategori Yok",0);
        categoryBox.setSelectedIndex(0);
        panel.add(categoryBox);
        JLabel nameLabel = new JLabel("Kategori Adı");
        panel.add(nameLabel);
        JTextField nameField = new JTextField(10);
        panel.add(nameField);
        JButton insertButton = new JButton("EKLE");
        panel.add(insertButton);
        JButton cancelButton = new JButton("İPTAL");
        panel.add(cancelButton);

        //Kategori ekleme işlemleri
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (nameField.getText().isEmpty() || nameField.getText().isBlank())
                        throw new Exception("Bütün Alanları Doldurun");

                    Category entity = new Category();
                    entity.setName(nameField.getText());
                    if (categoryBox.getSelectedIndex() != 0)
                        entity.setParent_id(((Category) categoryBox.getSelectedItem()).getId());
                    else
                        entity.setParent_id(0);

                    if (new CategoryMySQL().insert(entity)) {
                        JOptionPane.showMessageDialog(null, entity.getName() + " Kategorisi Eklendi");

                        //KategoriBox içeriğini yenileme işlemi
                        FMain.stockCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.stockCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                        FMain.stockCategoryBox.setSelectedIndex(0);
                        FMain.saleCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.saleCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                        FMain.saleCategoryBox.setSelectedIndex(0);

                        categoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        categoryBox.insertItemAt("Üst Kategori Yok", 0);
                        categoryBox.setSelectedIndex(0);
                    }
                    nameField.setText("");  nameField.requestFocusInWindow();
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null,exp.getMessage());
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
