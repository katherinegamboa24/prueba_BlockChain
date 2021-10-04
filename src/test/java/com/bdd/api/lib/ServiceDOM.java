package com.bdd.api.lib;

import com.bdd.api.builder.ApiConfig;
import com.bdd.api.builder.ApiConfigBuilder;
import com.bdd.api.generic.ServiceDomImpl;
import com.bdd.api.util.UtilApi;
import cucumber.api.DataTable;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ServiceDOM extends ScenarioSteps implements ServiceDomImpl {
    private final String sourceClass = this.getClass().getName();
    private transient EnvironmentVariables environmentVariables;
    private transient RequestSpecification reqConfig = null;
    private transient Map<String, Object> parameters = new HashMap();

    public ServiceDOM() {
    }

    public Headers configurerHeaders(DataTable dataTable) {
        List<Header> headerList = new LinkedList();
        List<Map<String, String>> listCabeceras = dataTable.asMaps(String.class, String.class);
        Iterator var4 = listCabeceras.iterator();

        while(var4.hasNext()) {
            Map<String, String> stringStringMap = (Map)var4.next();
            Header header = new Header((String)stringStringMap.get("cabeceras"), (String)stringStringMap.get("valor"));
            headerList.add(header);
        }

        Headers headers = new Headers(headerList);
        UtilApi.logger(ServiceDOM.class).log(Level.INFO, "CABECERAS: {0}", headers);
        return headers;
    }

    public Map<String, Object> configurerParameters(DataTable dataTable) {
        List<Map<String, String>> listParemetros = dataTable.asMaps(String.class, String.class);
        Iterator var3 = listParemetros.iterator();

        while(var3.hasNext()) {
            Map<String, String> stringStringMap = (Map)var3.next();
            this.parameters.put(stringStringMap.get("parametros"), stringStringMap.get("valor"));
        }

        UtilApi.logger(ServiceDOM.class).log(Level.INFO, "PARAMETROS: {0}", this.parameters);
        return this.parameters;
    }

    public Map<String, Object> configurerPathVariable(DataTable dataTable) {
        List<Map<String, String>> listPathVariable = dataTable.asMaps(String.class, String.class);
        Iterator var3 = listPathVariable.iterator();

        while(var3.hasNext()) {
            Map<String, String> stringStringMap = (Map)var3.next();
            this.parameters.put(stringStringMap.get("pathVariables"), stringStringMap.get("valor"));
        }

        UtilApi.logger(ServiceDOM.class).log(Level.INFO, "PATH-VARIABLES: {0}", this.parameters);
        return this.parameters;
    }

    public String configurerBodyRequest(String pathServiceRequest, DataTable dataTableRequestValues) {
        File file = new File(pathServiceRequest);
        String bodyRequest = "";

        try {
            String content = FileUtils.readFileToString(file, "utf-8");
            JSONObject jsonObject = new JSONObject(content);
            bodyRequest = jsonObject.toString();
            List<Map<String, String>> listBodyRequest = dataTableRequestValues.asMaps(String.class, String.class);

            String newJson;
            for(Iterator var9 = listBodyRequest.iterator(); var9.hasNext(); bodyRequest = newJson) {
                Map<String, String> stringStringMap = (Map)var9.next();
                newJson = UtilApi.updateValueOfNodeInJson(bodyRequest, (String)stringStringMap.get("key"), ((String)stringStringMap.get("valor")).replace("%WHITE%", " ").replace("%BAR%", "|"));
            }
        } catch (IOException var11) {
            UtilApi.logger(this.getClass()).throwing(this.sourceClass, "configurerBodyRequest()", var11);
        }

        UtilApi.logger(this.getClass()).log(Level.INFO, "BODY-REQUEST: {0}", bodyRequest);
        return bodyRequest;
    }

    /** @deprecated */
    @Deprecated
    public Response ejecutarApi(String tipoApi, String pathResource, String metodo, HashMap<String, Object> apiConfiguration) {
        SerenityRest.useRelaxedHTTPSValidation();
        Response response = null;
        Map<String, Object> pathVariables = Collections.emptyMap();
        Map<String, Object> parametros = Collections.emptyMap();
        Map<String, Object> formParamas = Collections.emptyMap();
        String body = "";
        String tipoMetodo = metodo.toUpperCase();
        String fullResource;
        String baseURI;
        if (!tipoApi.equals("junit")) {
            baseURI = EnvironmentSpecificConfiguration.from(this.environmentVariables).getProperty("url.base.api." + tipoApi);
            fullResource = baseURI + pathResource;
        } else {
            fullResource = pathResource;
        }

        if (apiConfiguration.get("pathVariables") != null) {
            pathVariables = (Map)apiConfiguration.get("pathVariables");
        }

        if (apiConfiguration.get("parametros") != null) {
            parametros = (Map)apiConfiguration.get("parametros");
        }

        if (apiConfiguration.get("formParametros") != null) {
            formParamas = (Map)apiConfiguration.get("formParametros");
        }

        if (apiConfiguration.get("bodyRequest") != null) {
            body = apiConfiguration.get("bodyRequest").toString();
        }

        try {
            baseURI = tipoMetodo.toUpperCase(Locale.getDefault());
            byte var13 = -1;
            switch(baseURI.hashCode()) {
                case 70454:
                    if (baseURI.equals("GET")) {
                        var13 = 0;
                    }
                    break;
                case 79599:
                    if (baseURI.equals("PUT")) {
                        var13 = 3;
                    }
                    break;
                case 2461856:
                    if (baseURI.equals("POST")) {
                        var13 = 1;
                    }
                    break;
                case 75900968:
                    if (baseURI.equals("PATCH")) {
                        var13 = 2;
                    }
                    break;
                case 2012838315:
                    if (baseURI.equals("DELETE")) {
                        var13 = 4;
                    }
            }

            switch(var13) {
                case 0:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras")).pathParams(pathVariables).params(parametros);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).get(fullResource, new Object[0]);
                    break;
                case 1:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras"));
                    if (body.isEmpty()) {
                        this.reqConfig.formParams(formParamas);
                    } else {
                        this.reqConfig.body(body);
                    }

                    if (parametros.isEmpty()) {
                        this.reqConfig.pathParams(pathVariables);
                    } else {
                        this.reqConfig.params(parametros);
                    }

                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).post(fullResource, new Object[0]);
                    break;
                case 2:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras")).body(body);
                    if (parametros.isEmpty()) {
                        this.reqConfig.pathParams(pathVariables);
                    } else {
                        this.reqConfig.params(parametros);
                    }

                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).patch(fullResource, new Object[0]);
                    break;
                case 3:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras")).params(parametros).body(body);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).put(fullResource, new Object[0]);
                    break;
                case 4:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras"));
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).delete(fullResource, new Object[0]);
                    break;
                default:
                    this.reqConfig = SerenityRest.given().headers((Headers)apiConfiguration.get("cabeceras"));
                    response = (Response)this.reqConfig.when().post(fullResource, new Object[0]);
                    UtilApi.logger(ServiceDOM.class).log(Level.INFO, "Verbo {0} no disponible.", tipoMetodo);
            }
        } catch (Exception var14) {
            UtilApi.logger(ServiceDOM.class).throwing(this.sourceClass, "ejecutarApi()", var14);
            UtilApi.logger(ServiceDOM.class).log(Level.WARNING, "Cause {0}", var14.getCause());
            UtilApi.logger(ServiceDOM.class).log(Level.WARNING, "Message {0}", var14.getMessage());
        }

        assert response != null;

        response.prettyPeek();
        return response;
    }

    public Response ejecutarApiBuilder(ApiConfig apiConfig) {
        SerenityRest.useRelaxedHTTPSValidation();
        Response response = null;
        Map<String, Object> pathVariables = Collections.emptyMap();
        Map<String, Object> parametros = Collections.emptyMap();
        Map<String, Object> formUrlEncoded = Collections.emptyMap();
        String body = "";
        String tipoMetodo = apiConfig.getMethod().toUpperCase();
        String tipoApi = apiConfig.getApiType();
        String apiURL = apiConfig.getApiURL();
        Headers headers = apiConfig.getHeaders();
        String fullURL;
        String baseURI;
        if (!tipoApi.equals("junit")) {
            baseURI = EnvironmentSpecificConfiguration.from(this.environmentVariables).getProperty("url.base.api." + tipoApi);
            fullURL = baseURI + apiURL;
        } else {
            fullURL = apiURL;
        }

        if (apiConfig.getPathVariables() != null) {
            pathVariables = apiConfig.getPathVariables();
        }

        if (apiConfig.getParams() != null) {
            parametros = apiConfig.getParams();
        }

        if (apiConfig.getFormUrlEncoded() != null) {
            formUrlEncoded = apiConfig.getFormUrlEncoded();
        }

        if (apiConfig.getBody() != null) {
            body = apiConfig.getBody();
        }

        try {
            baseURI = tipoMetodo.toUpperCase(Locale.getDefault());
            byte var13 = -1;
            switch(baseURI.hashCode()) {
                case 70454:
                    if (baseURI.equals("GET")) {
                        var13 = 0;
                    }
                    break;
                case 79599:
                    if (baseURI.equals("PUT")) {
                        var13 = 4;
                    }
                    break;
                case 2461856:
                    if (baseURI.equals("POST")) {
                        var13 = 1;
                    }
                    break;
                case 75900968:
                    if (baseURI.equals("PATCH")) {
                        var13 = 2;
                    }
                    break;
                case 2012838315:
                    if (baseURI.equals("DELETE")) {
                        var13 = 3;
                    }
            }

            switch(var13) {
                case 0:
                    this.reqConfig = SerenityRest.given().headers(headers).pathParams(pathVariables).params(parametros);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).get(fullURL, new Object[0]);
                    break;
                case 1:
                    this.reqConfig = SerenityRest.given().headers(headers);
                    if (body.isEmpty()) {
                        this.reqConfig.formParams(formUrlEncoded);
                    } else {
                        this.reqConfig.body(body);
                    }

                    if (parametros.isEmpty()) {
                        this.reqConfig.pathParams(pathVariables);
                    } else {
                        this.reqConfig.params(parametros);
                    }

                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).post(fullURL, new Object[0]);
                    break;
                case 2:
                    this.reqConfig = SerenityRest.given().headers(apiConfig.getHeaders()).body(body);
                    if (parametros.isEmpty()) {
                        this.reqConfig.pathParams(pathVariables);
                    }

                    if (pathVariables.isEmpty()) {
                        this.reqConfig.params(parametros);
                    }

                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).patch(fullURL, new Object[0]);
                    break;
                case 3:
                    this.reqConfig = SerenityRest.given().headers(headers);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).delete(fullURL, new Object[0]);
                    break;
                case 4:
                    this.reqConfig = SerenityRest.given().headers(headers).params(parametros).body(body);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).put(fullURL, new Object[0]);
                    break;
                default:
                    this.reqConfig = SerenityRest.given().headers(headers);
                    response = (Response)((RequestSpecification)this.reqConfig.when().log().all()).post(fullURL, new Object[0]);
                    UtilApi.logger(ServiceDOM.class).log(Level.INFO, "Verbo {0} no disponible.", tipoMetodo);
            }
        } catch (Exception var14) {
            UtilApi.logger(ServiceDOM.class).log(Level.WARNING, "Cause {0}", var14.getCause());
            UtilApi.logger(ServiceDOM.class).log(Level.WARNING, "Message {0}", var14.getMessage());
            UtilApi.logger(ServiceDOM.class).throwing(this.sourceClass, "ejecutarApiBuilder()", var14);
        }

        assert response != null;

        response.prettyPeek();
        return response;
    }

    public QueryableRequestSpecification requestSpecification() {
        return SpecificationQuerier.query(this.reqConfig);
    }

    public Object obtenerValorNodoRespuestaJson(String nodo, Response response) {
        return JsonPath.with(response.getBody().asString()).get(nodo);
    }

    public String extraerValorNodoDeLaRespuesta(Response response, String nodo) {
        return (String)((ValidatableResponse)response.then()).extract().body().jsonPath().get(nodo);
    }

    public Object obtenerValorNodoDeArrayRespuestaJson(String nodo, Response response) {
        return JsonPath.with(response.getBody().asString()).getList(nodo);
    }

    public ValidatableResponse validarCodigoRespuesta(Response response, int statusCode) {
        return (ValidatableResponse)((ValidatableResponse)((ValidatableResponse)response.then()).assertThat()).statusCode(statusCode);
    }

    protected void validarEsquema(Response response, String schemaFile) {
        File file = new File(schemaFile);
        String jsonObjectString = "";

        try {
            String content = FileUtils.readFileToString(file, "utf-8");
            JSONObject jsonObject = new JSONObject(content);
            jsonObjectString = jsonObject.toString();
        } catch (IOException var8) {
            UtilApi.logger(this.getClass()).throwing(this.sourceClass, "validarEsquema()", var8);
        }

        ((ValidatableResponse)((ValidatableResponse)response.then()).assertThat()).body(JsonSchemaValidator.matchesJsonSchema(jsonObjectString), new Matcher[0]);
    }

    protected ApiConfigBuilder apiBuilder() {
        return new ApiConfigBuilder();
    }

}
