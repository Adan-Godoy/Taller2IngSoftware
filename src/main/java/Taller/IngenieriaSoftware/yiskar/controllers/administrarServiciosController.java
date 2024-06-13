package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.entities.Servicios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class administrarServiciosController {

    @FXML
    private TableView<Servicios> TablaServicios;
    @FXML
    private TableColumn<Servicios, String> colNombre;
    @FXML
    private TableColumn<Servicios, Float> colPrecio;

    private ObservableList<Servicios> listaServicios;

    private Stage stage;

    @FXML
    public void initialize() {
        TablaServicios.setPlaceholder(new Label("No hay servicios registrados"));
        listaServicios = FXCollections.observableArrayList();

        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TablaServicios.setItems(listaServicios);
        cargarServicio();
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
                        listaServicios.add(servicio);
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El valor de precio no es un número válido: " + precioString);
                        // Aquí puedes manejar el error según tu lógica de negocio, por ejemplo, ignorar la línea o notificar al usuario.
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarServicio(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Servicio");
        dialog.setHeaderText("Ingrese el nombre del servicio");
        dialog.setContentText("Nombre:");
        Optional<String> nombreResult = dialog.showAndWait();

        nombreResult.ifPresent(nombre -> {
            if (nombre.trim().isEmpty()) {
                mostrarAlertaError("Error al agregar servicio", "El nombre del servicio no puede estar vacío.");
                return;
            }
            if (servicioExiste(nombre)) {
                mostrarAlertaError("Error al agregar servicio", "Este nombre de servicio ya existe.");
                return;
            }

            TextInputDialog precioDialog = new TextInputDialog();
            precioDialog.setTitle("Agregar Servicio");
            precioDialog.setHeaderText("Ingrese el precio del servicio");
            precioDialog.setContentText("Precio:");
            Optional<String> precioResult = precioDialog.showAndWait();

            precioResult.ifPresent(precioStr -> {
                if (precioStr.trim().isEmpty()) {
                    mostrarAlertaError("Error al agregar servicio", "El precio no puede estar vacío.");
                    return;
                }
                try {
                    float precio = Float.parseFloat(precioStr);
                    if (precio < 300) {
                        mostrarAlertaError("Error al agregar servicio", "El precio debe ser al menos 300 pesos chilenos.");
                        return;
                    }
                    Servicios nuevoServicio = new Servicios(nombre, precio);
                    listaServicios.add(nuevoServicio);
                } catch (NumberFormatException e) {
                    mostrarAlertaError("Error al agregar servicio", "El precio debe ser un número válido.");
                }
            });
        });
    }

    @FXML
    private void modificarServicio(ActionEvent event) {
        Servicios servicioSeleccionado = TablaServicios.getSelectionModel().getSelectedItem();

        if (servicioSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(servicioSeleccionado.getNombre());
            dialog.setTitle("Modificar Servicio");
            dialog.setHeaderText("Modificar nombre del servicio");
            dialog.setContentText("Nombre:");
            Optional<String> nombreResult = dialog.showAndWait();

            nombreResult.ifPresent(nombre -> {
                if (nombre.trim().isEmpty()) {
                    mostrarAlertaError("Error al modificar servicio", "El nombre del servicio no puede estar vacío.");
                    return;
                }
                if (!nombre.equalsIgnoreCase(servicioSeleccionado.getNombre()) && servicioExiste(nombre)) {
                    mostrarAlertaError("Error al modificar servicio", "Este nombre de servicio ya existe.");
                    return;
                }

                TextInputDialog precioDialog = new TextInputDialog(String.valueOf(servicioSeleccionado.getPrecio()));
                precioDialog.setTitle("Modificar Servicio");
                precioDialog.setHeaderText("Modificar precio del servicio");
                precioDialog.setContentText("Precio:");
                Optional<String> precioResult = precioDialog.showAndWait();

                precioResult.ifPresent(precioStr -> {
                    if (precioStr.trim().isEmpty()) {
                        mostrarAlertaError("Error al modificar servicio", "El precio no puede estar vacío.");
                        return;
                    }
                    try {
                        float precio = Float.parseFloat(precioStr);
                        if (precio < 300) {
                            mostrarAlertaError("Error al modificar servicio", "El precio debe ser al menos 300 pesos chilenos.");
                            return;
                        }
                        servicioSeleccionado.setNombre(nombre);
                        servicioSeleccionado.setPrecio(precio);
                        TablaServicios.refresh(); // Actualizar la tabla
                    } catch (NumberFormatException e) {
                        mostrarAlertaError("Error al modificar servicio", "El precio debe ser un número válido.");
                    }
                });
            });
        } else {
            mostrarAlertaWarning("Seleccionar Servicio", "Ningún servicio seleccionado", "Por favor, seleccione un servicio para modificar.");
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicios servicioSeleccionado = TablaServicios.getSelectionModel().getSelectedItem();

        if (servicioSeleccionado != null) {
            // Confirmación doble para eliminar servicio
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro que desea eliminar este servicio?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                listaServicios.remove(servicioSeleccionado);
            }
        } else {
            mostrarAlertaWarning("Seleccionar Servicio", "Ningún servicio seleccionado", "Por favor, seleccione un servicio para eliminar.");
        }
    }

    private boolean servicioExiste(String nombre) {
        return listaServicios.stream().anyMatch(servicio -> servicio.getNombre().equalsIgnoreCase(nombre));
    }

    private void mostrarAlertaError(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarAlertaWarning(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void guardarServicios() {
        Path archivoServicios = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "Taller", "IngenieriaSoftware", "yiskar", "Data", "Servicios.txt");

        try {
            // Crear el archivo si no existe
            if (!Files.exists(archivoServicios)) {
                Files.createFile(archivoServicios);
            }

            // Escribir los servicios en el archivo
            PrintWriter writer = new PrintWriter(Files.newBufferedWriter(archivoServicios, StandardOpenOption.TRUNCATE_EXISTING));
            for (Servicios servicio : listaServicios) {
                writer.println(servicio.getNombre() + "," + servicio.getPrecio());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}