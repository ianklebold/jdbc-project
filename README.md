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

## ExecuteUpdate

Se utiliza cuando se realice alguna operacion DML 'Data manipulation lenguage', como son: 

```
- INSERT
- UPDATE
- DELETE
- MERGE
- SELECT
- etc
```
Devuelve un entero que representa la cantidad de registros impactados

Tambien la podemos usar en operacion DDL 'Data definition lenguage', como son: 

```
- CREATE
- ALTER
- DROP
- RENAME
- TRUNCATE
- GRANT
- REVOKE
- etc
```
Donde en estos casos este metodo devuelve 0. 



## ExecuteQuery

Ejecuta una consulta SQL, donde devuelve un objeto del tipo ResultSet con los valores devueltos por la query. 



### ResultSet

Es el objeto que contiene el resultado de una query. Tiene dos metodos principales, next y getXXX donde "XXX" es el tipo de dato
Por ejemplo podría ser String, Int, Long, etc.

Creamos la query
```
ResultSet resultSet = connection.prepareStatement("SELECT * FROM person").executeQuery();
```

```
- next() : Es un puntero, devuelve falso cuando ya no hay nada, si hay algo devuelve el registro (Podemos usar los metodos get en estos)

- getXXX() : Es un metodo donde get va acompañado del tipo de dato y como parametro recibe la columna a la cual hacemos referencia
```

Ejemplo
```
 while (resultSet.next()) {
            System.out.println("Name: " + resultSet.getString(1) + "\n");
            System.out.println("LastName: "+ resultSet.getString(2) + "\n");
            System.out.println("NickName: "+ resultSet.getString(3) + "\t");
}
```


## Execute

Es una versión más generica de las dos anteriores. Se lo puede usar para manipular datos, realizar consultas, modificar, eliminar y crear estructuras como tables. 

A su vez, un execute devuelve distintos resultados dependiendo el tipo de operacion realizada: 

```
- Devuelve TRUE : Si se trata de una consulta, por lo que tambien devuelve un ResultSet

Se puede ocupar el metodo getResultSet() de ResultStatement para obtener los resultados de la query.


- Devuelve False : Si se trata de una manipulacion de datos o de estructura, por lo que devuelve un entero con la cantidad de registros impactados

Se puede ocupar el metodo getUpdateCount() de ResultStatement para obtener la cantidad de registros impactados


```
## Diferencia entre Statement, PreparedStatement y CallableStatement


### Statement
Este objeto permite ejecutar sentencias SQL pero no permite cambiar los parametros en tiempo de ejecucion. Por ejemplo SELECT * FROM PERSON seria una consulta que no se pasan valores por parametros, podria ejecutarse con Statement. 

### PreparedStatement
PreparedStatement es una especialización de Statement, es pre-compilado, y este si permite variar el valor de los parametros en tiempo de ejecucion. Recordemos que los parametros se indican con el signo "?".

### CallableStatement
Hereda de PreparedStatement permite ejecutar procedimientos.

Ejemplo : 

```
CallableStatement cst = connection.prepareCall("callMyProcedure(?,?)");

```

## Transacciones





































