package com.literalura.repository;

import com.literalura.model.Libro;
import com.literalura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Encontrar un libro por título (case insensitive)
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    // Encontrar libros por idioma
    List<Libro> findByIdioma(Language idioma);

    // Listar los 10 libros más descargados
    List<Libro> findTop10ByOrderByDescargasDesc();
}
