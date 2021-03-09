# Proyecto Final - Análisis de datos temporales en bases de grafos

## Exportar base de datos / modelo

1. Generar el modelo
2. Detener el proceso Neo4j
3. Asegurarse que exista previamente la carpeta database en tbdg (sino falla el comando)
4. Asegurarse que fof.db exista en el directorio data/databases de Neo4j (es el nombre de la db default)
5. ./bin/neo4j-admin dump --database=fof.db --to=/home/matias/tbdg/database/db.dump


## Importar base de datos / modelo

1. Detener el proceso Neo4j
2. Utilizar la opción --force para garantizar el overwrite de la db si es que existe
3. ./bin/neo4j-admin load --from=/home/matias/tbdg/database/db.dump --database=fof.db --force

## Cliente

El cliente se puede configurar a partir de un archivo `.properties`, caso contrario se toman las opciones por default
de la aplicación. Para que el cliente utilice el mismo, se le debe pasar la opción `-c` con la ruta al archivo.

Archivo de configuración de ejemplo:
```properties
database.bolt.host=localhost
database.bolt.port=7687
database.username=neo4j
database.password=admin
webapp.port = 7000
socialNetwork.persons=50
socialNetwork.maxFriendships=5
socialNetwork.maxFriendshipIntervals=2
socialNetwork.cityCount=5
socialNetwork.brandCount=2
socialNetwork.maxFans=2
socialNetwork.maxFansIntervals=1
socialNetwork.cPath.numberOfPaths=2,1
socialNetwork.cPath.minLength=5,10
flights.cityCount=10
flights.outgoingFlightsPerAirport=3
flights.flightsPerDestination=3
```

### Generación de base de datos / modelo

Para poder generar datos, la aplicación debe recibir el flag `--populate`, acompañado del modelo que se desee utilizar.

| Modelo  |  Parámetro |
|---|---|
|  SocialNetwork |  s |
| Flights  | f |

Ejemplo de ejecución:
```bash
java -jar client-1.0.jar --populate s --config ./client.properties

Primero desde la carpeta client
mvn clean package
Yo lo corro desde la carpeta pf-2018
java -jar .\tdbg-client\target\client-1.0-SNAPSHOT.jar --populate f --config .\config.properties
```

### Correr webapp

Para correr desde la linea de comandos ejecutar:

```bash
java -jar client-1.0.jar --config ./client.properties

Para correrlo desde la carpeta proyecto anterior, uso el siguiente comando
java -jar .\pf-2018-cvivtbdg\tdbg-client\target\client-1.0-SNAPSHOT.jar --config .\pf-2018-cvivtbdg\config.properties
```

Para correr desde el IDE, utilizar el punto de entrada `Main.main()`. La aplicación se levantará en <localhost:7000>.
