package com.aluracursos.challengeliteralura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaDeNacimiento;

    public Autor(){}

    public Autor(DatosAutor datosAutor)
    {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
    }

    public String toString()
    {
        return "Nombre: " + nombre + "\n" +
                "Fecha de nacimiento: " + fechaDeNacimiento + "\n";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}
