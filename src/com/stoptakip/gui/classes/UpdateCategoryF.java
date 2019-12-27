package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.CategoryMySQL;
import com.stoptakip.dto.models.Category;
import com.stoptakip.gui.interfaces.IDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UpdateCategoryF extends JDialog implements IDialog {

    public UpdateCategoryF() { initFrame(); }

    Category entity;

    @Override
    public void initFrame() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("KATEGORİ DÜZENLE");
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
        westPanel.setBorder(BorderFactory.createTitledBorder("Kategori Arama"));
        JPanel westNorthPanel = new JPanel(new GridLayout(1,2));
        JLabel nameLabel = new JLabel("Kategori");
        westNorthPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        westNorthPanel.add(nameField);
        JPanel westSouthPanel = new JPanel((new BorderLayout()));
        JList categoryList = new JList();
        categoryList.setListData(new CategoryMySQL().getAll().toArray());
        JScrollPane nameAreaScroll = new JScrollPane(categoryList);
        westSouthPanel.add(nameAreaScroll, BorderLayout.CENTER);
        westPanel.add(westNorthPanel, BorderLayout.NORTH);
        westPanel.add(westSouthPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new GridLayout(5,2));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Kategori Düzenle"));
        JLabel categoryLabel = new JLabel("Üst Kategori");
        eastPanel.add(categoryLabel);
        JComboBox categoryBox = new JComboBox();
        categoryBox.setEnabled(false);
        categoryBox.setEditable(false);
        eastPanel.add(categoryBox);
        JLabel newNamelabel = new JLabel("Kategori Adı");
        eastPanel.add(newNamelabel);
        JTextField newNameField = new JTextField(15);
        newNameField.setEnabled(false);
        eastPanel.add(newNameField);
        JButton updateButton = new JButton("DÜZENLE");
        updateButton.setEnabled(false);
        eastPanel.add(updateButton);
        JButton deleteButton = new JButton("SİL");
        deleteButton.setEnabled(false);
        eastPanel.add(deleteButton);

        panel.add(westPanel);
        panel.add(eastPanel);

        nameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {  }

            @Override
            public void keyPressed(KeyEvent e) {  }

            @Override
            public void keyReleased(KeyEvent e) {
                categoryList.setListData(new CategoryMySQL().getAllByName(nameField.getText()).toArray());
            }
        });

        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                entity = (Category) categoryList.getSelectedValue();

                if(!categoryList.isSelectionEmpty()) {
                    newNameField.setText(entity.getName());     newNameField.setEnabled(true);
                    categoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                    categoryBox.insertItemAt("Üst Kategori Yok", 0);
                    categoryBox.setSelectedIndex(entity.getParent_id());    categoryBox.setEnabled(true);
                    updateButton.setEnabled(true);  deleteButton.setEnabled(true);

                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(newNameField.getText().isEmpty())
                        throw new Exception("Bütün Alanları Doldurun");

                    entity.setName(newNameField.getText());

                    if(categoryBox.getSelectedIndex() != 0)
                        entity.setParent_id(((Category) categoryBox.getSelectedItem()).getId());
                    else
                        entity.setParent_id(0);

                    if(new CategoryMySQL().update(entity)){
                        JOptionPane.showMessageDialog(null, "Kategori Güncellendi");

                        FMain.stockCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.stockCategoryBox.insertItemAt("---Kategori Seçiniz---",0);
                        FMain.stockCategoryBox.setSelectedIndex(0);
                        FMain.saleCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                        FMain.saleCategoryBox.insertItemAt("---Kategori Seçiniz---",0);
                        FMain.saleCategoryBox.setSelectedIndex(0);

                        nameField.setText("");  nameField.requestFocusInWindow();
                        categoryList.clearSelection();  categoryList.setListData(new CategoryMySQL().getAll().toArray());
                        newNameField.setText("");   newNameField.setEnabled(false);
                        categoryBox.removeAllItems();   categoryBox.setEnabled(false);
                        updateButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, entity.getName() + " Kategorisini Silmek İstiyor musunuz?", "UYARI", JOptionPane.YES_NO_OPTION);
                if (result == 0 && new CategoryMySQL().delete(entity)) {
                    JOptionPane.showMessageDialog(null, entity.getName() + " Kategorisi Silindi");

                    FMain.stockCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                    FMain.stockCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                    FMain.stockCategoryBox.setSelectedIndex(0);
                    FMain.saleCategoryBox.setModel(new DefaultComboBoxModel(new CategoryMySQL().getAll().toArray()));
                    FMain.saleCategoryBox.insertItemAt("---Kategori Seçiniz---", 0);
                    FMain.saleCategoryBox.setSelectedIndex(0);

                    nameField.setText("");  nameField.requestFocusInWindow();
                    categoryList.clearSelection();  categoryList.setListData(new CategoryMySQL().getAll().toArray());
                    newNameField.setText("");   newNameField.setEnabled(false);
                    categoryBox.removeAllItems();   categoryBox.setEnabled(false);
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });

        return panel;
    }
}
