package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.entities.Servicios;
import Taller.IngenieriaSoftware.yiskar.services.PagarTarjetaService;
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

public class comprarGiftCardController {

    @FXML
    private TableView<Servicios> tablaServiciosDisp;
    @FXML
    private TableView<Servicios> tablaServiciosAgre;

    @FXML
    private TableColumn<Servicios, String> colNombreDisp;
    @FXML
    private TableColumn<Servicios, Float> colPrecioDisp;

    @FXML
    private TableColumn<Servicios, String> colNombreAgre;
    @FXML
    private TableColumn<Servicios, Float> colPrecioAgre;

    @FXML
    private Label puntosTxt;
    private ObservableList<Servicios> listaServiciosDisp;
    private ObservableList<Servicios> listaServiciosAgre;

    public void initialize() {
        tablaServiciosDisp.setPlaceholder(new Label("No hay servicios registrados"));
        listaServiciosDisp = FXCollections.observableArrayList();

        this.colNombreDisp.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioDisp.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tablaServiciosDisp.setItems(listaServiciosDisp);
        cargarServicio();

        tablaServiciosAgre.setPlaceholder(new Label("No hay servicios agregados"));
        listaServiciosAgre = FXCollections.observableArrayList();

        this.colNombreAgre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioAgre.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tablaServiciosAgre.setItems(listaServiciosAgre);

        Servicios total = new Servicios("Total", 0);
        listaServiciosAgre.add(total);
    }

    private void cargarServicio() {
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\Servicios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    String nombre = datos[0];
                    String precioString = datos[1];
                    try {
                        float precio = Float.parseFloat(precioString);
                        Servicios servicio = new Servicios(nombre, precio);
                        listaServiciosDisp.add(servicio);
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El valor de precio no es un número válido: " + precioString);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarServicio(ActionEvent event) {
        Servicios selectedServicio = tablaServiciosDisp.getSelectionModel().getSelectedItem();
        if (selectedServicio != null && !selectedServicio.getNombre().equals("Total")) {
            // Eliminar el total temporalmente
            Servicios totalServicio = listaServiciosAgre.remove(listaServiciosAgre.size() - 1);


            // Añadir el servicio seleccionado a la tabla de servicios agregados
            listaServiciosAgre.add(selectedServicio);

            tablaServiciosDisp.getSelectionModel().clearSelection();

            // Actualizar el total
            float nuevoTotal = totalServicio.getPrecio() + selectedServicio.getPrecio();
            totalServicio.setPrecio(nuevoTotal);

            // Añadir el total nuevamente al final de la lista
            listaServiciosAgre.add(totalServicio);

            // Refrescar la tabla para mostrar el total actualizado
            tablaServiciosAgre.refresh();

            // Eliminar el servicio seleccionado de la tabla de servicios disponibles
            listaServiciosDisp.remove(selectedServicio);
        } else
        {
            AlertBox.mostrarError("Por favor, seleccione un servicio para agregar.", "No hay servicio seleccionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicios selectedServicio = tablaServiciosAgre.getSelectionModel().getSelectedItem();
        if (selectedServicio != null && !selectedServicio.getNombre().equals("Total")) {
            // Añadir el servicio eliminado de nuevo a la tabla de servicios disponibles
            Servicios totalServicio = listaServiciosAgre.remove(listaServiciosAgre.size() - 1);
            listaServiciosDisp.add(selectedServicio);
            tablaServiciosAgre.getSelectionModel().clearSelection();

            float nuevoTotal = totalServicio.getPrecio() - selectedServicio.getPrecio();
            totalServicio.setPrecio(nuevoTotal);

            // Añadir el total nuevamente al final de la lista
            listaServiciosAgre.add(totalServicio);

            // Eliminar el servicio seleccionado de la tabla de servicios agregados
            listaServiciosAgre.remove(selectedServicio);

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

        float montoTotal = listaServiciosAgre.get(listaServiciosAgre.size() - 1).getPrecio();  // Obtener el monto total correcto

        PagarTarjetaService pago = new PagarTarjetaService(montoTotal);

        if (pago.realizarPago())
        {
            // Si el cargo se realiza con éxito, limpiar la lista de servicios agregados
            listaServiciosAgre.clear();

            // Añadir el servicio 'Total' nuevamente con precio 0 después de limpiar la lista
            Servicios total = new Servicios("Total", 0);
            listaServiciosAgre.add(total);

            // Actualizar las tablas
            tablaServiciosDisp.refresh();
            tablaServiciosAgre.refresh();

            // Mostrar mensaje de éxito al usuario
            AlertBox.mostrarError("Cargo realizado", "Se ha realizado el cargo correctamente.", Alert.AlertType.CONFIRMATION);
        } else {
            // Mostrar mensaje de error si el cargo no se realiza
            AlertBox.mostrarError("Error en el cargo", "No se pudo realizar el cargo en la tarjeta.", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void pagoPuntos(ActionEvent event)
    {
        float montoTotal = listaServiciosAgre.get(listaServiciosAgre.size() - 1).getPrecio();  // Obtener el monto total correcto


    }

}


