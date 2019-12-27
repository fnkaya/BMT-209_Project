package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CategoryMySQL;
import com.stoptakip.dao.daoconcrete.ProductMySQL;
import com.stoptakip.dto.models.Category;
import com.stoptakip.dto.models.Product;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertProductF extends JDialog implements IDialog {

    public InsertProductF(){
        initFrame();
    }

    @Override
    public void initFrame(){
        JPanel panel = initPanel();
        add(panel);
        setTitle("ÜRÜN EKLE");
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel(){
        JPanel panel = new JPanel(new GridLayout(5,2));
        panel.setBorder(BorderFactory.createTitledBorder("Ürün Bilgileri "));

        //Ürün bilgileri components
        JLabel barcodeNoLabel = new JLabel("Barkod No");
        panel.add(barcodeNoLabel);
        JTextField barcodeNoField = new JTextField(10);
        panel.add(barcodeNoField);
        JLabel nameLabel = new JLabel("Ürün Adı");
        panel.add(nameLabel);
        JTextField nameField = new JTextField(10);
        panel.add(nameField);
        JLabel priceLabel = new JLabel("Fiyat (₺)");
        panel.add(priceLabel);
        JTextField priceField = new JTextField(10);
        panel.add(priceField);
        JLabel categoryLabel = new JLabel("Kategoriler");
        panel.add(categoryLabel);
        JComboBox categoryBox = new JComboBox(new CategoryMySQL().getAll().toArray());
        categoryBox.insertItemAt("---Kategori Seçiniz---", 0);
        categoryBox.setSelectedIndex(0);
        panel.add(categoryBox);
        JButton insertButton = new JButton("EKLE");
        panel.add(insertButton);
        JButton cancelButton = new JButton("İPTAL");
        panel.add(cancelButton);

        //Ürün ekleme işlemleri
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try{
                   if(barcodeNoField.getText().isEmpty() || nameField.getText().isEmpty() || priceField.getText().isEmpty()
                           || barcodeNoField.getText().isBlank() || nameField.getText().isBlank() || priceField.getText().isBlank()
                           || categoryBox.getSelectedIndex() == 0 || Double.parseDouble(priceField.getText()) <= 0)
                       throw new Exception("Lütfen Bütün Alanları Doldurunuz");

                   Product productEntity = new Product();
                   productEntity.setBarcode_no(barcodeNoField.getText());
                   productEntity.setName(nameField.getText());
                   productEntity.setPrice(Double.parseDouble(priceField.getText().trim()));

                   Category categoryEntity = (Category) categoryBox.getSelectedItem();

                   if (new ProductMySQL().insert(productEntity) && new ProductMySQL().insertRelation(productEntity, categoryEntity)) {
                       JOptionPane.showMessageDialog(null, productEntity.getName() + " " + categoryEntity.getName() + " Kategorisine Eklendi");

                       FMain.stockCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                       FMain.stockCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                       FMain.stockCategoryBox.setSelectedIndex(0);
                       FMain.saleCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                       FMain.saleCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                       FMain.saleCategoryBox.setSelectedIndex(0);

                       barcodeNoField.setText("");  barcodeNoField.requestFocusInWindow();
                       nameField.setText("");
                       priceField.setText("");
                       categoryBox.setSelectedIndex(0);
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
