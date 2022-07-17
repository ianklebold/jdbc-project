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

Conjunto de sentencias SQL donde se ejecutan todas de una vez. Las transacciones se completan si y solo si se ejecutan todas sus sentencias con exito.

Si en algun paso intermedio algo falla, entonces se retorna a un estado seguro es decir un estado anterior de los datos.

Se busca que se cumpla la condicion de ACID

```
- Atomica -> Todas las transacciones se ejecutan como una sola. O se ejecutan todas las sentencias o no se ejecuta nada (No hay impacto). 

- Consistencia -> Cuando todas las sentencias fueron correctas se debe ejecutar un commit, es decir guardar los datos que se han modificado y que estos guarden una correcta relación. Por ejemplo, al realizar una transaccion de dinero, se descuente el monto correspondiente de la cuenta origen y aumente la cantidad correcta en al cuenta destino.

- Insolacion -> Las transacciones se ejecutan independentes entre ellas.

- Durable -> Asegura que los datos deben de permanecer ante fallos. 
```

Comandos para controlar transacciones: 
```
- Commit -> Confirma la transaccion 

- Roll back -> Descarta los cambios realizados anteriormente

- Save Point -> Es un punto de control, en el cual se puede regresar al realizar un rollback, de esta forma no descartamos absolutamente todos los cambios

- Set Transaction -> Asigna un nombre a la transaccion
```
### Commit

Significa guardar los datos en la base de datos realizada una sentencia SQL

```
Por ejemplo con este comando generamos un commit a partir de JAV JDBC: 

- preparedStatement.executeUpdate();
```
### Auto-commit

Permite hacer commit de la base de datos de forma independiente. Cada vez que ejecutamos para confirmar una transaccion se genera automaticamente un commit , si queremos trabajar con transacciones esto se debe deshabilitar porque lo que buscamos es que NO se ejecuten commits cada una sentencia sino cada un cojunto de sentencias.

Deshabilitamos autocommit: 

```
- connection.setAutoCommit(false);
```
Esto me permite controlar cuando quiero hacer un commit y mandar más de una sentencia en una transacción. A partir de esto es nuestra responsabilidad tener que controlar los commits y rollbacks. 

### Rollback

Ante algun fallo el rollback me permite volver a un estado seguro,es decir, todas las sentencias generadas si en alguna falla entonces con el rollback podemos hacer que no se concreten o persistan estas sentencias. 

¿Como hacer commits y rollbacks manuales?

```
- connection.commit();
- connection.rollback();
```

Es buena practica luego de generar un commit y/o rollback manual volver a un autocommit 

```
- connection.setAutoCommit(true);
```


### SavePoint

En lugar de hacer un rollback de todos los commits, es decir deshacer todo lo que hicimos y volver a un estado anterior, podemos guardar puntos de control.
Esto lo hacemos con el siguiente comando: 

```
- SavePoint savePoint = connection.setSavepoint("nameSavePoint");
```

Y el rollback que generemos puede ser total o hasta el savePoint: 

```
Rollback completo : 
- connection.rollback();

Rollback parcial : 
- connection.rollback(savePoint);
```

Liberamos el recurso savepoint con:

```
- connection.releaseSavepoint(savePoint);
```

### JavaFaker

Faker es una API que permite generar datos aleatorios para probar nuestras aplicaciones. Para ello es necesario añadir su correspondiente dependencia en el POM.XML

Ejemplo : 
```
Faker faker = new Faker();
        
System.out.println(faker.name().firstName());
System.out.println(faker.name().lastName());
System.out.println(faker.dragonBall().character());
```

### addBatch

Es una forma de tratar las transacciones, lo que permite es a partir de PreparedStatement ir creando los registros y con addBatch acumular este conjunto de operaciones. Al final cuando terminamos el proceso batch damos a executeBatch() para finalizar, nos retorna las sentencias guardadas y a su vez retorna el numero de registros guardados.

Ejemplo:
```
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
```


## Datasources

Los datasources es un objeto perteneciente a javax que permite de manera más flexible que DriverManager hacer la conexión con las bases de datos. 

El dataSource será una clase que implementará la interfaz datasource, donde está clase tendrá como función la conexion a la base de datos, de tal forma que en cualquier otra clase donde necesite realizar una conexion a la base de datos, solamente tengo que preocuparme por llamar al metodo getConnection que implementará DataSource. 

Lo que logramos con los datasources es centralizar la conexión a la base de datos.

```
public class DataSourceConnections {
    
    public static void main(String[] args) {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:~/test");
        /*
         * Para este caso JdbcDataSource es solo de H2, es decir JdbcDataSource
         * Es una implementacion de Datasource.
         * 
         * Si es necesario jdbcDataSource se puede darle username y password para conectarse
         * a la base de datos.
         */
        try {
            Connection connection = jdbcDataSource.getConnection();    
            RunScript.execute(connection, new FileReader("/home/administrador/Facultad/Udemy/JDBC/Repository/project-jdbc/src/main/java/com/jdbc/scripts/scripts_1.sql"));
            
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            
        }
        
    }
}

```
## Pool de conexiones 

