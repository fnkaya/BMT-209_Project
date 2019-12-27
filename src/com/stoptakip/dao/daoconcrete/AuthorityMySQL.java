package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IGenericServices;
import com.stoptakip.dto.models.Authority;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AuthorityMySQL extends DBHelper implements IGenericServices<Authority> {

    @Override
    public boolean insert(Authority entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO authority (name) VALUES ('" + entity.getName() + "')");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Authority entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM authority WHERE id = " + entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Authority entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE authority SET name = '"+ entity.getName() +"' WHERE id = "+ entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean control(Authority entity) {
        return false;
    }

    @Override
    public Authority get(int id) {
        Authority entity = null;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM authority WHERE id = "+ id);
            if(resultSet.next()){
                entity = new Authority();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
            }else
                throw new SQLException("Yetki Bulunamadı");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }

    @Override
    public ArrayList<Authority> getAll() {
        ArrayList<Authority> entities = new ArrayList<>();
        Authority entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM authority ORDER BY id ASC");
            while (resultSet.next()) {
                entity = new Authority();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
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
