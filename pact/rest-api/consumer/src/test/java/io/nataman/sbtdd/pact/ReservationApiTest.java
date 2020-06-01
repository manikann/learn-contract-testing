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
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
@Log4j2
@PactTestFor(providerName = "ReservationsProvider", port = "5678")
public class ReservationApiTest {

  Reservation testData = new Reservation("1", "pact-test");
  @Autowired private WebTestClient webTestClient;
  @Autowired private JacksonTester jacksonTester;

  @BeforeEach
  void checkInfrastructure(MockServer mockServer) {
    log.info("mackServer: {}", mockServer);
    log.info("jacksonTester: {}", jacksonTester);
    log.info("webTestClient: {}", webTestClient);
    assertThat(mockServer).isNotNull();
    assertThat(jacksonTester).isNotNull();
    assertThat(webTestClient).isNotNull();
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
  void testReservations(MockServer mockServer) {
    var reservationFlux =
        webTestClient
            .get()
            .uri(mockServer.getUrl() + "/reservations")
            .exchange()
            .returnResult(Reservation.class)
            .getResponseBody();

    StepVerifier.create(reservationFlux).expectNext(testData).verifyComplete();
  }
}
