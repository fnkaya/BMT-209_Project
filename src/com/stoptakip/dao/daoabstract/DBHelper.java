package com.stoptakip.dao.daoabstract;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBHelper {

    private String url = "jdbc:mysql://localhost:3306/stvs?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey";
    private String userName = "root";
    private String password = "fkaya13";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return connection;
    }
}
