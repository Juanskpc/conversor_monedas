package clases;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Conversor {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner teclado = new Scanner(System.in);
        Conversor convertir = new Conversor();

        JsonParser parser = new JsonParser();
        JsonObject jsonObj = new JsonObject();

        int respuesta = 0;

        var mensajeBienvenida = """
                ******************************************
                
                Sea bienvenido/a al sistema de conversor de monedas :)
                
                1) Dolar            -> Peso argentino
                2) Peso argentino   -> Dolar
                3) Dolar            -> Real brasileño
                4) Real brasileño   -> Dolar
                5) Dolar            -> Peso colombiano
                6) Peso colombiano  -> Dolar
                7) Salir
                
                Elija una opción válida 
                
                ******************************************""";
        System.out.println(mensajeBienvenida);


        String url = "https://v6.exchangerate-api.com/v6/5b56cb0a1d393f5335c45578/latest/USD";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());



        while (respuesta != 7){
            System.out.println(mensajeBienvenida);

            System.out.println("Escriba una opción: ");
            respuesta = teclado.nextInt();

            String divisaIngresada = "";
            String divisaSolicitada = "";
            double valorConvertir = 0;

            if(respuesta > 0 && respuesta < 7){
                System.out.println("Ingrese el valor que desea convertir: ");
                valorConvertir = teclado.nextDouble();
            }


            switch (respuesta){
                case 1: //USD -> ARS
                    divisaIngresada = "USD";
                    divisaSolicitada = "ARS";
                    break;
                case 2: //ARS -> USD
                    divisaIngresada = "ARS";
                    divisaSolicitada = "USD";
                    break;
                case 3: //USD -> BRS
                    divisaIngresada = "USD";
                    divisaSolicitada = "BRL";
                    break;
                case 4: //BRS -> USD
                    divisaIngresada = "BRL";
                    divisaSolicitada = "USD";
                    break;
                case 5: //USD -> COP
                    divisaIngresada = "USD";
                    divisaSolicitada = "COP";
                    break;
                case 6: //COP -> USD
                    divisaIngresada = "COP";
                    divisaSolicitada = "USD";
                    break;
                case 7: //Salir
                    System.out.println("********** Adiós **********");
                    return;
                default:
                    System.out.println("Opción no válida :(");
            }
            String resultado = convertir.getDivisas(valorConvertir, divisaIngresada, divisaSolicitada);
            System.out.println(resultado);
        }


    }

    public String getDivisas(double valorConvertir, String divisaIngresada, String divisaSolicitada) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/5b56cb0a1d393f5335c45578/latest/" + divisaIngresada;
        Gson json = new Gson();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject conversionRates = gson.fromJson(response.body(), JsonObject.class);


        JsonObject divisasGeneral = conversionRates.getAsJsonObject("conversion_rates");

        double divisa = divisasGeneral.get(divisaSolicitada).getAsDouble();

        double valorFinal = valorConvertir * divisa;
        //System.out.println("El valor de " + valorConvertir + " [" + divisaIngresada + "] corresponde a -->" + valorFinal + " [" + divisaSolicitada + "]");
        String cadenaFinal = "El valor de " + valorConvertir + " [" + divisaIngresada + "] corresponde a -->" + valorFinal + " [" + divisaSolicitada + "]";

        return cadenaFinal;
    }
}