Es un conjunto de conexiones fisicas que pueden ser reutilizadas por multiples clientes. En lugar de abrir y cerrar conexiones por cada operaciones, utilizamos una sola y la reutilizamos. Al cerrar la aplicacion el pull de conexiones debe de ser cerrada. 

Tendremos un lugar donde tendremos habilitadas "n" cantidades de conexiones, entonces un cliente utilizará una de estas conexiones y cuando las deje de utilizar la liberará. Cuando se inicia la aplicacion se inician todas las conexiones. 

Tendremos varios pools de conexiones, como por ejemplo: 

```
- HikariCP
- dbcp2
- c3po

Además cada driver de base de datos tiene su propio pool de conexiones. 
```
### Comparaciónes y caracteristicas

Un pool de conexiones abstrae a los clientes tener que constantemente implementar la conexion (indicar base de datos, usuario y contraseña, etc), nos permite centralizar la conexion e inidicar la cantidad de ellas minima y maxima. Nos permite con tan solo un metodo (getConnection) poder disponer de la conexión y cuando ya no la ocupo habilitarla, es perfecta cuando tenemos distintas querys ejecutandose en paralelo. 

A su vez el tiempo de implementar una conexion nueva en un pool es mucho más corta a comparación de una conexión DriverManager. 

```
public class PoolComparative {
    private static final int NUM_CONNECTIONS = 200;

    /**
     * 1   - 255ms    - 244ms
     * 10  - 339ms    - 288ms
     * 20  - 438ms    - 400ms
     * 30  - 492ms    - 381ms
     * 100 - 942ms    - 367ms
     * 200 - 1387ms   - 362ms
     *  
    */
    public static void main(String[] args) {

        try {
            JdbcConnectionPool connectionPool = JdbcConnectionPool.create("jdbc:h2:~/test", "", "");
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < NUM_CONNECTIONS; i++) {
                Connection connection = connectionPool.getConnection();
                connection.close();
            }
            System.out.println("Total Time : " + (System.currentTimeMillis()-startTime) + "ms");

            connectionPool.getConnection().close();
        } catch (SQLException e) {
            System.out.println("No fue posible la conexion, result : " + e.getMessage());
        }
        
    }
}
```



### Pool de conexiones de HikariCp

Es un pool de conexiones aislado de la base de datos. Es el pool de conexiones con mayor performance. Para utilizarlo se debe aplicar su dependencia correspondiente en el POM.XML del proyecto. 


## Spring JDBC

Para utilizar en JDBC de Spring se deben importar las dependencias de JDBC API y la dependencia del driver de la base de datos que necesitemos. 

Por ejemplo : 

```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
  
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
```

### ¿Que incluye starter-jdbc?

Starter incluye una serie de paquetes, apis, que se utiliza en el proyecto. En este caso starter-jdbc contine: 
```
- spring-boot-starter
- HikariCP
- spring-jdbc
```
### Configurar Datasource

En tu properties, configuralo de la siguiente forma

```
spring.datasource.url=jdbc:mysql://localhost:3306/name_database
spring.datasource.username=username
spring.datasource.password=password
```
Mysql es solo un ejemplo, debes respetar la url que indique el servidor de base de datos que vas a ocupar

### ¿Que pool de conexiones utiliza Spring-boot?

El pool de conexiones por default es hikariCP.

¿Como podemos darnos cuenta de esto?

Spring tiene un archivo principal llamado Application. Dentro ejecuta una clase Application que dá con el inicio de toda la aplicacion, nosotros podemos obtener el contexto de spring asignandola a una variable.

![image](https://user-images.githubusercontent.com/56406481/179375870-9dbd1f97-5949-4760-8bee-0daf08208ad5.png)

Este contexto me permite acceder a los Beans administrados por spring. 

Podemos acceder a un bean de spring a partir de ejecutar el siguiente metodo: 

```
context.getBean();
```
Y a este meotodo le pasas por parametro el tipo de bean que queres obtener. En este caso nos intera un Datasource.

![image](https://user-images.githubusercontent.com/56406481/179379416-b889604d-63aa-40da-8aa2-464bbe9ff3be.png)


Aquí lo vemos implementa HikariDataSource: 

![image](https://user-images.githubusercontent.com/56406481/179379424-03828d36-24da-4269-a5c3-051be035e99a.png)


DATO! 
para los logs ocupar la dependencia slf4j : 

![image](https://user-images.githubusercontent.com/56406481/179379437-264ae288-24b3-4fd3-8fb7-37e1e1de2ddd.png)










































