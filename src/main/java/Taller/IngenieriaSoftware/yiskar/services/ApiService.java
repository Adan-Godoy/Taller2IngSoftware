package Taller.IngenieriaSoftware.yiskar.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApiService {
    private static ApiService instance;
    private String token;

    private static final String BASE_URL = "https://idonosob.pythonanywhere.com";

    // Constructor privado para evitar instanciación directa
    private ApiService() {
    }

    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

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

    public float obtenerSaldo(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String obtenerSaldoUrl = BASE_URL + "/obtener_saldo";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(obtenerSaldoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        if (response.statusCode() == 200) {
            return responseBodyJson.get("saldo").getAsFloat();
        } else {
            String errorMessage = responseBodyJson.get("msg").getAsString();
            throw new IOException(errorMessage);
        }
    }

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
