package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
public class ReservationAPITest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  public void get() {

    webTestClient
        .get()
        .uri("http://localhost:8080/reservations")
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody(Reservation.class)
        .value(
            reservation -> {
              assertThat(reservation).isNotNull();
              assertThat(reservation.getName()).isEqualTo("unit-testing");
            });
  }
}
