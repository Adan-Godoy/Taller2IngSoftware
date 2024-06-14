package Taller.IngenieriaSoftware.yiskar.entities;

public class Persona {
    /**
     * Nombre de la persona.
     */
    private String nombre;
    /**
     * Edad de la persona.
     */
    private int edad;
    /**
     * Correo de la persona.
     */
    private String email;
    /**
     * Contraseña de la persona.
     */
    private String contrasenia;


    /**
     * Metodo constructor de la clase persona.
     * @param nombre Nombre de la persona.
     * @param edad Edad de la persona.
     * @param email Correo de la persona.
     * @param contrasenia Contraseña de la persona.
     */
    public Persona(String nombre, int edad, String email, String contrasenia) {
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    /**
     * Metodo que retorna el nombre de la persona.
     * @return Nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo que modifica el nombre de la persona.
     * @param nombre Nuevo nombre de la persona.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que obtiene la edad de la persona.
     * @return Edad de la persona.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Metodo que modifica la edad de la persona.
     * @param edad Nueva edad de la persona.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Metodo que retorna el correo de la persona.
     * @return Correo de la persona.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo que modifica el correo de la persona.
     * @param email Nuevo correo de la persona.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo que obtiene la contraseña de la persona.
     * @return contraseña de la persona.
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Metodo que modifica la contraseña de la persona.
     * @param contraseña Nueva constraseña de la persona.
     */
    public void setContrasenia(String contraseña) { this.contrasenia = contraseña;}
}
