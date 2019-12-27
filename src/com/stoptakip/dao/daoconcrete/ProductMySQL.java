package com.stoptakip.dao.daoconcrete;

import com.stoptakip.dao.daoabstract.DBHelper;
import com.stoptakip.dao.daoabstract.IExtendedGenericServices;
import com.stoptakip.dto.models.Category;
import com.stoptakip.dto.models.Product;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ProductMySQL extends DBHelper implements IExtendedGenericServices<Product> {

    @Override
    public boolean insert(Product entity) {
        try {
            if(!control(entity)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO product (barcode_no, name, price) " +
                        "VALUES ('"+ entity.getBarcode_no() +"','"+ entity.getName() +"',"+ entity.getPrice() +")");
                statement.close();
                connection.close();
                return true;
            }else
                throw new SQLException(entity.getBarcode_no() +" Barkod Numaralı Ürün Mevcut");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Product entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM product WHERE id = "+ entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Product entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE product SET barcode_no = '"+ entity.getBarcode_no() +"',name = '"+ entity.getName() +"' ,price = "+ entity.getPrice() +
                    " WHERE id = "+ entity.getId() +"");
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean control(Product entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE barcode_no = '"+ entity.getBarcode_no() +"'");
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
    public Product get(int id) {
        Connection connection = getConnection();
        Product entity = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE id = "+ id);
            if (resultSet.next()){
                entity = new Product();
                entity.setId(resultSet.getInt("id"));
                entity.setBarcode_no(resultSet.getString("barcode_no"));
                entity.setName(resultSet.getString("name"));
                entity.setPrice(resultSet.getDouble("price"));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entity;
    }

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> entities = new ArrayList<>();
        Product entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Product();
                entity.setId(resultSet.getInt("id"));
                entity.setBarcode_no(resultSet.getString("barcode_no"));
                entity.setName(resultSet.getString("name"));
                entity.setPrice(resultSet.getDouble("price"));
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
    public ArrayList<Product> getAllByName(String name) {
        ArrayList<Product> entities = new ArrayList<>();
        Product entity;
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE name LIKE '%"+ name +"%' ORDER BY name ASC");
            while(resultSet.next()){
                entity = new Product();
                entity.setId(resultSet.getInt("id"));
                entity.setBarcode_no(resultSet.getString("barcode_no"));
                entity.setName(resultSet.getString("name"));
                entity.setPrice(resultSet.getDouble("price"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }

    public int getCategoryId(Product entity){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(category_id) as category_id FROM pc_relation WHERE product_id = "+ entity.getId());
            if (resultSet.next())
                return resultSet.getInt("category_id");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return 0;
    }

    public ArrayList<Product> getAllByCategoryId(int category_id) {
        ArrayList<Product> entities = new ArrayList<>();
        Product entity;
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, barcode_no, name, price FROM pc_relation " +
                    "JOIN product on pc_relation.product_id = product.id " +
                    "WHERE category_id = "+ category_id);
            while(resultSet.next()){
                entity = new Product();
                entity.setId(resultSet.getInt("id"));
                entity.setBarcode_no(resultSet.getString("barcode_no"));
                entity.setName(resultSet.getString("name"));
                entity.setPrice(resultSet.getFloat("price"));
                entities.add(entity);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return entities;
    }

    public boolean insertRelation(Product productEntity, Category categoryEntity){
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement2.executeQuery("SELECT id FROM product ORDER BY id DESC LIMIT 1");
            if(resultSet.next())
                productEntity.setId(resultSet.getInt("id"));
            statement.executeUpdate("INSERT INTO pc_relation (product_id, category_id) " +
                    "VALUES ("+ productEntity.getId() +","+ categoryEntity.getId() +")");
            do{
                categoryEntity = new CategoryMySQL().get(categoryEntity.getParent_id());
                statement.executeUpdate("INSERT INTO pc_relation (product_id, category_id) " +
                        "VALUES ("+ productEntity.getId() +","+ categoryEntity.getId() +")");
            }while (categoryEntity.getParent_id() != 0);
            statement.close();
            connection.close();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    public boolean deleteRelation(Product entity) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM pc_relation WHERE product_id = "+ entity.getId());
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }

    public boolean updateRelation(Product productEntity, Category categoryEntity){
        deleteRelation(productEntity);
        Connection connection = getConnection();
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO pc_relation (product_id, category_id) " +
                    "VALUES ("+ productEntity.getId() +","+ categoryEntity.getId() +")");
            do{
                categoryEntity = new CategoryMySQL().get(categoryEntity.getParent_id());
                statement.executeUpdate("INSERT INTO pc_relation (product_id, category_id) " +
                        "VALUES ("+ productEntity.getId() +","+ categoryEntity.getId() +")");
            }while (categoryEntity.getParent_id() != 0);
            statement.close();
            connection.close();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }
}
