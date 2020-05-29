package io.nataman.sbtdd;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = NONE)
class ConsumerApplicationTest {

  private static WireMockServer wireMockServer;
  @Autowired
  private ReservationClient client;

  @BeforeAll
  static void setup() {
    configureFor("localhost", 8080);
    wireMockServer = new WireMockServer();
    wireMockServer.start();
  }

  @AfterAll
  static void stop() {
    wireMockServer.stop();
  }

  @Test
  void contextLoads() {
    stubFor(
        get(urlEqualTo("/reservations"))
            .willReturn(okJson("[{\"id\": \"1\",\"reservationName\": \"consumer-test\"}]")));

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
