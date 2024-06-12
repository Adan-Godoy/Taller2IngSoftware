package Taller.IngenieriaSoftware.yiskar.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApiService {
    private static String token;

    private static final String BASE_URL = "https://idonosob.pythonanywhere.com";

    public static void login(String username, String password) throws IOException, InterruptedException {
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

        /* Ítalo: si no escribes correctamente la propiedad del JsonObject que vas a obtener, el programa se caerá
         es recomendable colocar todo dentro de un try-catch
         */
        if (response.statusCode() == 200) {
            token = responseBodyJson.get("access_token").getAsString();

        } else {
            // Ítalo: extrae el mensaje de error directamente del JsonObject
            String errorMessage = responseBodyJson.get("msg").getAsString();
            throw new IOException(errorMessage);
        }
    }

    public static boolean verificarTarjeta(String numeroTarjeta, String mesVencimiento, String anioVencimiento, String codigoSeguridad) throws IOException, InterruptedException {
        String validarTarjetaUrl = BASE_URL + "/validar_tarjeta";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":\"%s\",\"anio_vencimiento\":\"%s\",\"codigo_seguridad\":\"%s\"}",
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
}
