package com.stoptakip.gui.classes;

import com.stoptakip.dao.daoconcrete.*;
import com.stoptakip.dto.complexmodels.ComplexSale;
import com.stoptakip.dto.complexmodels.ComplexStock;
import com.stoptakip.dto.complexmodels.ComplexTotalSale;
import com.stoptakip.dto.complexmodels.ComplexTotalStock;
import com.stoptakip.dto.models.*;
import com.stoptakip.gui.interfaces.IMainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;

public class FMain extends JFrame implements IMainFrame {

    public FMain(){
        initFrame();
    }

    final Staff userEntity = FLogin.userEntity;//Giriş yapan kullanıcı
    static JComboBox stockCategoryBox;
    static JComboBox stockProductBox;
    static JComboBox saleCategoryBox;
    static JComboBox saleProductBox;
    static JComboBox customerBox;
    static JTable stockTable;
    static JTable saleTable;
    DefaultTableModel totalStockModel;
    DefaultTableModel totalSaleModel;

    @Override
    public void initFrame() {
        JPanel panel = initPanel();
        JMenuBar menuBar = initMenuBar();

        add(panel);
        setJMenuBar(menuBar);
        setTitle("SATIŞ VE STOK TAKİP");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/stock.png"));
        setSize(1250,800);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabs = initTabs();
        panel.add(tabs, BorderLayout.CENTER);

        //Total stok ve satış table paneli
        JPanel southPanel = new JPanel(new GridLayout(1,2));
        southPanel.setPreferredSize(new Dimension(10,250));

        //Total stok panel
        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(BorderFactory.createTitledBorder("TOPLAM STOK"));
        JScrollPane stockScrollPane = new JScrollPane();
        stockPanel.add(stockScrollPane, BorderLayout.CENTER);
        totalStockModel = new DefaultTableModel(new String[]{"Ürün", "Miktar"},0);
        JTable totalStockTable = new JTable(totalStockModel);
        stockScrollPane.setViewportView(totalStockTable);
        southPanel.add(stockPanel);

        //Total satış panel
        JPanel salePanel = new JPanel(new BorderLayout());
        salePanel.setBorder(BorderFactory.createTitledBorder("TOPLAM SATIŞ"));
        JScrollPane saleScrollPane = new JScrollPane();
        salePanel.add(saleScrollPane, BorderLayout.CENTER);
        totalSaleModel = new DefaultTableModel(new String[]{"Ürün", "Miktar", "Fiyat"},0);
        JTable totalSaleTable = new JTable(totalSaleModel);
        saleScrollPane.setViewportView(totalSaleTable);
        panel.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(salePanel);

        //Total stock table doldurma işlemi
        for(ComplexTotalStock entity : new StockMySQL().getTotalComplex())
            totalStockModel.addRow(entity.getData());

        //Total satış table doldurma işlemi
        for(ComplexTotalSale entity : new SaleMySQL().getTotalComplex())
            totalSaleModel.addRow(entity.getData());

        return panel;
    }

    @Override
    public JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Dosya menü
        JMenu fileMenu = new JMenu("Dosya");
        menuBar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem("Çıkış");
        fileMenu.add(exitItem);

        //Ürün - Kategori menü
        JMenu cpMenu = new JMenu("Ürün/Kategori");
        menuBar.add(cpMenu);
        JMenuItem insertProductionItem = new JMenuItem("Ürün Ekle");
        cpMenu.add(insertProductionItem);
        JMenuItem updateProductItem = new JMenuItem("Ürün Düzenle");
        cpMenu.add(updateProductItem);
        cpMenu.addSeparator();
        JMenuItem insertCategoryItem = new JMenuItem("Kategori Ekle");
        cpMenu.add(insertCategoryItem);
        JMenuItem updateCategoryItem = new JMenuItem("Kategori Düzenle");
        cpMenu.add(updateCategoryItem);

