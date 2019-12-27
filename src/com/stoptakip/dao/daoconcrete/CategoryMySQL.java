package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IExtendedGenericServices;
import com.stoptakip.dto.models.Category;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class CategoryMySQL extends DBHelper implements IExtendedGenericServices<Category> {

    @Override
    public boolean insert(Category entity) {
        try {
            if(!control(entity)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO category (name, parent_id)" +
                        " VALUES ('"+ entity.getName() +"',"+ entity.getParent_id() +")");
                statement.close();
                connection.close();
                return true;
            }else
                throw new SQLException("Bu Kategori Zaten Var");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Category entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM category WHERE id = "+ entity.getId());

            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM category WHERE parent_id = "+ entity.getId());
            while (resultSet.next()){
                statement2.executeUpdate("DELETE FROM category WHERE id = "+ resultSet.getInt("id"));
            }
            statement.close();
            statement2.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Category entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE category SET name = '"+ entity.getName() +"',parent_id = "+ entity.getParent_id() +
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
    public boolean control(Category entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM category WHERE name = '"+ entity.getName() +"'");
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
    public Category get(int id) {
        Category entity = null;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM category WHERE id = "+ id);
            if(resultSet.next()){
                entity = new Category();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setParent_id(resultSet.getInt("parent_id"));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }

    @Override
    public ArrayList<Category> getAll() {
        ArrayList<Category> entities = new ArrayList<>();
        Category entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM category ORDER BY id ASC");
            while(resultSet.next()){
                entity = new Category();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("Name"));
                entity.setParent_id(resultSet.getInt("parent_id"));
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
    public ArrayList<Category> getAllByName(String name) {
        ArrayList<Category> entities = new ArrayList<>();
        Category entity;
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM category WHERE name LIKE '%"+ name +"%' ORDER BY id ASC");
            while(resultSet.next()){
                entity = new Category();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setParent_id(resultSet.getInt("parent_id"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }
}
