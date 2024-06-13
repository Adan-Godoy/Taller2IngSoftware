package Taller.IngenieriaSoftware.yiskar.entities;

public class Cliente extends Persona {
    private int puntos;
    public Cliente(String nombre, int edad, String email, String contraseña, int puntos) {
        super(nombre,edad,email,contraseña);
        this.puntos = puntos;

    }
    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


}


