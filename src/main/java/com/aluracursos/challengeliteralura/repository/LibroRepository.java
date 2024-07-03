package com.aluracursos.challengeliteralura.repository;

import com.aluracursos.challengeliteralura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    List<Libro> findByIdiomasContaining(String idioma);
}
