package br.com.caelum.apigateway;

import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.netflix.zuul.context.RequestContext;

@Configuration
class LocationRewriteConfig {

  @Bean
  LocationRewriteFilter locationRewriteFilter() {
    return new LocationRewriteFilter() {
      @Override
      public boolean shouldFilter() {
        int statusCode = RequestContext.getCurrentContext().getResponseStatusCode();
        return HttpStatus.valueOf(statusCode).is3xxRedirection() || HttpStatus.valueOf(statusCode).is2xxSuccessful();
      }
    };
  }

}
