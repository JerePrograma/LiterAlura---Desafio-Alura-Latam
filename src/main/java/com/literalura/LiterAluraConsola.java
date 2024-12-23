package com.literalura;

import com.literalura.principal.Principal;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraConsola implements CommandLineRunner {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LiterAluraConsola(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiterAluraConsola.class, args);
    }

    @Override
    public void run(String... args) {
        Principal principal = new Principal(libroRepository, autorRepository);
        principal.muestraElMenu();
    }
}
