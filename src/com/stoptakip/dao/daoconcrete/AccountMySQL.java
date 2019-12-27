package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IAccountServices;
import com.stoptakip.dto.complexmodels.ComplexAccount;
import com.stoptakip.dto.models.Account;

import javax.swing.*;
import java.security.spec.ECField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountMySQL extends DBHelper implements IAccountServices {

    @Override
    public boolean insert(Account entity) {
        try {
            if(!control(entity)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO account (staff_id, authority_id, password)"
                        + "VALUES ('"+ entity.getStaff_id() +"',"+ entity.getAuthority_id() +",'"+ entity.getPassword() +"')");
                statement.close();
                connection.close();
                return true;
            }else
                throw new SQLException("Bu Personele Ait Bir Hesap Zaten Var");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Account entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM account WHERE staff_id = "+ entity.getStaff_id());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Account entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE account SET authority_id = "+ entity.getAuthority_id() +", password = '"+ entity.getPassword() +"'" +
                    " WHERE id = "+ entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean control(Account entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM account WHERE staff_id = "+ entity.getStaff_id());
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
    public Account get(int id) {
        Account entity = null;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM account WHERE id = "+ id);
            if (resultSet.next()){
                entity = new Account();
                entity.setStaff_id(resultSet.getInt("staff_id"));
                entity.setAuthority_id(resultSet.getInt("authority_id"));
                entity.setPassword(resultSet.getString("password"));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return entity;
    }

    public Account getByStaffId(int staff_id) {
        Account entity = null;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM account WHERE staff_id = "+ staff_id);
            if (resultSet.next()){
                entity = new Account();
                entity.setStaff_id(resultSet.getInt("staff_id"));
                entity.setAuthority_id(resultSet.getInt("authority_id"));
                entity.setPassword(resultSet.getString("password"));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return entity;
    }

    @Override
    public ArrayList<Account> getAll(){
        ArrayList<Account> entities = new ArrayList<>();
        Account entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY Id ASC");
            while(resultSet.next()){
                entity = new Account();
                entity.setStaff_id(resultSet.getInt("staff_id"));
                entity.setAuthority_id(resultSet.getInt("authority_id"));
                entity.setPassword(resultSet.getString("password"));
                entities.add(entity);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return entities;
    }


    public ArrayList<ComplexAccount> getAllComplex() {
        ArrayList<ComplexAccount> entities = new ArrayList<>();
        ComplexAccount entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account.id, staff_id, name, e_mail, password, authority_id FROM account" +
                    " JOIN staff on account.staff_id = staff.id WHERE activated = true ORDER BY name ASC");
            while(resultSet.next()){
                entity = new ComplexAccount();
                entity.setId(resultSet.getInt("account.id"));
                entity.setStaff_id(resultSet.getInt("staff_id"));
                entity.setStaff_name(resultSet.getString("name"));
                entity.setStaff_e_mail(resultSet.getString("e_mail"));
                entity.setPassword(resultSet.getString("password"));
                entity.setAuthority_id(resultSet.getInt("authority_id"));
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