        //Personel - Hesap - Yetki menü
        JMenu staffMenu = new JMenu("Personel/Hesap");
        menuBar.add(staffMenu);
        JMenuItem insertStaffItem = new JMenuItem("Personel Ekle");
        staffMenu.add(insertStaffItem);
        JMenuItem updateStaffItem = new JMenuItem("Personel Düzenle");
        staffMenu.add(updateStaffItem);
        staffMenu.addSeparator();
        JMenuItem insertAccountItem = new JMenuItem("Hesap Oluştur");
        staffMenu.add(insertAccountItem);
        JMenuItem updateAccountItem = new JMenuItem("Hesap Düzenle");
        staffMenu.add(updateAccountItem);
        staffMenu.addSeparator();

        //Müşteri menü
        JMenu customerMenu = new JMenu("Müşteri");
        menuBar.add(customerMenu);
        JMenuItem insertCustomerItem = new JMenuItem("Müşteri Ekle");
        customerMenu.add(insertCustomerItem);
        JMenuItem updateCustomerItem = new JMenuItem("Müşteri Düzenle");
        customerMenu.add(updateCustomerItem);

        //Giriş yapan kullanıcının yetkisine göre arayüz
        int authorityId = new AccountMySQL().getByStaffId(userEntity.getId()).getAuthority_id();
        if(authorityId != 1)
            staffMenu.setEnabled(false);
        if(authorityId != 1 && authorityId != 2)
            cpMenu.setEnabled(false);

