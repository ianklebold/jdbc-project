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














