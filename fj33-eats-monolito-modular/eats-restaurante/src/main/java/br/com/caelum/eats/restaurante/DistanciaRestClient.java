package br.com.caelum.eats.restaurante;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
class DistanciaRestClient {

  private String distanciaServiceUrl;
  private RestTemplate restTemplate;

  DistanciaRestClient(RestTemplate restTemplate,
                                      @Value("${configuracao.distancia.service.url}") String distanciaServiceUrl) {
    this.distanciaServiceUrl = distanciaServiceUrl;
    this.restTemplate = restTemplate;
  }


  //jitter
  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 3, random = true ))
  void novoRestauranteAprovado(Restaurante restaurante) {
    RestauranteParaServicoDeDistancia restauranteParaDistancia = new RestauranteParaServicoDeDistancia(restaurante);
    String url = distanciaServiceUrl+"/restaurantes";
    ResponseEntity<RestauranteParaServicoDeDistancia> responseEntity = restTemplate.postForEntity(url, restauranteParaDistancia, RestauranteParaServicoDeDistancia.class);

    ResponseEntity<RestauranteParaServicoDeDistancia> responseEntity2 = restTemplate.
            postForEntity(url, restauranteParaDistancia, RestauranteParaServicoDeDistancia.class);
    HttpStatus statusCode = responseEntity.getStatusCode();
    if (!HttpStatus.CREATED.equals(statusCode)) {
      throw new RuntimeException("Status diferente do esperado: " + statusCode);
    }
  }

  void restauranteAtualizado(Restaurante restaurante) {
    RestauranteParaServicoDeDistancia restauranteParaDistancia = new RestauranteParaServicoDeDistancia(restaurante);
    String url = distanciaServiceUrl+"/restaurantes/" + restaurante.getId();
    restTemplate.put(url, restauranteParaDistancia, RestauranteParaServicoDeDistancia.class);
  }

}