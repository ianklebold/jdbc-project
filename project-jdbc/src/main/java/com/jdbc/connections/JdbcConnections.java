package com.jdbc.connections;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            

            createPersons(preparedStatement);
            giveMeAllPersons(connection);
            giveMeAllPersonsWithExecute(connection);

            preparedStatement.close();
            connection.close(); //Desconectarme de la base de datos
        } catch (SQLException | FileNotFoundException e) {
            // Se impirme el error en la pantalla
            e.printStackTrace();
        }
    }

    private static void createPersons(PreparedStatement preparedStatement) throws SQLException {

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

    private static void giveMeAllPersonsWithExecute( Connection connection ) throws SQLException {

        PreparedStatement result = connection.prepareStatement("SELECT * FROM person WHERE lastname like 'F%'");
        
        if(result.execute() == true){
            while (result.getResultSet().next()) {
                System.out.printf("\n Id: [%d]\tName : [%s]\tLastName : [%s]\tNickName : [%s]",
                result.getResultSet().getInt(1), result.getResultSet().getString(2),result.getResultSet().getString(3),result.getResultSet().getString(4));
            }
        }

    }
}
