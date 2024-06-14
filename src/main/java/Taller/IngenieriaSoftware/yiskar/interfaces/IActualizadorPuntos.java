package Taller.IngenieriaSoftware.yiskar.interfaces;

public interface IActualizadorPuntos
{
    void addObserver(IObserver o);

    void delObserver(IObserver o);

    void actualizarPuntos(int puntos);
}
