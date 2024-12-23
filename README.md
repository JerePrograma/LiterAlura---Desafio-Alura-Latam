# LiterAlura 📚

## Descripción

Proyecto desarrollado como parte del programa **ONE (Oracle Next Education)**. La aplicación permite realizar consultas a la API **Gutendex** para buscar libros y autores, gestionar los datos localmente con una base de datos PostgreSQL, y generar estadísticas personalizadas sobre los libros registrados.

---

## Características

- **Buscar libros por título**: Consulta la API de Gutendex y guarda el primer resultado en la base de datos.
- **Guardar datos de libros y autores**: Almacena información relevante de libros y sus autores.
- **Consultar libros registrados por idioma**: Filtra libros según el idioma seleccionado.
- **Listar autores registrados**: Muestra todos los autores guardados en la base de datos.
- **Listar autores vivos en un año específico**: Identifica qué autores estaban vivos en un año dado.
- **Generar estadísticas de descargas**: Calcula y muestra datos como promedio, máximo y mínimo de descargas.
- **Top 10 libros más descargados**: Presenta los libros con mayor número de descargas.

---

## Tecnologías Usadas

- **Java**: Lenguaje principal.
- **Spring Boot**: Framework para la gestión de la aplicación.
- **Hibernate**: ORM para la interacción con la base de datos.
- **PostgreSQL**: Base de datos utilizada para almacenar los datos.
- **Gutendex API**: Fuente de datos externa.
- **Maven**: Herramienta de gestión de dependencias.

---

## Configuración del Proyecto

### 1. Clonar el Repositorio

````bash
git clone https://github.com/tu_usuario/liter-alura.git
cd liter-alura

---

### 2. Configurar la Base de Datos

La configuración de la base de datos PostgreSQL se realiza en el archivo `application.properties`:

```properties
spring.application.name=LiterAlura

spring.datasource.url=jdbc:postgresql://localhost:5432/liter_alura
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

---

### 3. Ejecutar la Aplicación

#### Opción 1: Usando Maven
```bash
mvn spring-boot:run

```bash
java -jar target/liter-alura-0.0.1-SNAPSHOT.jar

---

### 4. Acceder a la Aplicación

- **Consola**: Interactúa con la aplicación directamente desde la terminal. El menú principal se mostrará para que selecciones las opciones disponibles.

---

## Funcionalidades

### 1. Buscar Libro por Título
- Realiza una consulta a la API Gutendex utilizando un título ingresado por el usuario.
- Recupera y muestra los detalles del primer libro que coincide con el título.
- Guarda el libro en la base de datos si no existe previamente.

### 2. Listar Libros Registrados
- Muestra todos los libros almacenados en la base de datos.
- Incluye detalles como título, autor, idioma y número de descargas.

### 3. Listar Autores Registrados
- Presenta una lista de todos los autores almacenados en la base de datos.
- Incluye información como nombre, fecha de nacimiento y fecha de fallecimiento (si aplica).

### 4. Listar Autores Vivos en un Año
- Permite al usuario ingresar un año específico para buscar autores vivos durante ese periodo.
- Los resultados se basan en los años de nacimiento y fallecimiento almacenados en la base de datos.

### 5. Listar Libros por Idioma
- Filtra y muestra los libros almacenados en la base de datos según el idioma seleccionado.
- Idiomas admitidos: `es` (español), `en` (inglés), `fr` (francés), `pt` (portugués).

### 6. Generar Estadísticas
- Proporciona estadísticas generales sobre los libros almacenados, tales como:
  - Total de libros registrados.
  - Número total de descargas.
  - Promedio de descargas por libro.
  - Máximo y mínimo de descargas.

### 7. Top 10 Libros Más Descargados
- Presenta una lista con los 10 libros más descargados almacenados en la base de datos.
- Ordenados en función del número de descargas en orden descendente.

### 8. Buscar Autor por Nombre
- Permite buscar autores en la base de datos utilizando un nombre o parte de él.
- Muestra información detallada del autor si se encuentra en la base de datos.
````
