# JDBC Project


Java database connectivity es la API de java la cual permite implementar una gran cantidad de drivers que le permitirán conectarse a base de datos y manipular la información existente en él. 

JDBC comprende dos paquetes muy importantes los cuales son : 

```
- java.sql : Contiene las clases necesarias que permitiran manipular la informacion de las bases de datos 
- javax.sql : Contiene Api's para acceder a fuentes de datos del lado del servidor desde clientes JDBC

```
## DRIVERS

Los drivers son aquellos los cuales permiten conectar el servidor de base de datos con java.

## H2

Es una base de datos muy pequeña en memoria, generalmente utilizaas para test, demostraciones. Esta base de datos inicia con la aplicación y se cierra cuando se cierra la aplicación.

### Generar la conexion en H2

```
 //Para cada base de datos se tiene distinta url
Connection connection = DriverManager.getConnection("jdbc:h2:~/test");

 connection.close(); //Desconectarme de la base de datos
```
### Ejecutar Script SQL desde java con H2


```
 //Para cada base de datos se tiene distinta url
RunScript.execute(connection, new FileReader("enlace al sql"));
```

## PreparedStatement
Una vez creada la conexion con la base de datos, lo siguiente es crear un objeto preparedstatement que presenta una setencia sql precompilada, donde este objeto será utilizado para generar una sentencia SQL. 
Si se desea agregar un parametro a la sentencia se utilizará el simbolo ? . 


```
PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO table_name(name, lastname, nickname) VALUES(?,?,?)");
preparedStatement.close();

Para cargar esos parametros: 

preparedStatement.setString(1,"Ian");
preparedStatement.setString(2,"Fernández");
preparedStatement.setString(3,"iancarnivor");

El set depende del tipo de dato a cargar en la columna.

Para ejecutar el script:

preparedStatement.executeUpdate();

executeUpdate devuelve un numero, ese numero es el numero de registros insertados.
```















