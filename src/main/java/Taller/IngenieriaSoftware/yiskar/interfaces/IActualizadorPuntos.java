package Taller.IngenieriaSoftware.yiskar.interfaces;

public interface IActualizadorPuntos
{
    /**
     * Método que añade un observer al actualizador de puntos.
     * @param o Observer a agregar.
     */
    void addObserver(IObserver o);

    /**
     * Método que elimina un observer al actualizador de puntos.
     * @param o Observer a eliminar.
     */
    void delObserver(IObserver o);

    /**
     * Método que notifica una actualización de puntos a los observers.
     * @param puntos Actualizacion de puntos.
     */
    void actualizarPuntos(int puntos);
}
