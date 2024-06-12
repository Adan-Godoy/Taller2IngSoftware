package Taller.IngenieriaSoftware.yiskar.entities;

public class CreditCard {
    private String numero;
    private String nombre;
    private String mes;
    private String anio;
    private String codigoSeguridad;

    public CreditCard(String numero, String nombre, String mes, String anio, String codigoSeguridad) {
        this.numero = numero;
        this.nombre = nombre;
        this.mes = mes;
        this.anio = anio;
        this.codigoSeguridad = codigoSeguridad;
    }

    public String getNumero() {
        return this.numero;
    }

    public String toString() {
        return String.format("%-20s %-20s %s/%s %s",
                nombre,
                numero,
                mes,
                anio,
                codigoSeguridad);
    }
}

