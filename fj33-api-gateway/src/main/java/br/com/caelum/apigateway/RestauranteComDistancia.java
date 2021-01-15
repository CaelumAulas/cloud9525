package br.com.caelum.apigateway;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestauranteComDistancia {

    private DistanciaRestClient distanciaRestClient;
    private RestauranteRestClient restauranteRestClient;


    public RestauranteComDistancia(DistanciaRestClient distanciaRestClient, RestauranteRestClient restauranteRestClient) {
        this.distanciaRestClient = distanciaRestClient;
        this.restauranteRestClient = restauranteRestClient;
    }

    @GetMapping("/restaurante-com-distancia/{cep}/restaurante/{restauranteId}")
    public Map<String, Object> buscaRestauranteEDistancia(@PathVariable("cep") String cep,
                                                          @PathVariable("restauranteId") Long id) {

        Map<String, Object> restaurante = restauranteRestClient.porId(id);
        Map<String, Object> distancia = distanciaRestClient.porCepEId(cep, id);

        restaurante.putAll(distancia);

        return restaurante;
    }


}
