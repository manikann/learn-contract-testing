package io.nataman.sbtdd;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.hamcrest.Matchers.equalTo;
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
import reactor.test.StepVerifier;

/**
 * Test for {@link ReservationRoutesConfiguration}
 */
@WebFluxTest
@Import({ReservationRoutesConfiguration.class})
@Log4j2
public class ReservationAPITest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  ReservationRepository mockRepository;

  @Autowired
  ReservationRoutesConfiguration httpConfiguration;

  @Test
  void use_step_verifier() {
    Reservation testData = Reservation.builder().id("1").name("unit-test...").build();
    when(mockRepository.findAll()).thenReturn(Flux.just(testData));

    StepVerifier.create(
        webTestClient
            .get()
            .uri("/reservations")
            .exchange()
            .returnResult(Reservation.class)
            .getResponseBody())
        .expectSubscription()
        .expectNext(testData)
        .verifyComplete();
  }

  @Test
  void use_web_test_client() {
    Reservation testData = Reservation.builder().id("2").name("unit-test...").build();
    when(mockRepository.findAll()).thenReturn(Flux.just(testData));

    var response = webTestClient.get().uri("/reservations").exchange();

    // response assertions
    response.expectStatus().is2xxSuccessful();
    response.expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE);

    var responseBody = response.expectBody();
    responseBody.jsonPath("$.[0].name").isEqualTo("unit-test...");
    responseBody.jsonPath("$.[0].id").isEqualTo(2);
  }

  @Test
  void use_rest_assured() {
    Reservation testData = Reservation.builder().id("3").name("unit-test...").build();
    when(mockRepository.findAll()).thenReturn(Flux.just(testData));

    // given
    var spec = given().standaloneSetup(httpConfiguration.routes());

    // when
    var response = spec.when().get("/reservations").then();

    // then
    response
        .assertThat()
        .statusCode(200)
        .body("name[0]", equalTo("unit-test..."))
        .body("id[0]", equalTo("3"));
  }
}
