package Taller.IngenieriaSoftware.yiskar.services;

import Taller.IngenieriaSoftware.yiskar.interfaces.IPagarService;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class PagarTarjetaService implements IPagarService
{
    private String numeroTarjeta;
    private int mesVencimiento;
    private int anhioVencimiento;
    private int codigoSeguridad;
    private boolean valido;
    private float montoTotal;

    public PagarTarjetaService(float montoTotal)
    {
        validarTarjeta();
        this.montoTotal = montoTotal;
    }
    @Override
    public boolean realizarPago()
    {
        if(!valido)
        {
            return false;
        }
        try
        {
            return ApiService.getInstance().realizarCargo(numeroTarjeta, montoTotal, "Compra de Giftcard Autolavados Méndez", mesVencimiento, anhioVencimiento, codigoSeguridad);

        }catch(Exception e)
        {
            AlertBox.mostrarError("Error", "Error al intentar realizar el cargo: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void validarTarjeta()
    {
        // Diálogo para solicitar el número de tarjeta de crédito
        TextInputDialog numeroDialog = new TextInputDialog();
        numeroDialog.setTitle("Datos de Tarjeta de Crédito");
        numeroDialog.setHeaderText("Ingrese el número de tarjeta de crédito:");
        numeroDialog.setContentText("Número de tarjeta:");

        // Obtener el número de tarjeta de crédito
        Optional<String> numeroResult = numeroDialog.showAndWait();
        if (numeroResult.isPresent())
        {
            String numeroTarjeta = numeroResult.get();

            // Diálogo para solicitar la fecha de vencimiento
            TextInputDialog fechaMDialog = new TextInputDialog();
            fechaMDialog.setTitle("Datos de Tarjeta de Crédito");
            fechaMDialog.setHeaderText("Ingrese el mes de vencimiento (MM):");
            fechaMDialog.setContentText("Mes de vencimiento:");

            // Obtener la fecha de vencimiento
            Optional<String> fechaResultM = fechaMDialog.showAndWait();

            TextInputDialog fechaADialog = new TextInputDialog();
            fechaADialog.setTitle("Datos de Tarjeta de Crédito");
            fechaADialog.setHeaderText("Ingrese el año de vencimiento (AAAA):");
            fechaADialog.setContentText("Año de vencimiento:");

            // Obtener la fecha de vencimiento
            Optional<String> fechaResultA = fechaADialog.showAndWait();
            if (fechaResultM.isPresent() && fechaResultA.isPresent())
            {
                String mesAnioVencimiento = fechaResultM.get()+"/"+fechaResultA.get();

                // Validar que la fecha de vencimiento esté en el formato MM/AAAA
                if (!mesAnioVencimiento.matches("\\d{2}/\\d{4}"))
                {
                    AlertBox.mostrarError("Formato incorrecto", "El formato de la fecha de vencimiento debe ser MM/AAAA", Alert.AlertType.ERROR);
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
                        AlertBox.mostrarError("Formato incorrecto", "El código de seguridad debe ser un número entero.", Alert.AlertType.ERROR);
                        return;
                    }

                    try
                    {
                        if(ApiService.getInstance().verificarTarjeta(numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad))
                        {
                            this.numeroTarjeta = numeroTarjeta;
                            this.mesVencimiento = mesVencimiento;
                            this.anhioVencimiento = anioVencimiento;
                            this.codigoSeguridad = codigoSeguridad;
                            this.valido = true;
                        }else
                        {
                            AlertBox.mostrarError("La tarjeta no es válida.","Error", Alert.AlertType.ERROR);
                            this.valido = false;
                        }
                    }catch(Exception e)
                    {
                        AlertBox.mostrarError("Error", "Error al intentar realizar el cargo: " + e.getMessage(), Alert.AlertType.ERROR);
                        return;
                    }
                }else
                {
                    AlertBox.mostrarError("Debe ingresar el código de seguridad.","Error", Alert.AlertType.ERROR);
                    return;
                }
            } else
            {
                AlertBox.mostrarError("Debe ingresar la fecha de vencimiento","Error", Alert.AlertType.ERROR);
                return;
            }
        } else
        {
            AlertBox.mostrarError("Debe ingresar el número de tarjeta.", "Error.", Alert.AlertType.ERROR);
            return;
        }
    }
}
