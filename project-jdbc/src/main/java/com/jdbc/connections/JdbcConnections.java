package com.jdbc.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnections {
    public static void main(String[] args) {
        
        try {
            //Para cada base de datos se tiene distinta url
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test");
            System.out.println("Conexion creada");

            connection.close(); //Desconectarme de la base de datos
            System.out.println("Conexion cerrada");
        } catch (SQLException e) {
            // Se impirme el error en la pantalla
            e.printStackTrace();
        }
    }
}
