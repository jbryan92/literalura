package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResultados(
        @JsonAlias("results")List<DatosLibro> resultados
        ) {
        public List<DatosLibro> getResultados() {
                return resultados;
        }

        @Override
        public String toString() {
                return resultados.toString();
        }
}
