package Taller.IngenieriaSoftware.yiskar.services;
import Taller.IngenieriaSoftware.yiskar.controllers.comprarGiftCardController;
import Taller.IngenieriaSoftware.yiskar.entities.ClienteAutenticado;
import Taller.IngenieriaSoftware.yiskar.interfaces.IObserver;
import Taller.IngenieriaSoftware.yiskar.interfaces.IPagarService;
import Taller.IngenieriaSoftware.yiskar.repository.PersonaRepository;
import Taller.IngenieriaSoftware.yiskar.util.ActualizadorPuntos;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.scene.control.Alert;

public class PagarPuntosService implements IPagarService
{
    private float montoTotal;
    private int puntosCliente;
    public PagarPuntosService(float montoTotal)
    {
        ClienteAutenticado clienteAutenticado = ClienteAutenticado.getInstancia();
        this.montoTotal = montoTotal;
        this.puntosCliente = clienteAutenticado.obtenerPuntos();
    }
    @Override
    public boolean realizarPago()
    {
        if(montoTotal>puntosCliente)
        {
            AlertBox.mostrarError("Puntos insuficientes.","Error", Alert.AlertType.ERROR);
            return false;
        }
        int puntosFinales = puntosCliente - (int)montoTotal;
        ActualizadorPuntos actualizadorPuntos = new ActualizadorPuntos();
        actualizadorPuntos.addObserver(PersonaRepository.getInstance());
        actualizadorPuntos.addObserver(ClienteAutenticado.getInstancia());
        actualizadorPuntos.actualizarPuntos(puntosFinales);
        montoTotal = puntosFinales;
        this.puntosCliente = puntosFinales;
        return true;
    }

    public int obtenerPuntos(){return puntosCliente;}


}
