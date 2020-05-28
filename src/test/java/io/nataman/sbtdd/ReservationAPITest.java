package io.nataman.sbtdd;

import static org.mockito.Mockito.when;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Test for {@link ReservationHttpConfiguration}
 */
@WebFluxTest
@Import({ReservationHttpConfiguration.class})
@Log4j2
public class ReservationAPITest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  ReservationRepository mockRepository;

  @Test
  void get() {

    when(mockRepository.findAll())
        .thenReturn(Flux.just(Reservation.builder().id("1").name("unit-test...").build()));

    webTestClient
        .get()
        .uri("http://localhost:8080/reservations")
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("@.[0].name")
        .isEqualTo("unit-test...");
  }
}
