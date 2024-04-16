package com.sendship.database;

import com.mysql.cj.MysqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    protected Connection connection;

    public DBConnection(){
        String url = "jdbc:mysql://localhost:3306/warehouse";
        String username = "root";
        String password = "H2e0n0z3el$";
        try {
            connection = DriverManager.getConnection(url, username, password);
        }catch (SQLException e) {
            System.out.println("Can't connect to database! :" + e.getMessage());
        }
    }
}
