package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IExtendedGenericServices;
import com.stoptakip.dto.models.Staff;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StaffMySQL extends DBHelper implements IExtendedGenericServices<Staff> {

    @Override
    public boolean insert(Staff entity) {
        try {
            if(!control(entity)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO staff (name, e_mail, address, phone, activated)"
                        + "VALUES ('"+ entity.getName() +"','"+ entity.getE_mail() +"','"+ entity.getAddress() +"','"+ entity.getPhone() +"',"+ entity.getActivated() +")");
                statement.close();
                connection.close();
                return true;
            }else
                throw new SQLException("Bu Personel Zaten Kayıtlı");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Staff entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM staff WHERE id = " + entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Staff entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE staff SET name = '"+ entity.getName() +"',e_mail = '"+ entity.getE_mail() +"'," +
                    "address = '"+ entity.getAddress() +"',phone = '"+ entity.getPhone() +"',activated = "+ entity.getActivated() +
                    " WHERE id = '"+ entity.getId() +"'");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean control(Staff entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff WHERE name = '"+ entity.getName() +"' AND e_mail = '"+ entity.getE_mail() +"'");
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
    public Staff get(int id) {
        Connection connection = getConnection();
        Staff entity = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff WHERE id = "+ id);
            if(resultSet.next()){
                entity = new Staff();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entity.setActivated(resultSet.getBoolean("activated"));
            }else
                throw new SQLException("Personel Bulunamadı");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }

    @Override
    public ArrayList<Staff> getAll() {
        ArrayList<Staff> entities = new ArrayList<>();
        Staff entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Staff();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entity.setActivated(resultSet.getBoolean("activated"));
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
    public ArrayList<Staff> getAllByName(String name) {
        ArrayList<Staff> entities = new ArrayList<>();
        Staff entity;
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff WHERE name LIKE '%"+ name +"%' ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Staff();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entity.setActivated(resultSet.getBoolean("activated"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }

    public ArrayList<Staff> getAllActive(){
        ArrayList<Staff> entities = new ArrayList<>();
        Staff entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff WHERE activated = true ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Staff();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entity.setActivated(resultSet.getBoolean("activated"));
                entities.add(entity);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }

    public Staff getByEMail(String e_mail) {
        Connection connection = getConnection();
        Staff entity = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff WHERE e_mail = '"+ e_mail +"'");
            if (resultSet.next()){
                entity = new Staff();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setE_mail(resultSet.getString("e_mail"));
                entity.setAddress(resultSet.getString("address"));
                entity.setPhone(resultSet.getString("phone"));
                entity.setActivated(resultSet.getBoolean("activated"));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }
}
