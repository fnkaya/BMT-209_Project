package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CategoryMySQL;
import com.stoptakip.dao.daoconcrete.ProductMySQL;
import com.stoptakip.dto.models.Category;
import com.stoptakip.dto.models.Product;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UpdateProductF extends JDialog implements IDialog {

    public UpdateProductF(){ initFrame(); }

    Product productEntity;

    @Override
    public void initFrame(){
        JPanel panel = initPanel();
        add(panel);
        setTitle("ÜRÜN DÜZENLE");
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
        westPanel.setBorder(BorderFactory.createTitledBorder("Ürün Arama"));
        JPanel westNorthPanel = new JPanel(new GridLayout(1,2));
        JLabel nameLabel = new JLabel("Ürün");
        westNorthPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        westNorthPanel.add(nameField);
        JPanel westSouthPanel = new JPanel((new BorderLayout()));
        JList productList = new JList();
        productList.setListData(new ProductMySQL().getAll().toArray());
        JScrollPane nameAreaScroll = new JScrollPane(productList);
        westSouthPanel.add(nameAreaScroll);
        westPanel.add(westNorthPanel, BorderLayout.NORTH);
        westPanel.add(westSouthPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new GridLayout(5,2));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Ürün Düzenle"));
        JLabel newBarcodeNoLabel = new JLabel("Barkod No");
        eastPanel.add(newBarcodeNoLabel);
        JTextField newBarcodeNoField = new JTextField(10);
        newBarcodeNoField.setEnabled(false);
        eastPanel.add(newBarcodeNoField);
        JLabel newNamelabel = new JLabel("Ürün Adı");
        eastPanel.add(newNamelabel);
        JTextField newNameField = new JTextField(10);
        newNameField.setEnabled(false);
        eastPanel.add(newNameField);
        JLabel newPriceLabel = new JLabel("Fiyat (₺)");
        eastPanel.add(newPriceLabel);
        JTextField newPriceField = new JTextField(10);
        newPriceField.setEnabled(false);
        eastPanel.add(newPriceField);
        JLabel categoryLabel = new JLabel("Kategori");
        eastPanel.add(categoryLabel);
        JComboBox categoryBox = new JComboBox(new CategoryMySQL().getAll().toArray());
        categoryBox.insertItemAt("---Kategori Seçiniz---",0);
        categoryBox.setSelectedIndex(0);
        categoryBox.setEnabled(false);
        eastPanel.add(categoryBox);
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
                productList.setListData(new ProductMySQL().getAllByName(nameField.getText()).toArray());
            }
        });

        productList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                productEntity = (Product) productList.getSelectedValue();

                if(!productList.isSelectionEmpty()) {
                    newBarcodeNoField.setText(productEntity.getBarcode_no());   newBarcodeNoField.setEnabled(true);
                    newNameField.setText(productEntity.getName()); newNameField.setEnabled(true);
                    newPriceField.setText(String.valueOf(productEntity.getPrice()));   newPriceField.setEnabled(true);
                    categoryBox.setSelectedIndex(new ProductMySQL().getCategoryId(productEntity));  categoryBox.setEnabled(true);
                    updateButton.setEnabled(true);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (newNameField.getText().isEmpty() || newPriceField.getText().isEmpty() || categoryBox.getSelectedIndex() == 0)
                        throw new Exception("Bütün Alanları Doldurun");

                    productEntity.setBarcode_no(newBarcodeNoField.getText());
                    productEntity.setName(newNameField.getText());
                    productEntity.setPrice(Double.parseDouble(newPriceField.getText().trim()));

                    Category categoryEntity = (Category) categoryBox.getSelectedItem();

                    if (new ProductMySQL().update(productEntity) && new ProductMySQL().updateRelation(productEntity, categoryEntity)){
                        JOptionPane.showMessageDialog(null, "Ürün Güncellendi");

                        FMain.stockCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.stockCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                        FMain.stockCategoryBox.setSelectedIndex(0);
                        FMain.saleCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.saleCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                        FMain.saleCategoryBox.setSelectedIndex(0);

                        nameField.setText("");
                        nameField.requestFocusInWindow();
                        productList.clearSelection();
                        productList.setListData(new ProductMySQL().getAll().toArray());
                        newBarcodeNoField.setText("");
                        newBarcodeNoField.setEnabled(false);
                        newNameField.setText("");
                        newNameField.setEnabled(false);
                        newPriceField.setText("");
                        newPriceField.setEnabled(false);
                        categoryBox.setSelectedIndex(0);
                        categoryBox.setEnabled(false);
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
