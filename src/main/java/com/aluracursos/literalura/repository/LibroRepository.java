package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findTop10ByOrderByDescargasDesc();

}
