package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.entities.ClienteAutenticado;
import Taller.IngenieriaSoftware.yiskar.entities.Servicio;
import Taller.IngenieriaSoftware.yiskar.repository.PersonaRepository;
import Taller.IngenieriaSoftware.yiskar.repository.ServiciosRepository;
import Taller.IngenieriaSoftware.yiskar.services.PagarPuntosService;
import Taller.IngenieriaSoftware.yiskar.services.PagarTarjetaService;
import Taller.IngenieriaSoftware.yiskar.util.ActualizadorPuntos;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class comprarGiftCardController{

    @FXML
    private TableView<Servicio> tablaServiciosDisp;
    @FXML
    private TableView<Servicio> tablaServiciosAgre;

    @FXML
    private TableColumn<Servicio, String> colNombreDisp;
    @FXML
    private TableColumn<Servicio, Float> colPrecioDisp;

    @FXML
    private TableColumn<Servicio, String> colNombreAgre;
    @FXML
    private TableColumn<Servicio, Float> colPrecioAgre;

    @FXML
    private Label puntosTxt;
    private ObservableList<Servicio> listaServicioDisp;
    private ObservableList<Servicio> listaServicioAgre;

    public void initialize() {
        tablaServiciosDisp.setPlaceholder(new Label("No hay servicios registrados"));
        listaServicioDisp = FXCollections.observableArrayList();

        this.colNombreDisp.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioDisp.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tablaServiciosDisp.setItems(listaServicioDisp);
        cargarServicio();

        tablaServiciosAgre.setPlaceholder(new Label("No hay servicios agregados"));
        listaServicioAgre = FXCollections.observableArrayList();

        this.colNombreAgre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioAgre.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tablaServiciosAgre.setItems(listaServicioAgre);

        Servicio total = new Servicio("Total", 0);
        listaServicioAgre.add(total);

        ClienteAutenticado clienteAutenticado = ClienteAutenticado.getInstancia();
        puntosTxt.setText("Puntos disponibles: "+clienteAutenticado.obtenerPuntos());
    }

    private void cargarServicio() {
        listaServicioDisp.clear();

        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        Servicio[] servicios = serviciosRepository.obtenerServicios();
        if (servicios == null) {
            AlertBox.mostrarError("No hay servicios registrados", "No hay datos", Alert.AlertType.WARNING);
            return;
        }
        for (Servicio servicio : servicios) {
            if (servicio == null) {
                return;
            }
            listaServicioDisp.add(servicio);
        }
        tablaServiciosDisp.refresh();
    }

    @FXML
    private void agregarServicio(ActionEvent event) {
        Servicio selectedServicio = tablaServiciosDisp.getSelectionModel().getSelectedItem();
        if (selectedServicio != null && !selectedServicio.getNombre().equals("Total")) {
            // Eliminar el total temporalmente
            Servicio totalServicio = listaServicioAgre.remove(listaServicioAgre.size() - 1);


            // Añadir el servicio seleccionado a la tabla de servicios agregados
            listaServicioAgre.add(selectedServicio);

            tablaServiciosDisp.getSelectionModel().clearSelection();

            // Actualizar el total
            int nuevoTotal = totalServicio.getPrecio() + selectedServicio.getPrecio();
            totalServicio.setPrecio(nuevoTotal);

            // Añadir el total nuevamente al final de la lista
            listaServicioAgre.add(totalServicio);

            // Refrescar la tabla para mostrar el total actualizado
            tablaServiciosAgre.refresh();

            // Eliminar el servicio seleccionado de la tabla de servicios disponibles
            listaServicioDisp.remove(selectedServicio);
        } else
        {
            AlertBox.mostrarError("Por favor, seleccione un servicio para agregar.", "No hay servicio seleccionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicio selectedServicio = tablaServiciosAgre.getSelectionModel().getSelectedItem();
        if (selectedServicio != null && !selectedServicio.getNombre().equals("Total")) {
            // Añadir el servicio eliminado de nuevo a la tabla de servicios disponibles
            Servicio totalServicio = listaServicioAgre.remove(listaServicioAgre.size() - 1);
            listaServicioDisp.add(selectedServicio);
            tablaServiciosAgre.getSelectionModel().clearSelection();

            int nuevoTotal = totalServicio.getPrecio() - selectedServicio.getPrecio();
            totalServicio.setPrecio(nuevoTotal);

            // Añadir el total nuevamente al final de la lista
            listaServicioAgre.add(totalServicio);

            // Eliminar el servicio seleccionado de la tabla de servicios agregados
            listaServicioAgre.remove(selectedServicio);

            // Refrescar las tablas
            tablaServiciosDisp.refresh();
            tablaServiciosAgre.refresh();
        } else
        {
            AlertBox.mostrarError("Por favor, seleccione un servicio para eliminar.", "No hay servicio seleccionado", Alert.AlertType.WARNING);
        }
    }
    @FXML
    private void pagoTarjeta(ActionEvent event) {

        float montoTotal = listaServicioAgre.get(listaServicioAgre.size() - 1).getPrecio();  // Obtener el monto total correcto

        PagarTarjetaService pago = new PagarTarjetaService(montoTotal);

        if (pago.realizarPago())
        {
            // Si el cargo se realiza con éxito, limpiar la lista de servicios agregados
            listaServicioAgre.clear();

            // Añadir el servicio 'Total' nuevamente con precio 0 después de limpiar la lista
            Servicio total = new Servicio("Total", 0);
            listaServicioAgre.add(total);

            // Actualizar las tablas
            tablaServiciosDisp.refresh();
            tablaServiciosAgre.refresh();

            int puntosObtenidos = Math.round(montoTotal*20/100);
            ClienteAutenticado clienteAutenticado = ClienteAutenticado.getInstancia();
            int puntosCliente = clienteAutenticado.obtenerPuntos();
            int puntosFinales = puntosCliente+puntosObtenidos;

            ActualizadorPuntos actualizadorPuntos = new ActualizadorPuntos();
            actualizadorPuntos.addObserver(PersonaRepository.getInstance());
            actualizadorPuntos.addObserver(ClienteAutenticado.getInstancia());
            actualizadorPuntos.actualizarPuntos(puntosFinales);
            puntosTxt.setText("Puntos disponibles: "+puntosFinales);

            // Mostrar mensaje de éxito al usuario
            AlertBox.mostrarError("Cargo realizado", "Se ha realizado el cargo correctamente.", Alert.AlertType.CONFIRMATION);
            cargarServicio();
        } else {
            // Mostrar mensaje de error si el cargo no se realiza
            AlertBox.mostrarError("Error en el cargo", "No se pudo realizar el cargo en la tarjeta.", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void pagoPuntos(ActionEvent event)
    {
        float montoTotal = listaServicioAgre.get(listaServicioAgre.size() - 1).getPrecio();  // Obtener el monto total correcto
        PagarPuntosService pagarPuntosService = new PagarPuntosService(montoTotal);
        if(pagarPuntosService.realizarPago())
        {
            // Si el cargo se realiza con éxito, limpiar la lista de servicios agregados
            listaServicioAgre.clear();

            // Añadir el servicio 'Total' nuevamente con precio 0 después de limpiar la lista
            Servicio total = new Servicio("Total", 0);
            listaServicioAgre.add(total);

            // Actualizar las tablas
            tablaServiciosDisp.refresh();
            tablaServiciosAgre.refresh();
            puntosTxt.setText("Puntos disponibles: "+pagarPuntosService.obtenerPuntos());

            AlertBox.mostrarError("Cargo realizado", "Se ha realizado el cargo correctamente.", Alert.AlertType.CONFIRMATION);
            cargarServicio();

        }


    }

}


