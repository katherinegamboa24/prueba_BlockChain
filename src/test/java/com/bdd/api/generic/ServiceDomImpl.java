package com.bdd.api.generic;

import com.bdd.api.builder.ApiConfig;
import cucumber.api.DataTable;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.QueryableRequestSpecification;

import java.util.HashMap;
import java.util.Map;

public interface ServiceDomImpl {
    Headers configurerHeaders(DataTable var1);

    Map<String, Object> configurerParameters(DataTable var1);

    Map<String, Object> configurerPathVariable(DataTable var1);

    String configurerBodyRequest(String var1, DataTable var2);

    Response ejecutarApi(String var1, String var2, String var3, HashMap<String, Object> var4);

    Response ejecutarApiBuilder(ApiConfig var1);

    Object obtenerValorNodoRespuestaJson(String var1, Response var2);

    Object obtenerValorNodoDeArrayRespuestaJson(String var1, Response var2);

    String extraerValorNodoDeLaRespuesta(Response var1, String var2);

    ValidatableResponse validarCodigoRespuesta(Response var1, int var2);

    QueryableRequestSpecification requestSpecification();
}
