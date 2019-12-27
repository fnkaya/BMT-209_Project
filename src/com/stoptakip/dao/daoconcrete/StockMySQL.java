package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IStockServices;
import com.stoptakip.dto.complexmodels.ComplexStock;
import com.stoptakip.dto.complexmodels.ComplexTotalStock;
import com.stoptakip.dto.models.Stock;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StockMySQL extends DBHelper implements IStockServices {

    @Override
    public boolean insert(Stock entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO stock (product_id, staff_id, amount, date) " +
                    "VALUES ('"+ entity.getProduct_id() +"','"+ entity.getStaff_id() +"',"+ entity.getAmount() +",'"+ entity.getDate() +"')");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public ComplexTotalStock getComplex(int id) {
        ComplexTotalStock entity = null;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT product_id, name, SUM(amount) as total_stock FROM stock " +
                    "JOIN product on stock.product_id = product.id WHERE product.id ="+ id);
            if(resultSet.next()){
                entity = new ComplexTotalStock();
                entity.setProduct_name(resultSet.getString("name"));
                entity.setTotal_amount(resultSet.getInt("total_stock") - getSaleAmount(resultSet.getInt("product_id")));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }

    @Override
    public ArrayList<ComplexStock> getAllComplex(){
        ArrayList<ComplexStock> entities = new ArrayList<>();
        ComplexStock entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT stock.id, staff.name, product.name, amount, date FROM stock " +
                    "JOIN product on stock.product_id = product.id " +
                    "JOIN staff on stock.staff_id = staff.id ORDER BY id DESC");
            while(resultSet.next()){
                entity = new ComplexStock();
                entity.setId(resultSet.getInt("id"));
                entity.setProduct_name(resultSet.getString("product.name"));
                entity.setStaff_name(resultSet.getString("staff.name"));
                entity.setAmount(resultSet.getInt("amount"));
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
    public ArrayList<ComplexTotalStock> getTotalComplex() {
        ArrayList<ComplexTotalStock> entities = new ArrayList<>();
        ComplexTotalStock entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT product_id, name, SUM(amount) as total_stock FROM stock " +
                    "JOIN product on stock.product_id = product.id GROUP BY name ORDER BY name ASC");
            while(resultSet.next()){
                entity = new ComplexTotalStock();
                entity.setProduct_name(resultSet.getString("name"));
                entity.setTotal_amount(resultSet.getInt("total_stock") - getSaleAmount(resultSet.getInt("product_id")));
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
    public int getSaleAmount(int product_id){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(amount) as total_sale FROM sale " +
                    "JOIN product on sale.product_id = product.id WHERE product_id = "+ product_id +" GROUP BY name ORDER BY name ASC");
            if (resultSet.next())
                return resultSet.getInt("total_sale");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return 0;
    }
}
