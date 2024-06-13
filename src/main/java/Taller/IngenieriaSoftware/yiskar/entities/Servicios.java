package Taller.IngenieriaSoftware.yiskar.entities;

import javafx.scene.image.Image;

import java.util.Objects;

public class Servicios {

    private String nombre;
    private float precio;


    public Servicios(String nombre, float precio) {
        this.nombre = nombre;
        this.precio = precio;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicios servicios = (Servicios) o;
        return Float.compare(precio, servicios.precio) == 0 && Objects.equals(nombre, servicios.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, precio);
    }
}
