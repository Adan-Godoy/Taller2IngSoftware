package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.entities.Servicios;
import Taller.IngenieriaSoftware.yiskar.services.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

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
        if (selectedServicio != null) {
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
        } else {
            // Mostrar un mensaje de alerta si no se selecciona ningún servicio
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No hay servicio seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un servicio para agregar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicios selectedServicio = tablaServiciosAgre.getSelectionModel().getSelectedItem();
        if (selectedServicio != null) {
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
        } else {
            // Mostrar un mensaje de alerta si no se selecciona ningún servicio
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No hay servicio seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un servicio para eliminar.");
            alert.showAndWait();
        }
    }
    @FXML
    private void realizarDescuento(ActionEvent event) {


        // Diálogo para solicitar el número de tarjeta de crédito
        TextInputDialog numeroDialog = new TextInputDialog();
        numeroDialog.setTitle("Datos de Tarjeta de Crédito");
        numeroDialog.setHeaderText("Ingrese el número de tarjeta de crédito:");
        numeroDialog.setContentText("Número de tarjeta:");

        // Obtener el número de tarjeta de crédito
        Optional<String> numeroResult = numeroDialog.showAndWait();
        if (numeroResult.isPresent()) {
            String numeroTarjeta = numeroResult.get();

            // Diálogo para solicitar la fecha de vencimiento
            TextInputDialog fechaDialog = new TextInputDialog();
            fechaDialog.setTitle("Datos de Tarjeta de Crédito");
            fechaDialog.setHeaderText("Ingrese la fecha de vencimiento (MM/AAAA):");
            fechaDialog.setContentText("Fecha de vencimiento:");

            // Obtener la fecha de vencimiento
            Optional<String> fechaResult = fechaDialog.showAndWait();
            if (fechaResult.isPresent()) {
                String mesAnioVencimiento = fechaResult.get();

                // Validar que la fecha de vencimiento esté en el formato MM/AAAA
                if (!mesAnioVencimiento.matches("\\d{2}/\\d{4}")) {
                    mostrarAlerta("Formato incorrecto", "El formato de la fecha de vencimiento debe ser MM/AAAA");
                    return;
                }

                // Obtener mes y año de vencimiento por separado
                String[] fechaVencimiento = mesAnioVencimiento.split("/");
                int mesVencimiento = Integer.parseInt(fechaVencimiento[0]);
                int anioVencimiento = Integer.parseInt(fechaVencimiento[1]);

                // Diálogo para solicitar el código de seguridad
                TextInputDialog codigoDialog = new TextInputDialog();
                codigoDialog.setTitle("Datos de Tarjeta de Crédito");
                codigoDialog.setHeaderText("Ingrese el código de seguridad:");
                codigoDialog.setContentText("Código de seguridad:");

                // Obtener el código de seguridad
                Optional<String> codigoResult = codigoDialog.showAndWait();
                if (codigoResult.isPresent()) {
                    String codigoSeguridadStr = codigoResult.get();

                    // Validar que el código de seguridad sea un número entero
                    int codigoSeguridad;
                    try {
                        codigoSeguridad = Integer.parseInt(codigoSeguridadStr);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Formato incorrecto", "El código de seguridad debe ser un número entero.");
                        return;
                    }

                    // Llama al servicio para validar la tarjeta de crédito
                    try {
                        boolean tarjetaValida = ApiService.getInstance().verificarTarjeta(numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

                        if (tarjetaValida) {
                            // Si la tarjeta es válida, realizar el cargo
                            float montoTotal = listaServiciosAgre.get(listaServiciosAgre.size() - 1).getPrecio();  // Obtener el monto total correcto

                            boolean cargoRealizado = ApiService.getInstance().realizarCargo(numeroTarjeta, montoTotal, "Descripción del cargo", mesVencimiento, anioVencimiento, codigoSeguridad);

                            if (cargoRealizado) {
                                // Si el cargo se realiza con éxito, limpiar la lista de servicios agregados
                                listaServiciosAgre.clear();

                                // Añadir el servicio 'Total' nuevamente con precio 0 después de limpiar la lista
                                Servicios total = new Servicios("Total", 0);
                                listaServiciosAgre.add(total);

                                // Actualizar las tablas
                                tablaServiciosDisp.refresh();
                                tablaServiciosAgre.refresh();

                                // Mostrar mensaje de éxito al usuario
                                mostrarAlerta("Cargo realizado", "Se ha realizado el cargo correctamente.");
                            } else {
                                // Mostrar mensaje de error si el cargo no se realiza
                                mostrarAlerta("Error en el cargo", "No se pudo realizar el cargo en la tarjeta.");
                            }
                        } else {
                            // Mostrar mensaje de alerta si la tarjeta no es válida
                            mostrarAlerta("Tarjeta inválida", "La tarjeta ingresada no es válida.");
                        }
                    } catch (Exception e) {
                        // Capturar cualquier excepción y mostrar mensaje de error
                        mostrarAlerta("Error", "Error al intentar realizar el cargo: " + e.getMessage());
                    }
                }
            }
        }

    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}


