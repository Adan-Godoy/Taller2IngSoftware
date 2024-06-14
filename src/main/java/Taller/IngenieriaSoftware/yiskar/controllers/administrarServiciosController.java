package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.entities.Servicio;
import Taller.IngenieriaSoftware.yiskar.repository.ServiciosRepository;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
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
    private TableView<Servicio> TablaServicios;
    @FXML
    private TableColumn<Servicio, String> colNombre;
    @FXML
    private TableColumn<Servicio, Float> colPrecio;

    private ObservableList<Servicio> listaServicios;

    private Stage stage;

    @FXML
    public void initialize() {
        TablaServicios.setPlaceholder(new Label("No hay servicios registrados"));
        listaServicios = FXCollections.observableArrayList();

        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("Precio"));

        TablaServicios.setItems(listaServicios);
        cargarServicio();
    }

    private void cargarServicio() {
        listaServicios.clear();
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
            listaServicios.add(servicio);
        }
        TablaServicios.refresh();

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
                AlertBox.mostrarError("El nombre del servicio no puede estar vacío.", "Error al agregar servicio.", Alert.AlertType.ERROR);
                return;
            }

            TextInputDialog precioDialog = new TextInputDialog();
            precioDialog.setTitle("Agregar Servicio");
            precioDialog.setHeaderText("Ingrese el precio del servicio");
            precioDialog.setContentText("Precio:");
            Optional<String> precioResult = precioDialog.showAndWait();

            precioResult.ifPresent(precioStr -> {
                if (precioStr.trim().isEmpty()) {
                    AlertBox.mostrarError("El precio no puede estar vacío.", "Error al agregar servicio.", Alert.AlertType.ERROR);
                    return;
                }
                try {
                    float precio = Float.parseFloat(precioStr);
                    if (precio < 300) {
                        AlertBox.mostrarError("El precio debe ser al menos de 300 pesos chilenos.", "Error al agregar servicio.", Alert.AlertType.ERROR);
                        return;
                    }

                    ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
                    if (serviciosRepository.agregarServicio(nombre, (int) precio)) {
                        AlertBox.mostrarError("Servicio agregado con éxito.", "Operación exitosa", Alert.AlertType.CONFIRMATION);
                        cargarServicio();
                    } else {
                        AlertBox.mostrarError("Este nombre de servicio ya existe.", "Error al agregar servicio.", Alert.AlertType.ERROR);
                    }

                } catch (NumberFormatException e) {
                    AlertBox.mostrarError("El precio debe ser un valor numérico.", "Error al agregar servicio.", Alert.AlertType.ERROR);
                }
            });
        });
    }

    @FXML
    private void modificarNombre(ActionEvent event) {
        Servicio servicioSeleccionado = TablaServicios.getSelectionModel().getSelectedItem();

        if (servicioSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(servicioSeleccionado.getNombre());
            dialog.setTitle("Modificar Servicio");
            dialog.setHeaderText("Modificar nombre del servicio");
            dialog.setContentText("Nombre:");
            Optional<String> nombreResult = dialog.showAndWait();

            nombreResult.ifPresent(nombre ->
            {
                if (nombre.trim().isEmpty()) {
                    AlertBox.mostrarError("El nombre del servicio no puede estar vacío.", "Error al modificar servicio.", Alert.AlertType.ERROR);
                    return;
                }

                ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
                if (serviciosRepository.editarNombre(servicioSeleccionado.getNombre(), nombre))
                {
                    AlertBox.mostrarError("Nombre modificado con éxito", "Modificacion exitosa.", Alert.AlertType.CONFIRMATION);
                    cargarServicio();
                } else
                {
                    AlertBox.mostrarError("El nombre seleccionado ya existe.", "Error al modificar servicio", Alert.AlertType.ERROR);
                }
            });
        }else
        {
            mostrarAlertaWarning("Seleccionar Servicio", "Ningún servicio seleccionado", "Por favor, seleccione un servicio para modificar.");
        }
    }

    @FXML
    private void modificarPrecio(ActionEvent event)
    {
        Servicio servicioSeleccionado = TablaServicios.getSelectionModel().getSelectedItem();

        if (servicioSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(servicioSeleccionado.getNombre());
            dialog.setTitle("Modificar Servicio");
            dialog.setHeaderText("Modificar precio del servicio");
            dialog.setContentText("Nuevo precio:");
            Optional<String> precioResult = dialog.showAndWait();

            precioResult.ifPresent(precio ->
            {
                if (precio.trim().isEmpty()) {
                    AlertBox.mostrarError("El precio del servicio no puede estar vacío.", "Error al modificar servicio.", Alert.AlertType.ERROR);
                    return;
                }
                int precioNuevo;
                try
                {
                    precioNuevo = Integer.parseInt(precio);
                    if(precioNuevo<300)
                    {
                        AlertBox.mostrarError("El precio no puede ser menor a 300 pesos chilenos.","Error al modificar servicio", Alert.AlertType.CONFIRMATION);
                        return;
                    }

                }catch(Exception e)
                {
                    AlertBox.mostrarError("El precio debe ser numérico","Error al modificar servicio", Alert.AlertType.ERROR);
                    return;
                }

                ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
                if (serviciosRepository.editarPrecio(servicioSeleccionado.getNombre(), precioNuevo))
                {
                    AlertBox.mostrarError("Precio modificado con éxito", "Modificacion exitosa.", Alert.AlertType.CONFIRMATION);
                    cargarServicio();
                } else
                {
                    AlertBox.mostrarError("Hubo un error al modificar el precio.", "Error al modificar servicio", Alert.AlertType.ERROR);
                }
            });
        }else
        {
            mostrarAlertaWarning("Seleccionar Servicio", "Ningún servicio seleccionado", "Por favor, seleccione un servicio para modificar.");
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicio servicioSeleccionado = TablaServicios.getSelectionModel().getSelectedItem();

        if (servicioSeleccionado != null) {
            // Confirmación doble para eliminar servicio
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro que desea eliminar este servicio?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
                if(serviciosRepository.eliminarServicio(servicioSeleccionado.getNombre()))
                {
                    AlertBox.mostrarError("El servicio ha sido eliminado exitosamente","Operacion exitosa", Alert.AlertType.CONFIRMATION);
                    cargarServicio();
                }
            }else
            {
                AlertBox.mostrarError("Hubo un error al eliminar el servicio.","Error al eliminar servicio.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlertaWarning("Seleccionar Servicio", "Ningún servicio seleccionado", "Por favor, seleccione un servicio para eliminar.");
        }
    }

    private void mostrarAlertaWarning(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}