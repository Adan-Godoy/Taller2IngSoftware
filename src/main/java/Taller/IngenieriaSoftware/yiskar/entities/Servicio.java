package Taller.IngenieriaSoftware.yiskar.entities;

import java.util.Objects;

public class Servicio {

    /**
     * Nombre del servicio.
     */
    private String nombre;
    /**
     * Precio del servicio.
     */
    private int precio;


    /**
     * Metodo constructor del objeto servicio.
     * @param nombre Nombre del servicio.
     * @param precio Precio del servicio.
     */
    public Servicio(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;

    }

    /**
     * Metodo que retorna el nombre del servicio.
     * @return Nombre del servicio.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo que modifica el nombre del servicio.
     * @param nombre Nuevo nombre del servicio.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que retorna el precio del servicio.
     * @return Precio del servicio.
     */
    public int getPrecio() {
        return precio;
    }

    /**
     * Metodo que modifica el precio del servicio.
     * @param precio Nuevo precio del servicio.
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Float.compare(precio, servicio.precio) == 0 && Objects.equals(nombre, servicio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, precio);
    }
}
