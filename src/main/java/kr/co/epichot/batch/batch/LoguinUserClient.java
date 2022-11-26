package kr.co.epichot.batch.batch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.co.epichot.batch.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoguinUserClient {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private final WebClient webClient;

  public Mono<List<User>> getUpdatedUsers(LocalDateTime updatedAfter) {

    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/users")
            .queryParam("updated_after", updatedAfter.format(dateTimeFormatter))
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }
}
