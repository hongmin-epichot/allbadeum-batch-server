package kr.co.epichot.batch.service;

import kr.co.epichot.batch.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  private final WebClient client;

  public UserService() {
    this.client = WebClient.create("https://auth.hiadjuster.com/api");
  }

  public User getUser(final String id) {
    return client.get()
        .uri("/users/{id}", id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
        .bodyToMono(User.class)
        .blockOptional()
        .orElse(null);
  }

}
