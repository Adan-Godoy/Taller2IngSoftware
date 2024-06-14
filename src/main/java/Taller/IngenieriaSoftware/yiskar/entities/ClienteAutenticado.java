package Taller.IngenieriaSoftware.yiskar.entities;

import Taller.IngenieriaSoftware.yiskar.interfaces.IObserver;

public class ClienteAutenticado implements IObserver
{
    private Persona cliente;
    private static ClienteAutenticado instancia;
    private ClienteAutenticado()
    {
        this.cliente = null;
    }

    public static ClienteAutenticado getInstancia() {
        if(instancia == null)
        {
            instancia = new ClienteAutenticado();
        }
        return instancia;
    }
    public void setCliente(Persona cliente)
    {
        this.cliente = cliente;
    }

    public int obtenerPuntos()
    {
        return ((Cliente) this.cliente).getPuntos();
    }

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
