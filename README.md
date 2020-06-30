# Covid Check

Covid Check es una aplicación para registrar análisis de Covid-19 y luego consultar los resultados totales o filtrar por campos específicos.

## Requisitos
Se requiere NPM, Maven y Visual Studio Code.

## Instalacion

Descargar mediante GIT los siguientes repositorios, que contienen el frontend y el backend:

```bash
Frontend:  https://github.com/pereyra-ana/covid-registry-frontend
Backend: https://github.com/pereyra-ana/covid-registry-backend 
```

Para el backend, posicionarse en la carpeta donde se encuentra el pom.xml de la aplicación, y ejecutar los siguientes comandos:
```bash
mvn package
mvn spring-boot:run
```

Luego, desplegar el frontend ejecutando los siguientes comandos desde el terminal de la aplicación:
```bash
npm install
ng s -o
```
Automáticamente, se abrirá la aplicación en un navegador, lista para usarse.

## Uso

Desde la pantalla principal, se mostrarán, en la sección superior, las estadísticas al momento de los análisis existentes. 

Debajo, se encuentra la sección para filtrar los registros. En dicha sección, se puede seleccionar un campo de la lista de filtros, para luego escoger desde la lista que se despliega, uno o más valores para realizar la búsqueda. Presionando sobre la lupa que aparece a la derecha, se realizará la obtención de los datos, que se muestran en la lista principal.

Luego, podrá accederse al detalle de un registro, presionando sobre el ícono de ojo que aparece a la derecha de cada registro.

Finalmente, para realizar un alta de registro, debe presionarse el botón "Agregar Análisis", que se encuentra en la parte superior y cargar los datos. Todos lo que poseen el asterisco son requeridos. El formato de la cadena de ADN debe ser separado por espacios, y la cantidad de letras de cada cadena debe ser igual a la cantidad de cadenas de ADN.

## Licencia
Creado por Ana Pereyra.