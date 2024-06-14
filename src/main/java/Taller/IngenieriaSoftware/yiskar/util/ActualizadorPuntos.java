package Taller.IngenieriaSoftware.yiskar.util;

import Taller.IngenieriaSoftware.yiskar.interfaces.IActualizadorPuntos;
import Taller.IngenieriaSoftware.yiskar.interfaces.IObserver;

import java.util.*;

public class ActualizadorPuntos implements IActualizadorPuntos
{
    /**
     * Variable que almacena los observers a actualizar.
     */
    Set<IObserver> observerSet = new HashSet<>();
    @Override
    public void addObserver(IObserver o)
    {
        observerSet.add(o);
    }

    @Override
    public void delObserver(IObserver o)
    {
        observerSet.remove(o);
    }

    @Override
    public void actualizarPuntos(int puntos)
    {
        for(IObserver observer : observerSet)
        {
            observer.actualizarPuntos(puntos);
        }
    }
}
