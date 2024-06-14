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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    /**
     * Método que inicia el contenido de las tablas de la interfaz.
     */
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

    /**
     * Método que reinicia la tabla de servicios disponibles.
     */
    private void cargarServicio() {
        listaServicioDisp.clear();


        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        Servicio[] servicios = serviciosRepository.obtenerServicios();
        if (servicios == null) {
            AlertBox.mostrarError("Por el momento no es posible la compra de tarjeta de regalos. Intente más tarde.", "No hay servicios", Alert.AlertType.WARNING);
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

    /**
     * Método que permite al cliente agregar un servicio a su tabla de servicios seleccionados.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
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

    /**
     * Método que permite al cliente eliminar un servicio de su tabla de servicios seleccionados.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
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

    /**
     * Método que permite al cliente poder pagar los servicios seleccionados por medio de tarjeta de crédito.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
    @FXML
    private void pagoTarjeta(ActionEvent event) {

        float montoTotal = listaServicioAgre.get(listaServicioAgre.size() - 1).getPrecio();  // Obtener el monto total correcto

        PagarTarjetaService pago = new PagarTarjetaService(montoTotal);

        if (pago.realizarPago())
        {
            Servicio[] comprados = new Servicio[listaServicioAgre.size()-1];
            for(int i=0;i<listaServicioAgre.size()-1;i++)
            {
                comprados[i] = listaServicioAgre.get(i);
            }

            generarGiftCard(comprados);
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

    /**
     * Método que permite al cliente poder pagar los servicios seleccionados por medio de sus puntos acumulados.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
    @FXML
    private void pagoPuntos(ActionEvent event)
    {
        float montoTotal = listaServicioAgre.get(listaServicioAgre.size() - 1).getPrecio();  // Obtener el monto total correcto
        PagarPuntosService pagarPuntosService = new PagarPuntosService(montoTotal);
        if(pagarPuntosService.realizarPago())
        {
            Servicio[] comprados = new Servicio[listaServicioAgre.size()-1];
            for(int i=0;i<listaServicioAgre.size()-1;i++)
            {
                comprados[i] = listaServicioAgre.get(i);
            }

            generarGiftCard(comprados);
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

    /**
     * Método que genera una giftcard y la muestra por pantalla con los detalles solicitados en la ERS.
     * @param servicios servicios asociados a la giftcard.
     */
    private void generarGiftCard(Servicio[] servicios)
    {
        VBox vbox = new VBox(10);

        //Generar el código de la giftcard.
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        // Generar el primer dígito.
        int primerDigito = random.nextInt(9) + 1;
        codigo.append(primerDigito);

        // Generar los siete dígitos restantes.
        for (int i = 0; i < 7; i++) {
            int digito = random.nextInt(10);
            codigo.append(digito);
        }
        Label labelCodigo = new Label("Código de la giftcard: " + codigo);

        //Fecha y hora de la compra de la giftCard
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraCompraStr = fechaActual.format(formatter);
        Label labelFechaHoraCompra = new Label("Fecha y hora de la compra: " + fechaHoraCompraStr);

        //Servicios asociados.
        Label labelServicios = new Label("Detalle de los servicios asociados:");
        TextArea textAreaServicios = new TextArea();
        textAreaServicios.setEditable(false);
        for (Servicio servicio : servicios) {
            textAreaServicios.appendText(servicio.getNombre() + "\n");
        }

        //Fecha de vencimiento.
        LocalDate fechaVenc = LocalDate.now().plusMonths(6);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaVencimientoStr = fechaVenc.format(formatter2);
        Label labelFechaVencimiento = new Label("Fecha de vencimiento: " + fechaVencimientoStr);

        //Generar ventana de comprobante.
        vbox.getChildren().addAll(labelCodigo, labelFechaHoraCompra, labelServicios, textAreaServicios, labelFechaVencimiento);
        Scene scene = new Scene(vbox, 400, 300);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Comprobante de Gift Card");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}


