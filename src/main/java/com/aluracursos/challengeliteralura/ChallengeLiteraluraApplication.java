package com.aluracursos.challengeliteralura;

import com.aluracursos.challengeliteralura.repository.AutorRepository;
import com.aluracursos.challengeliteralura.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aluracursos.challengeliteralura.principal.Principal;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {
    //Librorepository
    private final LibroRepository libroRepository;
    //Autorrepository
    private final AutorRepository autorRepository;

    public ChallengeLiteraluraApplication(LibroRepository libroRepository, AutorRepository autorRepository)
    {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChallengeLiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(libroRepository, autorRepository );
        principal.mostrarMenu();
    }
}
