package Taller.IngenieriaSoftware.yiskar.entities;

import Taller.IngenieriaSoftware.yiskar.interfaces.IObserver;

public class ClienteAutenticado implements IObserver
{
    /**
     * Método que almacena el objeto perteneciente al cliente logueado.
     */
    private Persona cliente;
    /**
     * Instancia de la clase.
     */
    private static ClienteAutenticado instancia;

    /**
     * Constructor de la clase.
     */
    private ClienteAutenticado()
    {
        this.cliente = null;
    }

    /**
     * Método que retorna la instancia de la clase; si esta es nula, crea una y la devuelve.
     * @return instancia de la clase.
     */
    public static ClienteAutenticado getInstancia() {
        if(instancia == null)
        {
            instancia = new ClienteAutenticado();
        }
        return instancia;
    }

    /**
     * Método que le asigna un objeto Cliente a la instancia.
     * @param cliente Objeto de cliente.
     */
    public void setCliente(Persona cliente)
    {
        this.cliente = cliente;
    }

    /**
     * Método que retorna los puntos del cliente logueado.
     * @return Puntos del cliente logueado.
     */
    public int obtenerPuntos()
    {
        return ((Cliente) this.cliente).getPuntos();
    }

    /**
     * Método que retorna el correo el cliente logueado.
     * @return Correo del cliente logueado.
     */
    public String obtenerCorreo()
    {
        return this.cliente.getEmail();
    }

    @Override
    public void actualizarPuntos(int puntos)
    {
        ((Cliente) this.cliente).setPuntos(puntos);
    }
}
