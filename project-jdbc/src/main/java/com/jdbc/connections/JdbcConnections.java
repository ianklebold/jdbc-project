package com.jdbc.connections;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.tools.RunScript;

public class JdbcConnections {
    public static void main(String[] args) {
        
        try {
            //Para cada base de datos se tiene distinta url
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test");
            System.out.println("Conexion creada");

            RunScript.execute(connection, new FileReader("src/main/java/com/jdbc/scripts/scripts_1.sql"));
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(name, lastname, nickname) VALUES(?, ?, ?)");
            
            preparedStatement.setString(1,"Ian");
            preparedStatement.setString(2,"Fernández");
            preparedStatement.setString(3,"iancarnivor");

            preparedStatement.executeUpdate();
            System.out.println("Script executed");

            preparedStatement.close();
            connection.close(); //Desconectarme de la base de datos
        } catch (SQLException | FileNotFoundException e) {
            // Se impirme el error en la pantalla
            e.printStackTrace();
        }
    }
}
