package kr.co.epichot.batch.config;

import javax.net.ssl.SSLException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LoguinClientConfig {

  private final String baseUrl = "https://auth.hiadjuster.com/api";

  @Bean
  public WebClient loguinClient() throws SSLException {
    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 2))
        .build();

    exchangeStrategies.messageWriters()
        .stream()
        .filter(LoggingCodecSupport.class::isInstance)
        .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

    return WebClient.builder()
        .baseUrl(baseUrl)
        .filter(ExchangeFilterFunctions.basicAuthentication("2497900722", "password"))
        .exchangeStrategies(exchangeStrategies)
        .build();
  }

//  private static ExchangeFilterFunction logRequest() {
//    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//      log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
//      clientRequest.headers()
//          .forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
//      return Mono.just(clientRequest);
//    });
//  }
}
