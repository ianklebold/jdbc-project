package com.jdbc.transactions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.h2.tools.RunScript;

import com.github.javafaker.Faker;

public class BatchTransactions {
    public static void main(String[] args) {
        
        try {
            //Para cada base de datos se tiene distinta url
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test");
            System.out.println("Conexion creada");

            RunScript.execute(connection, new FileReader("/home/administrador/Facultad/Udemy/JDBC/Repository/project-jdbc/src/main/java/com/jdbc/scripts/scripts_1.sql"));
            
            createPersonsBatch(connection);
            giveMeAllPersons(connection);

            connection.close(); //Desconectarme de la base de datos
        } catch (SQLException | FileNotFoundException e) {
            // Se impirme el error en la pantalla
            e.printStackTrace();
        }
    }

    private static void createPersonsBatch(Connection connection)throws SQLException{
        PreparedStatement preparedStatement = 
        connection.prepareStatement("INSERT INTO person(name, lastname, nickname) VALUES(?,?,?)");
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            preparedStatement.setString(1,faker.name().firstName());
            preparedStatement.setString(2,faker.name().lastName());
            preparedStatement.setString(3,faker.funnyName().name());
            
            preparedStatement.addBatch(); //<- Guarda todas las sentencias
        }
        //Commit del proceso batch
        int[] cantBatch = preparedStatement.executeBatch();
        
        System.out.println("Registros impactados por cada operación: \t");
        
        for (int i = 0; i < cantBatch.length; i++) 
        System.out.printf("\nSentencia [%d] - Resultado : [%d] \t",i,cantBatch[i]);
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
