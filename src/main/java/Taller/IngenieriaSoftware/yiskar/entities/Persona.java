package Taller.IngenieriaSoftware.yiskar.entities;

public class Persona {
    private String nombre;
    private int edad;
    private String email;
    private String contraseña;


    public Persona(String nombre, int edad, String email, String contraseña) {
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
