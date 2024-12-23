package com.literalura.principal;

import com.literalura.dto.Data;
import com.literalura.dto.DtoLibro;
import com.literalura.model.Autor;
import com.literalura.model.Language;
import com.literalura.model.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import com.literalura.service.ConsumoAPI;
import com.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi;
    private final ConvierteDatos conversor;
    private final LibroRepository libroRepositorio;
    private final AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
        this.consumoApi = new ConsumoAPI();
        this.conversor = new ConvierteDatos();
    }

    public void muestraElMenu() {
        int opcion;
        do {
            System.out.println("""
                    1. Buscar libros por título
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un año
                    5. Listar libros por idioma
                    6. Generar estadísticas
                    7. Top 10 libros más descargados
                    8. Buscar autor por nombre
                    9. Listar autores por año de nacimiento
                    0. Salir
                    """);
            System.out.print("Elige una opción: ");
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                ejecutarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
                opcion = -1;
            }
        } while (opcion != 0);
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibroPorTitulo();
            case 2 -> listarLibrosRegistrados();
            case 3 -> listarAutoresRegistrados();
            case 4 -> listarAutoresVivosPorAnio();
            case 5 -> listarLibrosPorIdioma();
            case 6 -> generarEstadisticas();
            case 7 -> listarTop10Libros();
            case 8 -> buscarAutorPorNombre();
            case 9 -> listarAutoresPorAnioNacimiento();
            case 0 -> System.out.println("Hasta luego.");
            default -> System.out.println("Opción inválida, intenta de nuevo.");
        }
    }

    private Data obtenerDatosAPI(String titulo) {
        try {
            String URL_BASE = "https://gutendex.com/books";
            String url = URL_BASE + "?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String json = consumoApi.obtenerDatos(url);

            if (json == null || json.isBlank()) {
                System.out.println("No se recibió respuesta de la API.");
                return null;
            }

            return conversor.obtenerDatos(json, Data.class);
        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
            return null;
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Escribe el título del libro que deseas buscar: ");
        String titulo = teclado.nextLine();

        Data data = obtenerDatosAPI(titulo);
        if (data == null || data.results().isEmpty()) {
            System.out.println("No se encontraron libros con el título proporcionado.");
            return;
        }

        DtoLibro primerLibro = data.results().get(0);
        System.out.println("Título: " + primerLibro.titulo());
        System.out.println("Autor: " + primerLibro.autores().get(0).nombre());
        System.out.println("Idioma: " + primerLibro.idioma().get(0));
        System.out.println("Descargas: " + primerLibro.descargas());

        guardarLibro(primerLibro);
    }

    private void guardarLibro(DtoLibro dtoLibro) {
        Autor autor = autorRepositorio.findByNombreIgnoreCase(dtoLibro.autores().get(0).nombre())
                .orElseGet(() -> autorRepositorio.save(new Autor(dtoLibro.autores().get(0))));

        if (libroRepositorio.findByTituloIgnoreCase(dtoLibro.titulo()).isPresent()) {
            System.out.println("El libro ya está registrado.");
            return;
        }

        Libro libro = new Libro(dtoLibro, autor);
        libroRepositorio.save(libro);
        System.out.println("Libro guardado exitosamente.");
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorAnio() {
        System.out.print("Ingresa el año para buscar autores vivos: ");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autoresVivos = autorRepositorio.findAutoresVivosEnAnio(anio);
            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                autoresVivos.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingresa un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Escribe el idioma (es, en, fr, pt): ");
        try {
            Language idioma = Language.fromString(teclado.nextLine().toLowerCase());
            List<Libro> librosPorIdioma = libroRepositorio.findByIdioma(idioma);
            if (librosPorIdioma.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma especificado.");
            } else {
                librosPorIdioma.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no válido.");
        }
    }

    private void generarEstadisticas() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        var estadisticas = libros.stream().mapToInt(Libro::getDescargas).summaryStatistics();

        System.out.println("Estadísticas:");
        System.out.println("Total libros: " + estadisticas.getCount());
        System.out.println("Total descargas: " + estadisticas.getSum());
        System.out.println("Promedio descargas: " + estadisticas.getAverage());
        System.out.println("Máximo descargas: " + estadisticas.getMax());
        System.out.println("Mínimo descargas: " + estadisticas.getMin());
    }

    private void listarTop10Libros() {
        libroRepositorio.findTop10ByOrderByDescargasDesc().forEach(System.out::println);
    }

    private void buscarAutorPorNombre() {
        System.out.print("Escribe el nombre del autor que deseas buscar: ");
        String nombre = teclado.nextLine();
        autorRepositorio.findByNombreIgnoreCase(nombre)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No se encontró el autor.")
                );
    }

    private void listarAutoresPorAnioNacimiento() {
        System.out.print("Escribe el año de nacimiento: ");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepositorio.findByFechaDeNacimiento(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores nacidos en el año " + anio);
            } else {
                autores.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingresa un año válido.");
        }
    }
}
