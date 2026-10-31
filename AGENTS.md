## Descripción del proyecto

Este proyecto es una aplicación de gestión de reservas de aulas realizada para una tarea de 2.º de DAM.

Permite administrar aulas, profesores, franjas horarias y reservas.

## Tecnologías utilizadas

- Java 21 y Spring Boot.
- Maven para gestionar las dependencias.
- Spring Data JPA para trabajar con la base de datos.
- MySQL 8 ejecutado mediante Docker.
- Lombok para generar getters y setters.
- HTML, CSS y JavaScript para el frontend.
- IntelliJ IDEA como entorno de desarrollo.
- Postman para probar la API.

## Organización del código

El paquete principal es `es.raulsanchez.gestionaula`.

- `entity`: contiene Aula, Profesor, FranjaHoraria y Reserva.
- `repository`: contiene los repositorios JPA.
- `controller`: contiene los controladores REST.
- `src/main/resources/static/frontend`: contiene la página web, sus estilos y el JavaScript.

## Reglas para modificar el proyecto

1. Mantener el código sencillo, adecuado para un estudiante de 2.º de DAM.
2. Utilizar las clases y los repositorios existentes antes de crear otros nuevos.
3. No añadir frameworks al frontend. Utilizar HTML, CSS y JavaScript.
4. Mantener los nombres de las entidades y los campos existentes.
5. No cambiar los endpoints de la API sin explicar el motivo.
6. Explicar brevemente los cambios importantes para poder entenderlos.
7. No eliminar funcionalidades que ya funcionan.
8. Evitar cambios innecesarios y dependencias adicionales.

## Reglas de las reservas

- No permitir crear reservas con fechas anteriores a la actual.
- No permitir dos reservas de la misma aula, fecha y franja horaria.
- La fecha y hora de inserción se generan automáticamente en la entidad Reserva.
- Comprobar que el aula, el profesor y la franja horaria existen antes de crear una reserva.

## Pruebas

- Comprobar las operaciones CRUD de las cuatro entidades.
- Utilizar los códigos HTTP adecuados.
- Probar los filtros de aulas por ordenadores y capacidad.
- Comprobar que se rechazan las reservas duplicadas y las fechas pasadas.
- Mantener el funcionamiento del frontend conectado a Spring Boot.

## Uso de inteligencia artificial

La IA se utiliza como apoyo para explicar código, detectar errores, generar propuestas y desarrollar el frontend.

Antes de aceptar un cambio hay que revisar que sea compatible con el proyecto y comprender su funcionamiento.
