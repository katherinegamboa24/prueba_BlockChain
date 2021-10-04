package com.bdd.api.step;

import com.bdd.api.lib.ServiceDOM;
import cucumber.api.DataTable;
import cucumber.api.java.es.Y;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.util.EnvironmentVariables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BlockChainStep extends ServiceDOM {

    private EnvironmentVariables environmentVariables;

    private Response response;
    private String access_token;
    private String nombreServicio;
    private String formato;
    private String jsonEncriptado;
    private String jsonTxtPlano;
    private Headers cabecera;
    private String X_REFRESH_TOKEN;
    private String X_AUTH_TOKEN;
    private String RUC;
    private String contractID;
    private String productoCode;
    private String operacion;
    private Integer operationCode;
    private String cic;
    private String name;
    private String accountNumber;
    private String consistCode;
    private String cuentaDestino;
    private String seedConfirm;
    private String keyboard1confirm;

    public BlockChainStep(Headers cabecera) {
        this.cabecera = cabecera;
    }

    @Step("Ejecutar llamada a servicio Api")
    public void ejecutarServicioApi() {
        String url = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.blockcypher.url");
        String metodo = environmentVariables.getProperty("api.blockcypher.metodo");
        nombreServicio = environmentVariables.getProperty("api.blockcypher.nombreServicio");
        formato = environmentVariables.getProperty("api.blockcypher.formato");
        SerenityRest.useRelaxedHTTPSValidation();
        response = ejecutarServicio(url, metodo);
    }

    @Step("Ejecutar llamada a servicio")
    public Response ejecutarServicio(String url, String metodo) {
        String tipoMetodo = metodo.toUpperCase();
        Response response;

        switch (tipoMetodo) {
            case "POST":

                response = SerenityRest
                        .given()
                        .contentType("application/json")
                        .when().post(url);
                break;
            case "GET":
                response = SerenityRest
                        .given().headers(cabecera)
                        .contentType("application/json")
                        .when().get(url);
                break;
            default:

                response = SerenityRest
                        .given()
                        .contentType("application/json")
                        .when().post(url);
                ;
                break;
        }
        response.print();
        return response;


    }
    @Step("Validar status code de respuesta del servicio")
    public void validarCodigoRespueta(String identficadorServicio) throws IOException {
        //validarCodigoRespuesta(response, 200);
    }
    @Step("Guardar resultado de la ejecucion de un Api")
    public void guardo_respuesta_generado_ejecucion_del_servicio() throws IOException {
        createResponseFileByFormat(this.nombreServicio, this.formato, response.getBody().asString());
    }
    @Step("Validar que el contenido de la respuesta del servicio contenga un campo en especial.")
    public Boolean validarPresenciaDeCampoEnResponse(String fieldName) {
        try {
            String body = response.getBody().asString();
            if (body.contains(fieldName)) {
                return true;

            }
            return false;
        } catch (Exception e) {
            System.out.println("Error controlado: No existe el campo " + fieldName + " en el body JSON");
            return false;
        }
    }

    public static void createResponseFileByFormat(String serviceName, String formatFile, String response) throws
            IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS");
        LocalDateTime now = LocalDateTime.now();
        //Crea path del file
        File filePath = new File("response/api/" + serviceName + "/" + formatFile);
        filePath.mkdirs();
        //Nombre del file con formato fecha al final
        String finalFileName = serviceName + "_" + dtf.format(now) + "." + formatFile;

        //Crea el file dentro del path creado
        File file = new File(filePath + "/" + finalFileName);

        System.out.println("finalFileName: " + finalFileName);

        if (file.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

        //Escribe la respueta dentro del file creado
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(response);
        fileWriter.close();
    }
    protected Headers configurarCabeceras(List<Map<String, String>> list) {

        List<Header> headerList = new LinkedList<>();
        for (Map<String, String> stringStringMap : list) {
            Header header = new Header(stringStringMap.get("cabeceras"), stringStringMap.get("valor"));
            headerList.add(header);
        }
        return new Headers(headerList);
    }
    @Y("^Configuro cabeceras del servicio$")
    public void configuroCabeceras(DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        cabecera = configurarCabeceras(list);

        if (cabecera.get("Authorization") != null) {
            List<Header> headerList = new LinkedList<>();
            for (Header header : cabecera) {
                String valor = header.getValue();
                if (header.getName().contains("Authorization"))
                    valor = "Bearer " + cabecera.get("Authorization").getValue();
                if (header.getName().contains("Authorization")) {

                    if (cabecera.get("Authorization").getValue().contains("${token}"))
                        valor = "Bearer " + this.X_REFRESH_TOKEN;
                }

                Header nueva_header = new Header(header.getName(), valor);
                headerList.add(nueva_header);
            }
            cabecera = new Headers(headerList);
        }

        if (cabecera.get("ciam-token") != null) {
            List<Header> headerList = new LinkedList<>();
            for (Header header : cabecera) {
                String valor = header.getValue();
                if (header.getName().contains("ciam-token")) valor = "Bearer " + cabecera.get("ciam-token").getValue();
                if (header.getName().contains("ciam-token")) {

                    if (cabecera.get("ciam-token").getValue().contains("${token}"))
                        valor = "Bearer " + this.X_REFRESH_TOKEN;
                }

                Header nueva_header = new Header(header.getName(), valor);
                headerList.add(nueva_header);
            }
            cabecera = new Headers(headerList);
        }

        if (cabecera.get("X-AUTH-TOKEN") != null) {
            List<Header> headerList = new LinkedList<>();
            for (Header header : cabecera) {
                String valor = header.getValue();
                if (header.getName().contains("X-AUTH-TOKEN")) valor = cabecera.get("X-AUTH-TOKEN").getValue();
                if (header.getName().contains("X-AUTH-TOKEN")) {
                    if (cabecera.get("X-AUTH-TOKEN").getValue().contains("${X-token}"))
                        valor = this.X_AUTH_TOKEN;
                }

                Header nueva_header = new Header(header.getName(), valor);
                headerList.add(nueva_header);
            }
            cabecera = new Headers(headerList);
        }
    }
}
