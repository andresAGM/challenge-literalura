package com.aluracursos.challengeliteralura.repository;

import com.aluracursos.challengeliteralura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long>{
    //buscar autor por nombre
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento >= :anio ORDER BY a.fechaDeNacimiento ASC")
    List<Autor> buscarPorFechaEnDeterminadoAnio(String anio);
}
