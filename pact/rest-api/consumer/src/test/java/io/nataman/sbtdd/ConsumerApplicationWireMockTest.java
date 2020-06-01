package io.nataman.sbtdd;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = NONE)
@AutoConfigureWireMock
class ConsumerApplicationWireMockTest {

  @Autowired
  private ReservationApiClient client;

  @Test
  void contextLoads() {
    stubFor(
        get(urlEqualTo("/reservations"))
            .willReturn(okJson("[{\"id\": \"1\",\"name\": \"consumer-test\"}]")));

    Flux<Reservation> response = client.getAllReservations();
    StepVerifier.create(response)
        .assertNext(
            r -> {
              assertThat(r.getId()).isEqualTo("1");
              assertThat(r.getName()).isEqualTo("consumer-test");
            })
        .verifyComplete();
  }
}