        //Ürün ekleme penceresini açar
        insertProductionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new InsertProductF();
                    }
                });
            }
        });

        //Ürün düzenleme penceresi
        updateProductItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { new UpdateProductF(); }
        });

        //Kategori ekleme penceresini açar
        insertCategoryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new InsertCategoryF(); }
                });
            }
        });

        //Kategori düzenle
        updateCategoryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new UpdateCategoryF(); }
                });
            }
        });

        //Personel ekleme penceresini açar
        insertStaffItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new InsertStaffF(); }
                });
            }
        });

        //Personel düzenle
        updateStaffItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new UpdateStaffF(); }
                });
            }
        });

        //Hesap ekleme penceresi
        insertAccountItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new InsertAccountF(); }
                });
            }
        });


        //Hesap düzenle
        updateAccountItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new UpdateAccountF(); }
                });
            }
        });

        //Müşteri ekleme penceresi
        insertCustomerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new InsertCustomerF(); }
                });
            }
        });

        //Müşteri düzenle
        updateCustomerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { new UpdateCustomerF(); }
                });
            }
        });

        //Çıkış
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FLogin();
            }
        });

        return menuBar;
    }

    @Override
    public JTabbedPane initTabs() {
        JTabbedPane tabs = new JTabbedPane();

        //Stok ekranı tasarımı
        JPanel stockPanel = new JPanel(new BorderLayout());
        tabs.addTab("STOK  ", new ImageIcon("icons/stock2.png"), stockPanel, "STOK");

        JPanel stockWestPanel = new JPanel(new BorderLayout());
        stockWestPanel.setBorder(BorderFactory.createTitledBorder("ÜRÜN BİLGİLERİ    "));
        stockPanel.add(stockWestPanel, BorderLayout.WEST);

        JPanel stockWestNorthPanel = new JPanel(new GridLayout(4,2));
        stockWestPanel.add(stockWestNorthPanel, BorderLayout.NORTH);
        JPanel stockWestSouthPanel = new JPanel();
        stockWestPanel.add(stockWestSouthPanel, BorderLayout.SOUTH);

        JLabel stockCategoryLabel = new JLabel("Kategori");
        stockWestNorthPanel.add(stockCategoryLabel);
        stockCategoryBox = new JComboBox(new CategoryMySQL().getAll().toArray());
        stockCategoryBox.insertItemAt("---Kategori Seçiniz---",0);
        stockCategoryBox.setSelectedIndex(0);
        stockWestNorthPanel.add(stockCategoryBox);
        JLabel stockProductLabel = new JLabel("Ürün");
        stockWestNorthPanel.add(stockProductLabel);
        stockProductBox = new JComboBox();
        stockProductBox.insertItemAt("---Ürün Seçiniz---",0);
        stockProductBox.setSelectedIndex(0);
        stockProductBox.setEnabled(false);
        stockWestNorthPanel.add(stockProductBox);
        JLabel stockAmountLabel = new JLabel("Ürün Adedi");
        stockWestNorthPanel.add(stockAmountLabel);
        JTextField stockAmountField = new JTextField(10);
        stockAmountField.setEnabled(false);
        stockWestNorthPanel.add(stockAmountField);
        JButton insertStockButton = new JButton("STOK EKLE");
        insertStockButton.setEnabled(false);
        stockWestNorthPanel.add(insertStockButton);

        //Stok table
        String [] stockColumns = {"Ürün Adı", "Miktar", "Personel", "Tarih"};
        DefaultTableModel stockModel = new DefaultTableModel(stockColumns,0);
        stockTable = new JTable(stockModel);
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockPanel.add(stockScrollPane, BorderLayout.CENTER);
        //Stok table doldurma
        for(ComplexStock entity : new StockMySQL().getAllComplex())
            stockModel.addRow(entity.getData());

        //Kategori comboBox item seçilince yapılan işlemler
        stockCategoryBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Kategorideki ürünleri ürün kutusuna ekler
                if(stockCategoryBox.getSelectedIndex() != 0){
                    stockProductBox.setModel(new DefaultComboBoxModel(new ProductMySQL().getAllByCategoryId(((Category) stockCategoryBox.getSelectedItem()).getId()).toArray()));
                    stockProductBox.setEnabled(true);
                }else
                    stockProductBox.setEnabled(false);

                stockProductBox.insertItemAt("---Ürün Seçiniz---",0);
                stockProductBox.setSelectedIndex(0);
                stockAmountField.setText("");
                insertStockButton.setEnabled(false);
            }
        });

        //Ürün comboBox item seçilince yapılan işlemler
        stockProductBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                if(stockProductBox.getSelectedIndex() != 0)
                    stockAmountField.setEnabled(true);
                else
                    stockAmountField.setEnabled(false);
                stockAmountField.setText("");
            }
        });


        stockAmountField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!stockAmountField.getText().trim().isEmpty())
                    insertStockButton.setEnabled(true);
                else
                    insertStockButton.setEnabled(false);
            }
        });

        //Stok ekleme işlemleri
        insertStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Stock stockEntity = new Stock();
                    Product productEntity;

                    if(stockProductBox.getSelectedIndex() != 0)
                         productEntity = (Product) stockProductBox.getSelectedItem();
                    else
                        throw new Exception("Lütfen Ürün Seçiniz");

                    if(stockAmountField.getText().isEmpty() || Integer.parseInt(stockAmountField.getText()) <= 0)
                        throw new Exception("Lütfen Miktar Bilgisi Giriniz");

                    stockEntity.setStaff_id(userEntity.getId());
                    stockEntity.setProduct_id(productEntity.getId());
                    stockEntity.setAmount(Integer.parseInt(stockAmountField.getText().trim()));
                    stockEntity.setDate(new Date(new java.util.Date().getTime()));

                    if(new StockMySQL().insert(stockEntity)) {
                        JOptionPane.showMessageDialog(null, productEntity.getName() + " Stoğa Eklendi");

                        stockProductBox.setSelectedIndex(0);
                        stockAmountField.setText("");
                        insertStockButton.setEnabled(false);

                        //Stok table içeriğini yenileme
                        int stockRowCount = stockModel.getRowCount();
                        for (int i = 0; i < stockRowCount; i++)
                            stockModel.removeRow(0);
                        for (ComplexStock entity : new StockMySQL().getAllComplex())
                            stockModel.addRow(entity.getData());
                        //Total stok table içeriğini yenileme
                        int totalStockRowCount = totalStockModel.getRowCount();
                        for(int i = 0; i < totalStockRowCount; i++)
                            totalStockModel.removeRow(0);
                        for(ComplexTotalStock entity : new StockMySQL().getTotalComplex())
                            totalStockModel.addRow(entity.getData());
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
                }

            }
        });

        //Satış ekranı tasarımı
        JPanel salePanel = new JPanel(new BorderLayout());
        tabs.addTab("SATIŞ  ", new ImageIcon("icons/sale.png"), salePanel,"SATIŞ");

        JPanel saleWestPanel = new JPanel(new BorderLayout());
        salePanel.add(saleWestPanel, BorderLayout.WEST);
        saleWestPanel.setBorder(BorderFactory.createTitledBorder("SATIŞ BİLGİLERİ    "));

        JPanel saleWestNorthPanel = new JPanel(new GridLayout(5,2));
        saleWestPanel.add(saleWestNorthPanel, BorderLayout.NORTH);
        JPanel saleWestSouthPanel = new JPanel();
        saleWestPanel.add(saleWestSouthPanel, BorderLayout.SOUTH);

        JLabel saleCustomerLabel = new JLabel("Müşteri");
        saleWestNorthPanel.add(saleCustomerLabel);
        customerBox = new JComboBox(new CustomerMySQL().getAll().toArray());
        customerBox.insertItemAt("---Müşteri Seçiniz---",0);
        customerBox.setSelectedIndex(0);
        saleWestNorthPanel.add(customerBox);
        JLabel saleCategoryLabel = new JLabel("Kategori");
        saleWestNorthPanel.add(saleCategoryLabel);
        saleCategoryBox = new JComboBox(new CategoryMySQL().getAll().toArray());
        saleCategoryBox.insertItemAt("---Kategori Seçiniz---",0);
        saleCategoryBox.setSelectedIndex(0);
        saleCategoryBox.setEnabled(false);
        saleWestNorthPanel.add(saleCategoryBox);
        JLabel saleProductNameLabel = new JLabel("Ürün");
        saleWestNorthPanel.add(saleProductNameLabel);
        saleProductBox = new JComboBox();
        saleProductBox.insertItemAt("---Ürün Seçiniz---",0);
        saleProductBox.setSelectedIndex(0);
        saleProductBox.setEnabled(false);
        saleWestNorthPanel.add(saleProductBox);
        JLabel saleAmountLabel = new JLabel("Ürün Adedi");
        saleWestNorthPanel.add(saleAmountLabel);
        JTextField saleAmountField = new JTextField(10);
        saleAmountField.setEnabled(false);
        saleWestNorthPanel.add(saleAmountField);
        JButton insertSaleButton = new JButton("SATIŞ YAP");
        insertSaleButton.setEnabled(false);
        saleWestNorthPanel.add(insertSaleButton);

        //Satış ekranı table tasarımı
        String [] saleColumns = {"Müşteri", "Ürün Adı", "Birim Fiyat", "Miktar", "Toplam Fiyat", "Personel", "Tarih"};
        DefaultTableModel saleModel = new DefaultTableModel(saleColumns, 0);
        saleTable = new JTable(saleModel);
        JScrollPane saleScrollPane = new JScrollPane(saleTable);
        salePanel.add(saleScrollPane, BorderLayout.CENTER);
        //Satış table doldurma
        for(ComplexSale entity : new SaleMySQL().getAllComplex())
            saleModel.addRow(entity.getData());

        //Sale customerBox item select listener
        customerBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(customerBox.getSelectedIndex() != 0)
                    saleCategoryBox.setEnabled(true);
                else
                    saleCategoryBox.setEnabled(false);

                saleCategoryBox.setSelectedIndex(0);
                saleAmountField.setText("");
                insertSaleButton.setEnabled(false);
            }
        });

        //Sale categoryBox item select
        saleCategoryBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(saleCategoryBox.getSelectedIndex() != 0){
                    saleProductBox.setModel(new DefaultComboBoxModel(new ProductMySQL().getAllByCategoryId(((Category) saleCategoryBox.getSelectedItem()).getId()).toArray()));
                    saleProductBox.setEnabled(true);
                }else
                    saleProductBox.setEnabled(false);

                saleProductBox.insertItemAt("---Ürün Seçiniz---",0);
                saleProductBox.setSelectedIndex(0);
                saleAmountField.setText("");
                insertSaleButton.setEnabled(false);
            }
        });

        //Sale productBox item select listener
        saleProductBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(saleProductBox.getSelectedIndex() != 0)
                    saleAmountField.setEnabled(true);
                else
                    saleAmountField.setEnabled(false);

            }
        });

        saleAmountField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!saleAmountField.getText().trim().isEmpty())
                    insertSaleButton.setEnabled(true);
                else
                    insertSaleButton.setEnabled(false);

            }
        });

        //Satış ekleme işlemleri
        insertSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Sale saleEntity = new Sale();
                    Customer customerEntity = (Customer) customerBox.getSelectedItem();
                    Product productEntity = (Product) saleProductBox.getSelectedItem();

                    if(saleAmountField.getText().isEmpty() || Integer.parseInt(saleAmountField.getText()) <= 0)
                        throw new Exception("Lütfen Ürün Miktarını Giriniz");

                    saleEntity.setProduct_id(productEntity.getId());
                    saleEntity.setCustomer_id(customerEntity.getId());
                    saleEntity.setStaff_id(userEntity.getId());
                    saleEntity.setAmount(Integer.parseInt(saleAmountField.getText()));
                    saleEntity.setDate(new Date(new java.util.Date().getTime()));

                    int productStockAmount = (new StockMySQL().getComplex(productEntity.getId())).getTotal_amount();
                    if(productStockAmount <= 0)
                        throw new Exception("Ürün Stokta Yok");
                    if(productStockAmount < Integer.parseInt(saleAmountField.getText()))
                        throw new Exception("Ürünün Stoktaki Miktarından Fazla Bir Miktar Girildi!\nStok Miktarı: "+ productStockAmount);

                    if(new SaleMySQL().insert(saleEntity)){
                        JOptionPane.showMessageDialog(null, productEntity.getName() + " Satış İşlemi Gerçekleştirildi");

                        customerBox.setSelectedIndex(0);
                        saleAmountField.setText("");
                        insertSaleButton.setEnabled(false);

                        //Satış table içeriğini yenileme
                        int saleRowCount = saleModel.getRowCount();
                        for(int i=0; i< saleRowCount; i++)
                            saleModel.removeRow(0);
                        for(ComplexSale entity : new SaleMySQL().getAllComplex())
                            saleModel.addRow(entity.getData());
                        //Total satış table içeriğini yenileme
                        int totalSaleRowCount = totalSaleModel.getRowCount();
                        for(int i=0; i < totalSaleRowCount; i++)
                            totalSaleModel.removeRow(0);
                        for(ComplexTotalSale entity : new SaleMySQL().getTotalComplex())
                            totalSaleModel.addRow(entity.getData());
                        //Total stok table içeriğini yenileme
                        int totalStockRowCount = totalStockModel.getRowCount();
                        for(int i=0; i < totalStockRowCount; i++)
                            totalStockModel.removeRow(0);
                        for(ComplexTotalStock entity : new StockMySQL().getTotalComplex())
                            totalStockModel.addRow(entity.getData());
                    }
                }catch (Exception exp){
                    JOptionPane.showMessageDialog(null, exp.getMessage());
                }
            }
        });

        return tabs;
    }
}
