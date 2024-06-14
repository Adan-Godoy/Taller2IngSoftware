package Taller.IngenieriaSoftware.yiskar.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApiService {
    /**
     * Instancia de la clase.
     */
    private static ApiService instance;
    /**
     * Variable que almacena el token del usuario logueado.
     */
    private String token;

    /**
     * Variable que almacena la URL de la API.
     */
    private static final String BASE_URL = "https://idonosob.pythonanywhere.com";

    /**
     * Método constructor de la clase.
     */
    private ApiService() {
    }

    /**
     * Método que retorna la instancia de la clase, si es null, la crea y la retorna.
     * @return Instancia de la clase.
     */
    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    /**
     * Método que permite iniciar sesión en la API.
     * @param username Username del usuario.
     * @param password Contraseña del usuario.
     * @throws IOException Si huoo un error con la autentificación.
     * @throws InterruptedException Si huoo un error con la autentificación.
     */
    public void login(String username, String password) throws IOException, InterruptedException {
        String loginUrl = BASE_URL + "/login";

        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        if (response.statusCode() == 200) {
            token = responseBodyJson.get("access_token").getAsString();
        } else {
            String errorMessage = responseBodyJson.get("msg").getAsString();
            throw new IOException(errorMessage);
        }
    }

    /**
     * Método que le solicita a la API verificar si una tarjeta es válida.
     * @param numeroTarjeta Numero de la tarjeta.
     * @param mesVencimiento Mes de vencimiento de la tarjeta.
     * @param anioVencimiento Año de vencimiento de la tarjeta.
     * @param codigoSeguridad Código de seguridad de la tarjeta.
     * @return True si la tarjeta es válida, false de lo contrario.
     * @throws IOException Si huoo un error con la validación.
     * @throws InterruptedException Si huoo un error con la validación.
     */
    public boolean verificarTarjeta(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String validarTarjetaUrl = BASE_URL + "/validar_tarjeta";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(validarTarjetaUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        String mensaje = responseBodyJson.get("msg").getAsString();

        if (response.statusCode() == 200) {
            return mensaje.equals("Tarjeta válida");
        } else {
            throw new IOException(mensaje);
        }
    }

    /**
     * Método que solicita a la API un cargo a una tarjeta ingresada en el sistema.
     * @param numeroTarjeta Numero de la tarjeta.
     * @param monto Monto del cargo.
     * @param descripcion Descripción del cargo.
     * @param mesVencimiento Mes de vencimiento de la tarjeta.
     * @param anioVencimiento Año de vencimiento de la tarjeta.
     * @param codigoSeguridad Código e seguridad de la tarjeta.
     * @return True si el cargo se realizó con éxito, false de lo contrario.
     * @throws IOException Si huoo un error con el cargo.
     * @throws InterruptedException Si huoo un error con el cargo.
     */
    public boolean realizarCargo(String numeroTarjeta, float monto, String descripcion, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String realizarCargoUrl = BASE_URL + "/realizar_cargo";

        String montoStr = String.format("%.2f", monto);

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"monto\":\"%s\",\"descripcion\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, montoStr, descripcion, mesVencimiento, anioVencimiento, codigoSeguridad);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(realizarCargoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        String mensaje = responseBodyJson.get("msg").getAsString();

        if (response.statusCode() == 200) {
            return mensaje.equals("Cargo realizado exitosamente");
        } else {
            throw new IOException(mensaje);
        }
    }


}
