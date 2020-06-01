package io.nataman.sbtdd.pact;

import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.nataman.sbtdd.Reservation;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureJsonTesters
@Log4j2
@PactTestFor(providerName = "ReservationsProvider", port = "5678")
public class ReservationApiTest {

  private final Reservation testData = new Reservation("1", "pact-test");
  private WebTestClient webTestClient;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JacksonTester<Reservation> jacksonTester;

  @BeforeEach
  void checkInfrastructure(MockServer mockServer) {
    webTestClient = WebTestClient.bindToServer().baseUrl(mockServer.getUrl()).build();

    assertThat(mockServer).isNotNull();
    assertThat(webTestClient).isNotNull();
    assertThat(jacksonTester).isNotNull();
  }

  @SneakyThrows
  String responseBody() {
    var jsonContent = jacksonTester.write(testData);
    log.info("responseBody: {}", jsonContent);
    return jsonContent.getJson();
  }

  @Pact(consumer = "ReservationConsumer")
  RequestResponsePact reservations(PactDslWithProvider builder) {
    var headers = Collections.singletonMap("Content-Type", "application/json");
    var my_expected_state = builder.given("my expected state");
    var request =
        my_expected_state
            .uponReceiving("request for reservations")
            .path("/reservations")
            .method("GET");
    var response = request.willRespondWith().status(200).headers(headers).body(this::responseBody);
    return response.toPact();
  }

  @Test
  @PactTestFor(pactMethod = "reservations")
  @SneakyThrows
  void testReservations() {
    var response = webTestClient.get().uri("/reservations").exchange();
    response.expectHeader().contentType(MediaType.APPLICATION_JSON);
    response.expectStatus().is2xxSuccessful();
    var reservationFlux = response.returnResult(Reservation.class).getResponseBody();
    StepVerifier.create(reservationFlux.log("stepverifier")).expectNextCount(1).verifyComplete();
  }
}
