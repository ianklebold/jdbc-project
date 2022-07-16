package com.jdbc.transactions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.h2.tools.RunScript;

public class NewTransactions {
    public static void main(String[] args) {
        
        try {
            //Para cada base de datos se tiene distinta url
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test");
            System.out.println("Conexion creada");

            RunScript.execute(connection, new FileReader("/home/administrador/Facultad/Udemy/JDBC/Repository/project-jdbc/src/main/java/com/jdbc/scripts/scripts_1.sql"));
            
            createPersonsWithCommit(connection);
            executeRollBack(connection);
            executeSavePoint(connection);
            giveMeAllPersons(connection);

            connection.close(); //Desconectarme de la base de datos
        } catch (SQLException | FileNotFoundException e) {
            // Se impirme el error en la pantalla
            e.printStackTrace();
        }
    }

    private static void createPersonsWithCommit(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = 
        connection.prepareStatement("INSERT INTO person(name, lastname, nickname) VALUES(?,?,?)");
        connection.setAutoCommit(false);
        try {
            preparedStatement.setString(1,"Ian");
            preparedStatement.setString(2,"Fernández");
            preparedStatement.setString(3,"iancarnivor");
            preparedStatement.executeUpdate();
    
            preparedStatement.setString(1,"Jefferson");
            preparedStatement.setString(2,"Farfan");
            preparedStatement.setString(3,"Foquita");
            preparedStatement.executeUpdate();
    
            preparedStatement.setString(1,"Brok");
            preparedStatement.setString(2,"Lessnar");
            preparedStatement.setString(3,"Broky");
            preparedStatement.executeUpdate();
    
            preparedStatement.close();
            connection.commit();     
        } catch (Exception e) {
            System.out.println("Rolling back because :" + e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void executeRollBack(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = 
        connection.prepareStatement("INSERT INTO person(name, lastname, nickname) VALUES(?,?,?)");
        System.out.println("Ejecutando RollBack");
        connection.setAutoCommit(false);
        try {
            preparedStatement.setString(1,"Ian");
            preparedStatement.setString(2,"Fernández");
            preparedStatement.setString(3,"iancarnivor");
            preparedStatement.executeUpdate();
    
            preparedStatement.setString(1,"Jefferson");
            preparedStatement.setString(2,null);
            preparedStatement.setString(3,"Foquita");
            preparedStatement.executeUpdate(); //<-- Aqui se ejecuta la excepcion

            connection.commit(); 
        } catch (SQLException e) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        preparedStatement.close();
    }

    private static void executeSavePoint(Connection connection) throws SQLException {
        Savepoint savePoint = null; 
        System.out.println("Implementando SavePoints");
        PreparedStatement preparedStatement = 
        connection.prepareStatement("INSERT INTO person(name, lastname, nickname) VALUES(?,?,?)");

        connection.setAutoCommit(false);
        try {
            preparedStatement.setString(1,"Ian");
            preparedStatement.setString(2,"Fernández");
            preparedStatement.setString(3,"iancarnivor");
            preparedStatement.executeUpdate();
            savePoint = connection.setSavepoint("controlPoint");


            preparedStatement.setString(1,"Jefferson");
            preparedStatement.setString(2,null);
            preparedStatement.setString(3,"Foquita");
            preparedStatement.executeUpdate(); //<-- Aqui se ejecuta la excepcion

            connection.commit(); 
        } catch (SQLException e) {
            if(savePoint != null){
                connection.rollback(savePoint); //<- Vuelve al punto de control
            }else{
                connection.rollback(); //<- Hago rollback de todo
            }
        } finally {
            connection.setAutoCommit(true);
        }
        preparedStatement.close();
    }

    private static void giveMeAllPersons( Connection connection ) throws SQLException {
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM person").executeQuery();
        /**
         * \n Es un salto de linea
         * \t Es un tabulador
         * Se usa printf para poder añadir datos en un string
         *  */    
        while (resultSet.next()) {
            System.out.printf("\n Id: [%d]\tName : [%s]\tLastName : [%s]\tNickName : [%s]",
            resultSet.getInt(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
        }
    }
}
