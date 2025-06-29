package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.List;
@Entity
@Table(name = "libros" )
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autor autor;
    @Column
    private String idioma;
    private Integer descargas;

    public Libro(){}
    public Libro(DatosLibro datosLibro){

        this.titulo = datosLibro.titulo();
        if (datosLibro.autor() != null){
            List<DatosAutor> datosAutor = datosLibro.autor();
            this.autor = new Autor(datosLibro.autor().get(0));
        }

      //  this.idioma = datosLibro.idioma();
        if (datosLibro.idioma() != null && !datosLibro.idioma().isEmpty()) {
            this.idioma = datosLibro.idioma().get(0);
        } else {
            this.idioma = null;
        }
        this.descargas = datosLibro.descargas();
    }

    public Libro(DatosResultados d) {
        if (d.getResultados() != null && !d.getResultados().isEmpty()) {
            DatosLibro datosLibro = d.getResultados().get(0);
            List<DatosAutor> datosAutor = datosLibro.autor();
            this.titulo = datosLibro.titulo();
            if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
                this.autor = new Autor(datosLibro.autor().get(0));
            } else {
                this.autor = null;
            }
           // this.autor = new Autor(datosLibro.autor().get(0));
          //  this.idioma = datosLibro.idioma();
            if (datosLibro.idioma() != null && !datosLibro.idioma().isEmpty()) {
                this.idioma = datosLibro.idioma().get(0);
            } else {
                this.idioma = null;
            }
            this.descargas = datosLibro.descargas();
        } else {

            this.titulo = null;
            this.autor = null;
            this.idioma = null;
            this.descargas = null;
        }
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

//    public List<String> getIdioma() {
//        return idioma;
//    }
//
//    public void setIdioma(List<String> idioma) {
//        this.idioma = idioma;
//    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idioma=" + idioma +
                ", descargas=" + descargas;
    }
}