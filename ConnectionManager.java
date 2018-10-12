package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    public static Connection initConnection() throws Exception {
        StringBuilder url = new StringBuilder("jdbc:mysql://pc1620:3306/arvindhakrishna_k?")
                                              .append("user=arvindhakrishna_k&password=demo")
                                              .append("&useSSL=false");

        Connection connection = DriverManager.getConnection(url.toString());
        connection.setAutoCommit(false);
        return connection;
    }
}
