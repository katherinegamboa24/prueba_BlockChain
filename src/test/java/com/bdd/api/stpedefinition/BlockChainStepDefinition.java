package com.bdd.api.stpedefinition;

import com.bdd.api.step.BlockChainStep;
import cucumber.api.DataTable;
import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Y;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.io.IOException;

public class BlockChainStepDefinition {
    @Steps
    BlockChainStep blockChainSteps;


    @Cuando("^Ejecuto el servicio \"([^\"]*)\" para validar el bloque$")
    public void ejecuto_el_servicio_para_validar_el_bloque(String identficadorServicio) throws IOException {
        blockChainSteps.ejecutarServicioApi();
        blockChainSteps.validarCodigoRespueta(identficadorServicio);
    }
    @Y("^Guardo el response de la ejecucion del servicio$")
    public void guardo_el_response_del_servicio() throws IOException {
        blockChainSteps.guardo_respuesta_generado_ejecucion_del_servicio();
    }

    @Y("^Valido que la respuesta contenga el atributo \"([^\"]*)\" y \"([^\"]*)\"$")
    public void valido_contenido_respuesta(String contenido) {
        Assert.assertTrue(blockChainSteps.validarPresenciaDeCampoEnResponse(contenido));
    }
    @Cuando("^que configuro la cabecera del servicio$")
    public void configuro_headers(DataTable dataTable) {
        blockChainSteps.configuroCabeceras(dataTable);
    }
}
