package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IExtendedGenericServices;
import com.stoptakip.dto.models.Customer;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerMySQL extends DBHelper implements IExtendedGenericServices<Customer> {

    @Override
    public boolean insert(Customer entity) {
        try {
            if(!control(entity)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO customer (name, e_mail, address, phone)"
                        + "VALUES ('"+ entity.getName() + "','" + entity.getE_mail() + "','" + entity.getAddress() + "','" + entity.getPhone() + "')");
                statement.close();
                connection.close();
                return true;
            }else
                throw new SQLException("Bu Müşteri Zaten Kayıtlı");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Customer entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM customer WHERE id = " + entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Customer entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE customer SET name = '"+ entity.getName() +"',e_mail = '"+ entity.getE_mail() +"'," +
                    "address = '"+ entity.getAddress() +"',phone = '"+ entity.getPhone() +"' WHERE id = "+ entity.getId() +"");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean control(Customer entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE name = '"+ entity.getName() +"' AND e_mail = '"+ entity.getE_mail() +"'");
            if (resultSet.next())
                return true;
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public Customer get(int id) {
        Customer entity = new Customer();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE id = "+ id);
            if (resultSet.next()){
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
            }else
                throw new SQLException("Müşteri Bulunamadı");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;

    }

    @Override
    public ArrayList<Customer> getAll() {
        ArrayList<Customer> entities = new ArrayList<>();
        Customer entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer ORDER BY name ASC");
            while (resultSet.next()) {
                entity = new Customer();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
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
    public ArrayList<Customer> getAllByName(String name) {
        ArrayList<Customer> entities = new ArrayList<>();
        Customer entity;
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE name LIKE '%"+ name +"%' ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Customer();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }
}
