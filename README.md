# Gestión de reservas de aulas

Aplicación web para administrar las aulas de un centro, sus profesores, las franjas horarias y las reservas. Incluye una API REST desarrollada con Spring Boot y una interfaz sencilla en HTML, CSS y JavaScript.

## Funcionalidades

- Crear, consultar, modificar y eliminar aulas, profesores, franjas horarias y reservas.
- Buscar aulas por disponibilidad de ordenadores o por capacidad superior a un valor.
- Crear reservas asociando un aula, un profesor y una franja horaria.
- Al crear una reserva, comprobar que la fecha no haya pasado, que las entidades relacionadas existan y que el aula no esté ya reservada en esa fecha y franja.
- Guardar automáticamente la fecha y hora de inserción de la reserva.

## Tecnologías

- Java 21 y Spring Boot.
- Maven y Spring Data JPA.
- MySQL 8.
- Lombok.
- HTML, CSS y JavaScript.

## Requisitos

- JDK 21.
- Docker, para ejecutar MySQL, o una instalación de MySQL 8.
- Conexión a Internet la primera vez que se ejecuta Maven, para descargar las dependencias.

## Puesta en marcha

1. Inicia MySQL 8. Si todavía no tienes un contenedor, puedes crear uno con este comando. El nombre `mysql_gestionaula` es el que se usa en los comandos siguientes; si eliges otro nombre, tendrás que usarlo también al iniciar el contenedor:

   ```powershell
   docker run --name mysql_gestionaula -e MYSQL_ROOT_PASSWORD=tu_clave -e MYSQL_DATABASE=gestion_aulas -p 3307:3306 -d mysql:8.0
   ```

   Si ya tienes creado el contenedor `mysql_gestionaula`, inícialo con:

   ```powershell
   docker start mysql_gestionaula
   ```

2. Comprueba la conexión en `src/main/resources/application.properties`. La URL y el usuario deben coincidir con la configuración de MySQL. Configura `spring.datasource.password` con la misma clave que hayas elegido para `MYSQL_ROOT_PASSWORD`.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3307/gestion_aulas
   spring.datasource.username=root
   spring.datasource.password=tu_clave
   ```

3. Desde la carpeta del proyecto, inicia la aplicación:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Abre la interfaz web en [http://localhost:8080/frontend/index.html](http://localhost:8080/frontend/index.html). La API queda disponible en `http://localhost:8080`.

Hibernate crea o actualiza las tablas al iniciar la aplicación (`spring.jpa.hibernate.ddl-auto=update`). Para compilar y ejecutar las pruebas:

```powershell
.\mvnw.cmd clean test
```

## Uso de la aplicación

La página permite gestionar cada tipo de registro desde su sección. Para crear una reserva, primero debe existir al menos un aula, un profesor y una franja horaria. Las reservas no admiten fechas anteriores a la actual ni la ocupación duplicada de una misma aula, fecha y franja horaria.

## API REST

Las peticiones y respuestas con contenido utilizan JSON. Las operaciones de creación devuelven `201 Created`; al consultar un identificador inexistente se devuelve `404 Not Found`; al eliminar correctamente se devuelve `204 No Content`. Las solicitudes de reserva no válidas devuelven `400 Bad Request`.

| Recurso | Operaciones |
| --- | --- |
| `/aulas` | `GET` lista; `GET /{id}` consulta; `POST` crea; `PUT /{id}` modifica; `DELETE /{id}` elimina. |
| `/profesores` | `GET` lista; `GET /{id}` consulta; `POST` crea; `PUT /{id}` modifica; `DELETE /{id}` elimina. |
| `/franjas` | `GET` lista; `GET /{id}` consulta; `POST` crea; `PUT /{id}` modifica; `DELETE /{id}` elimina. |
| `/reservas` | `GET` lista; `GET /{id}` consulta; `POST` crea; `PUT /{id}` modifica; `DELETE /{id}` elimina. |

### Filtros de aulas

- `GET /aulas/buscar?ordenadores=true` busca aulas con ordenadores. Usa `false` para las que no tienen.
- `GET /aulas/buscar?capacidad=20` busca aulas con capacidad **superior a 20**.

El endpoint aplica un filtro cada vez. Si no se indica ninguno, devuelve todas las aulas.

### Ejemplos de peticiones

Crear un aula (`POST /aulas`):

```json
{
  "nombre": "Aula 101",
  "capacidad": 25,
  "ordenadores": true
}
```

Crear un profesor (`POST /profesores`):

```json
{
  "nombre": "Ana",
  "apellidos": "García López",
  "email": "ana.garcia@example.com"
}
```

Crear una franja (`POST /franjas`):

```json
{
  "horaInicio": "09:00:00",
  "horaFin": "10:00:00"
}
```

Crear una reserva (`POST /reservas`). Los identificadores deben corresponder a registros existentes y la fecha debe ser hoy o posterior:

```json
{
  "fecha": "2027-01-15",
  "aula": {
    "id": 1
  },
  "profesor": {
    "id": 1
  },
  "franjaHoraria": {
    "id": 1
  }
}
```

## Estructura del proyecto

```text
src/main/java/es/raulsanchez/gestionaula/
├── controller/   # Controladores REST
├── entity/       # Aula, Profesor, FranjaHoraria y Reserva
└── repository/   # Repositorios Spring Data JPA

src/main/resources/
├── application.properties
└── static/frontend/   # Página, estilos y JavaScript
```

## Uso de inteligencia artificial y problemas encontrados

Durante el desarrollo de la práctica he utilizado ChatGPT y GitHub Copilot como herramientas de apoyo.

ChatGPT me ha ayudado a entender algunos métodos de Spring Boot, resolver errores, implementar las validaciones de las reservas y crear la interfaz web con HTML, CSS y JavaScript.

GitHub Copilot se ha utilizado para generar una propuesta de documentación del proyecto.

### Problemas encontrados

- **Errores al crear objetos desde JSON:** se revisaron las entidades y los constructores de Lombok hasta conseguir que los registros se guardaran correctamente.
- **Problemas con el puerto 8080:** en ocasiones había otra ejecución de Spring Boot abierta.
- **Conexión entre el frontend y la API:** inicialmente el HTML se abría desde IntelliJ por el puerto 63342, lo que provocaba problemas con las peticiones a Spring Boot. Se solucionó colocando los archivos web en `src/main/resources/static/frontend`.
- **Error 404 al abrir el frontend:** fue necesario reconstruir el proyecto para que Spring Boot reconociera los archivos estáticos.

## Uso de inteligencia artificial

Durante el desarrollo he utilizado ChatGPT para resolver dudas, corregir errores y crear el frontend. También he utilizado GitHub Copilot para generar y revisar el README.

### Ejemplos de consultas a ChatGPT

Estos ejemplos resumen consultas realizadas durante el desarrollo:

- ¿Cómo puedo evitar reservas duplicadas utilizando Spring Data JPA?
- ¿Cómo compruebo que una fecha no sea anterior a la actual?
- ¿Cómo conecto un frontend HTML, CSS y JavaScript con mi API REST?
- ¿Cómo puedo servir el frontend desde el mismo puerto que Spring Boot?

### Problemas y decisiones

- Tuve problemas con los constructores de Lombok al recibir datos JSON.
- El frontend daba errores de conexión al abrirlo desde el puerto 63342. Lo solucioné sirviéndolo desde Spring Boot en el puerto 8080.
- Revisé las propuestas de IA y comprobé su funcionamiento mediante Postman y la interfaz web.