package Taller.IngenieriaSoftware.yiskar.services;

import Taller.IngenieriaSoftware.yiskar.entities.CreditCard;
import java.util.LinkedList;

public class CreditCardService {

    private static CreditCardService instance;
    private LinkedList<CreditCard> tarjetas;

    private CreditCardService() {
        this.tarjetas = new LinkedList<>();
    }

    public static CreditCardService getInstance() {
        if (instance == null) {
            instance = new CreditCardService();
        }
        return instance;
    }

    public void addCreditCard(String numero, String nombre, String mes, String anio, String codigoSeguridad) {

        for (CreditCard tarjeta : tarjetas) {
            if (tarjeta.getNumero().equals(numero)) {
                return;
            }
        }
        CreditCard nuevaTarjeta = new CreditCard(numero, nombre, mes, anio, codigoSeguridad);
        tarjetas.add(nuevaTarjeta);
    }

    public LinkedList<CreditCard> getTarjetas() {
        return tarjetas;
    }
}
