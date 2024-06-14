package Taller.IngenieriaSoftware.yiskar.entities;

public class Cliente extends Persona {
    /**
     * Puntos del cliente
     */
    private int puntos;

    /**
     * Metodo constructor de la clase Cliente
     * @param nombre nombre del cliente.
     * @param edad edad del cliente.
     * @param email correo del cliente.
     * @param contrasenia contraseña del cliente.
     * @param puntos puntos del cliente.
     */
    public Cliente(String nombre, int edad, String email, String contrasenia, int puntos) {
        super(nombre,edad,email,contrasenia);
        this.puntos = puntos;

    }

    /**
     * Metodo que retorna los puntos del cliente.
     * @return Puntos del cliente.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Metodo que modifica los puntos del cliente.
     * @param puntos Puntos nuevos del cliente.
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


}


