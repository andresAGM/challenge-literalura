package com.aluracursos.challengeliteralura.principal;

import com.aluracursos.challengeliteralura.model.*;
import com.aluracursos.challengeliteralura.repository.AutorRepository;
import com.aluracursos.challengeliteralura.repository.LibroRepository;
import com.aluracursos.challengeliteralura.service.ConsumoApi;
import com.aluracursos.challengeliteralura.service.ConvierteDatos;


import java.util.*;

public class Principal
{
    // Scanner
    private Scanner leer = new Scanner(System.in);
    //consumo api
    private ConsumoApi consumoApi = new ConsumoApi();
    //convertir json a objeto
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    //url base
    private static final String URL_BASE = "https://gutendex.com/books/";
    //Librorepository
    private final LibroRepository libroRepository;
    //repositorio del autor
    private final AutorRepository autorRepository;
    //lista de libros y autores
    List<Libro> libros;
    List<Autor> autores;

    public Principal(LibroRepository repository, AutorRepository autorRepository)
    {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu()
    {
       var opcion = -1;
       while(opcion != 0)
       {
           System.out.println("#----------------------------------------#");
           System.out.println("# LITERALURA - DESAFIO ALURA - LIBROS #");
           System.out.println("#----------------------------------------#");
           System.out.println("#- Elija una opcion: -#");

           //mostramos el menu y luego preguntamos la opcion
           var menu = """
                    #- 1. Buscar libros por titulo -#
                    #- 2. Listar Libros Registrados -#
                    #- 3. Listar Autores Registrados -#
                    #- 4. Listar Autores vivos en un determinado año -#
                    #- 5. Listar Libros por idioma -#
                    #--------------------------------#
                    #- 0. Salir -#
                    #--------------------------------#
                    """;
           System.out.print(menu);
           try {
               opcion = leer.nextInt();
               leer.nextLine();
               switch (opcion)
               {
                   case 1:
                       buscarLibroPorTitulo();
                       break;
                   case 2:
                       listarLibrosRegistrados();
                       break;
                   case 3:
                       listarAutoresRegistrados();
                       break;
                   case 4:
                       listarAutoresVivosEnUnDeterminadoAnio();
                       break;
                   case 5:
                       listarLibrosPorIdioma();
                       break;
                   case 0:
                       System.out.println("Saliendo del programa");
                       break;
                   default:
                       System.out.println("Opcion no valida");
                       break;
               }
           }catch (InputMismatchException e)
           {
               System.out.println("Valor no valido");
               leer.nextLine();
           }
       }
    }

    //Buscar libro por titulo
    public void buscarLibroPorTitulo()
    {
        System.out.println("Ingrese el titulo del libro a buscar: ");
        var titulo = leer.nextLine();
        var url = URL_BASE + "?search=" + titulo.replace(" ", "+");
        String json = "";
        //hacemos la peticion a la api
        try{
            json = consumoApi.obtenerDatos(url);
        }catch (Exception e){
            System.out.println("Error al buscar el libro");
            return;
        }
        //convertimos los datos
        var datos = convierteDatos.obtenerDatos(json, Datos.class);
        var libro = datos.resultados().stream().findFirst();
        if(libro.isPresent())
        {
            System.out.println("#------------------#\n" + "Libro encontrado: \n" + "#------------------#\n" + "TITULO:" + libro.get().titulo() + "\n" +
                    "AUTORES:" + libro.get().autores().get(0).nombre() + "," + libro.get().autores().get(0).fechaDeNacimiento() + "\n" +
                    "IDIOMAS:" + libro.get().idiomas().toString() + "\n" +
                    "NUMERO DE DESCARGAS:" + libro.get().numeroDeDescargas());

            //verificamos si el autor existe en la base de datos o si no lo guardamos
            Optional<Autor> autor = autorRepository.findByNombre(libro.get().autores().get(0).nombre());
            if(autor.isPresent())
            {
                //Guardar libro en la base de datos
                Libro libroNuevo = new Libro(new DatosLibro(libro.get().titulo(), libro.get().autores(), libro.get().idiomas().subList(0, libro.get().idiomas().size()), libro.get().numeroDeDescargas()));
                crearLibro(libroNuevo, autor.get(), libro.get().autores().get(0).nombre());
            }else
            {
                //creamos un nuevo autor
                Autor autorNuevo = new Autor(new DatosAutor(libro.get().autores().get(0).nombre(), libro.get().autores().get(0).fechaDeNacimiento()));
                autorRepository.save(autorNuevo);
                //Guardar libro en la base de datos conservando la relacion libro autor con el id autor
                Libro libroNuevo = new Libro(new DatosLibro(libro.get().titulo(), libro.get().autores(), libro.get().idiomas(), libro.get().numeroDeDescargas()));
                crearLibro(libroNuevo, autorNuevo, autorNuevo.getNombre());
            }
        }else{
            System.out.println("#------------------#");
            System.out.println("Libro no encontrado");
            System.out.println("#------------------#");
        }
    }

    private void crearLibro(Libro libro, Autor autor, String nombreAutor)
    {
        Libro libroNuevo = new Libro();
        libroNuevo.setTitulo(libro.getTitulo());
        libroNuevo.setAutores(nombreAutor);
        libroNuevo.setIdiomas(libro.getIdiomas());
        libroNuevo.setNumeroDeDescargas(libro.getNumeroDeDescargas());
        libroNuevo.setAutor(autor);
        libroRepository.save(libroNuevo);
    }

    private void listarLibrosRegistrados()
    {
        libros = libroRepository.findAll();
        if(libros.isEmpty())
        {
            System.out.println("No hay libros registrados");
        }else
        {
            System.out.println("#------------------#");
            System.out.println("Libros Registrados");
            System.out.println("#------------------#");
            libros.forEach(libro -> {
                System.out.println(libro.getTitulo());
            });
        }
    }

    private void listarAutoresRegistrados()
    {
        autores = autorRepository.findAll();
        if(autores.isEmpty())
        {
            System.out.println("No hay autores registrados");
        }else
        {
            System.out.println("#------------------#");
            System.out.println("Autores Registrados");
            System.out.println("#------------------#");
            autores.forEach(autor -> {
                System.out.println(autor.getNombre() + " - " + autor.getFechaDeNacimiento());
            });
        }
    }

    private void listarAutoresVivosEnUnDeterminadoAnio()
    {
        System.out.println("Ingrese el año: ");
        var anio = leer.nextLine();
        autores = autorRepository.buscarPorFechaEnDeterminadoAnio(anio);
        if(autores.isEmpty())
        {
            System.out.println("No hay autores vivos en el año " + anio);
        }else
        {
            System.out.println("#-------------------------#");
            System.out.println("Autores vivos en el año " + anio);
            System.out.println("#-------------------------#");
            autores.forEach(autor -> {
                System.out.println(autor.getNombre() + " - " + autor.getFechaDeNacimiento());
            });
        }
    }

    private void listarLibrosPorIdioma()4
    {
        System.out.println("Ingrese el idioma: ");
        System.out.print("""
                en -Ingles
                es -Español
                """);
        var idioma = leer.nextLine();
        //idioma = "["+ idioma +"]";
        libros = libroRepository.findByIdiomasContaining(idioma);
        if(libros.isEmpty())
        {
            System.out.println("No hay libros en el idioma " + idioma);
        }else
        {
            System.out.println("#------------------#");
            System.out.println("Libros en el idioma " + idioma);
            System.out.println("#------------------#");
            libros.forEach(libro -> {
                System.out.println(libro.getTitulo());
            });
        }
    }
}
