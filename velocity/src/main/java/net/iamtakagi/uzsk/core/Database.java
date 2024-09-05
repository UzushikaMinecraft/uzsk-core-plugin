package net.iamtakagi.uzsk.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.SQLException;

public class Database {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";

    //MySQL
    private String url;
    private String user;
    private String password;

    private Connection connection;

    public Database(String host, String port, String user, String password, String database) {
       this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
       this.user = user;
       this.password = password;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet execute(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(String query) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

