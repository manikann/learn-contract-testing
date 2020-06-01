package io.nataman.sbtdd;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class ReservationApiClient {

  private final WebClient webClient;

  Flux<Reservation> getAllReservations() {
    return webClient
        .get()
        .uri("http://localhost:8080/reservations")
        .retrieve()
        .bodyToFlux(Reservation.class);
  }
}
