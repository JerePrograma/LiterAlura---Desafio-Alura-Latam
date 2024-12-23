package com.literalura.repository;

import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Encontrar un autor por nombre (case insensitive)
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    // Encontrar autores vivos en un año específico
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeMuerte IS NULL OR a.fechaDeMuerte > :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);


    List<Autor> findByFechaDeNacimiento(int anio);
}
