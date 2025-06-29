package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE ="https://gutendex.com/books";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosResultados> datosLibros = new ArrayList<DatosResultados>();
    private LibroRepository repositorioL;
    private AutorRepository repositorioA;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository repositoryL, AutorRepository repositoryA) {

        this.repositorioL = repositoryL;
        this.repositorioA = repositoryA;
    }



    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                     2
                     
                     ************
                    1 - Listar libros 
                    2 - Listar autorer
                    3 - Listar libros por idioma
                    4 - Listar autor vivo en determinado año
                    5 - Mostrar libros buscados
                    6 - Mostrar autores buscados
                    7 - Buscar libro por autor
                    8 - Top 10 libros más descargados
                    9 - Estadisticas de descargas de libros
                    
                    

                                  
                    0 - Salir
                    
                    * Escriba un numero de la lista:
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    listarLibros();
                    break;
                case 2:
                    listarAutores();
                    break;
                case 3:
                    listarLibrosPorIdioma();
                    break;
                case 4:
                    listarAutorVivoEnDeterminadoAnio();
                    break;
                case 5:
                    mostrarLibrosBuscados();
                    break;
                case 6:
                    mostrarAutoresBuscados();
                    break;
                case 7:
                    buscarAutorPorLibro();
                    break;
                case 8:
                    top10LibrosMasDescargados();
                    break;
                case 9:
                    estadisticaDeDescargas();
                    break;



                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }



    private DatosResultados getDatosLibro() {

        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + tituloLibro.replace(" ", "+"));
        System.out.println(json);
        DatosResultados datos = conversor.obtenerDatos(json, DatosResultados.class);
        return datos;

    }

    
    private void listarAutores() {

        System.out.println("Ingrese nombre del autor para buscar libros");
        DatosResultados datos = getDatosLibro();


        List<DatosLibro> libros = datos.getResultados();
        for(DatosLibro libro : libros){
            List<DatosAutor> autores = libro.autor();
            for (DatosAutor autore : autores ){
                Optional<Autor> autorExistente = repositorioA.findByNombre(autore.nombre());
                if (!autorExistente.isPresent()){
                    DatosAutor datosAutor = new DatosAutor(autore.nombre(), autore.fechaDeNacimiento(),autore.fechaDeNacimiento());
                    Autor nAutor = new Autor(datosAutor);
                    System.out.println("Autor guardado :" + autore.nombre());
                    try{
                        repositorioA.save(nAutor);
                    }catch (DataIntegrityViolationException e){
                    }
                }else {
                    System.out.println("Autor ya existente :");
                }
            }
        }

    }
    private  void listarLibros() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        DatosResultados datos = getDatosLibro();
        Libro libro2 = new Libro(datos);

        List<DatosLibro> libros = datos.getResultados();

        for (DatosLibro libro : libros) {

                Optional<Libro> libroExistente = repositorioL.findByTitulo(libro.titulo());
                if (!libroExistente.isPresent()) {
                    try {
                        repositorioL.save(libro2);
                        System.out.println("Libro guardado: " + libro.titulo());
                    } catch (DataIntegrityViolationException e) {
                    }
                }else{System.out.println("Libro ya existente.");
                    }

                }
                //datosLibros.add(libro);
                System.out.println(datos);


        }

    private void mostrarLibrosBuscados() {
         libros = repositorioL.findAll();
        libros.forEach(System.out::println);

    }
    private void mostrarAutoresBuscados(){
        autores = repositorioA.findAll();
        autores.forEach(System.out::println);


    }
    private void buscarAutorPorLibro(){
        mostrarLibrosBuscados();
        System.out.println("Ingrese nombre del autor para buscar libros");
        var nombreAutor = teclado.nextLine();
        Optional<Libro> librou = libros.stream()
                .filter(l->l.getAutor().getNombre().toLowerCase().contains(nombreAutor.toLowerCase()))
                .findFirst();
        if(librou.isPresent()){
            var autorEncontrado = librou.get();
            System.out.println("Libro del autor :" + autorEncontrado.getTitulo());
            List<DatosLibro> autors = datosLibros.stream()
                    .flatMap(d-> d.getResultados().stream().limit(1)).collect(Collectors.toList());
            Autor autor = autorEncontrado.getAutor();
            System.out.println("Autor: " + autorEncontrado.getAutor().getNombre());

            autors.forEach(l-> {
                l.autor().forEach(a-> System.out.println(a.nombre()));

            });
  //          repositorioA.save(autor);
        } else {
            System.out.println("Autor inexistente");
        }
    }
    private void listarLibrosPorIdioma (){
        System.out.println("Ingrese el idioma (ejem: en,fr) del libro que desea buscar");
        DatosResultados datos = getDatosLibro();

        List<DatosLibro> idiomas = datos.getResultados();

        for (DatosLibro datosLibro : idiomas) {

            List<String> listaDeIdiomas = datosLibro.idioma();
            String idioma = null;
            if (listaDeIdiomas != null && !listaDeIdiomas.isEmpty()){
             idioma = listaDeIdiomas.get(0);
            }



            Libro libro2 = new Libro(datosLibro);


            Optional<Libro> libroExistente = repositorioL.findByTitulo(datosLibro.titulo());
            if (!libroExistente.isPresent()) {
                try {
                    repositorioL.save(libro2);
                    System.out.println("Libro en idioma pedido ya está guardado: " + libro2.getTitulo() + " " + libro2.getIdioma());
                } catch (DataIntegrityViolationException e) {

                }
            } else {
                System.out.println("Libro ya existente.");
            }
        }
        System.out.println(datos);
    }


    private void listarAutorVivoEnDeterminadoAnio(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el año para buscar autores vivos:");
        int año = scanner.nextInt();
        scanner.nextLine();


        List<Autor> autores = repositorioA.findAll();


        List<Autor> autoresVivos = new ArrayList<>();
        for (Autor autor : autores) {
            Integer fechaDeFallecimiento = autor.getFechaDeFallecimiento();
            if (fechaDeFallecimiento == null || fechaDeFallecimiento > año) {
                autoresVivos.add(autor);
            }
        }


        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos hasta el año " + año);
        } else {
            System.out.println("Autores vivos hasta el año " + año + ":");
            for (Autor autor : autoresVivos) {
                System.out.println(autor.getNombre());
            }
        }

    }
    private void top10LibrosMasDescargados(){
        List<Libro> topLibros = repositorioL.findTop10ByOrderByDescargasDesc();
        topLibros.forEach(l-> System.out.println("--------" +
                "\nLibro: " + l.getTitulo() +
                "\nNumero de Descargas: " + l.getDescargas() +
                "\n _________"));

    }
    private void estadisticaDeDescargas(){
        List<Libro> librosD = repositorioL.findAll();
        Optional<Libro> libroMaxD = librosD.stream().max(Comparator.comparing(Libro::getDescargas));
        Optional<Libro> libroMinD = librosD.stream().min(Comparator.comparing(Libro::getDescargas));
        DoubleSummaryStatistics est = librosD.stream()
                .filter(l-> l.getDescargas()> 0)
                .collect(Collectors.summarizingDouble(Libro::getDescargas));
        System.out.println("\n ________");
        System.out.println("Media de todas las descargas de los libros: " + est.getAverage());
        if (libroMaxD.isPresent()){
            Libro libru = libroMaxD.get();
            System.out.println("\n Max de descargas: " + est.getMax());
            System.out.println("Libro: " + libru.getTitulo() + "   --   Descargas:"+ libru.getDescargas());
        }else {
        }

        if (libroMinD.isPresent()){
            Libro libru = libroMinD.get();
            System.out.println("\n Min de descargas: " + est.getMin());
            System.out.println("Libro: "+ libru.getTitulo() + "   --   Descargas:"+ libru.getDescargas());
        }else {
        }
        System.out.println(" ________");


    }




}

