package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = NONE)
@AutoConfigureWireMock
class ConsumerApplicationTest {

  @Autowired
  private ReservationClient client;

  @Test
  void contextLoads() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/reservations"))
            .willReturn(
                WireMock.okJson("[{\"id\": \"1\",\"reservationName\": \"consumer-test\"}]")));

    Flux<Reservation> response = client.getAllReservations();
    StepVerifier.create(response)
        .assertNext(
            r -> {
              assertThat(r.getId()).isEqualTo("1");
              assertThat(r.getReservationName()).isEqualTo("consumer-test");
            })
        .verifyComplete();
  }
}
