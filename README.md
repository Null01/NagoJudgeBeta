# NagoJudgeBeta

> "La realidad social es causa y consecuencia de lo que es su educación superior".

NagoJudge es un espacio enfocado al crecimiento de habilidades académicas y profesionales. Un espacio donde se pondrá a prueba el factor de crecimiento sobre el cual se rige el sistema, basada en la sana competencia.

### Configuración
Descargar apache-tomee-plus-1.7.4 de la página oficial Apache TomEE & OpenEJB
[Apache TomEE](http://tomee.apache.org/downloads.html). 
Una vez descargado, descomprimir su contenido, y configurar los siguientes archivos

### DataSources
```sh
> cd [ruta_de_archivo_descomprimido]/apache-tomee-plus-1.7.4/conf/server.xml
```
Configurar el datasource en el archivo server.xml, dentro de la etiqueta <GlobalNamingResources/> agregar los <Resource/>, como se muestra en el ejemplo.

```sh
<GlobalNamingResources>
    <Resource
        id="jdbc/[your_datasource_name]"
        name="jdbc/[your_datasource_name]"
        type="javax.sql.DataSource"
        auth="Container"
        factory="edu.nagojudge.tools.security.EncryptedDataSourceFactory"
        maxActive="100"
        username="[your_username]"
        password="[your_password_encode]"
        driverClassName="[your_drivername]"
        url="jdbc:[your_db]://[host]:[puerto]/[db_name]?zeroDateTimeBehavior=convertToNull" />
</GlobalNamingResources>
```

### Aplicación
Generar los archivos de despliegue [*.war, *.jar] del proyecto, pegar el [nj-connector.jar] en la ruta:
```sh
> cd [ruta_de_archivo_descomprimido]/apache-tomee-plus-1.7.4/lib
```
Al igual que los drivers jdbc de la base de datos. Una vez realizados los pasos anteriores desplegar la aplicacion en nuestro servidor TomEE

