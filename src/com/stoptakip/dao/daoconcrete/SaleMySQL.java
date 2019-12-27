package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.ISaleServices;
import com.stoptakip.dto.complexmodels.ComplexSale;
import com.stoptakip.dto.complexmodels.ComplexTotalSale;
import com.stoptakip.dto.models.Sale;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SaleMySQL extends DBHelper implements ISaleServices {

    @Override
    public boolean insert(Sale entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO sale (customer_id, product_id, amount, staff_id, date)"
                    + "VALUES ('"+ entity.getCustomer_id() +"','"+ entity.getProduct_id() +"',"+ entity.getAmount() +",'"+ entity.getStaff_id() +"','"+ entity.getDate() +"')");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<ComplexSale> getAllComplex() {
        ArrayList<ComplexSale> entities = new ArrayList<>();
        ComplexSale entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT sale.id, customer.name, product.name, price, amount, (amount * price) as total_price, staff.name, date FROM sale " +
                            "JOIN customer on sale.customer_id = customer.id " +
                            "JOIN product on sale.product_id = product.id " +
                            "JOIN staff on sale.staff_id = staff.id ORDER BY sale.id DESC");
            while(resultSet.next()){
                entity = new ComplexSale();
                entity.setId(resultSet.getInt("sale.id"));
                entity.setCustomer_name(resultSet.getString("customer.name"));
                entity.setProduct_name(resultSet.getString("product.name"));
                entity.setAmount(resultSet.getInt("amount"));
                entity.setPrice(resultSet.getDouble("price"));
                entity.setTotal_price(resultSet.getDouble("total_price"));
                entity.setStaff_name(resultSet.getString("staff.name"));
                entity.setDate(resultSet.getDate("date"));
                entities.add(entity);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }

    @Override
    public ArrayList<ComplexTotalSale> getTotalComplex() {
        ArrayList<ComplexTotalSale> entities = new ArrayList<>();
        ComplexTotalSale entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, SUM(amount) as total_sale, SUM(amount * price) as total_price FROM sale " +
                    "JOIN product on sale.product_id = product.id GROUP BY product.name ORDER BY name ASC");
            while(resultSet.next()){
                entity = new ComplexTotalSale();
                entity.setProduct_name(resultSet.getString("name"));
                entity.setTotal_amount(resultSet.getInt("total_sale"));
                entity.setTotal_price(resultSet.getDouble("total_price"));
                entities.add(entity);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }
}
