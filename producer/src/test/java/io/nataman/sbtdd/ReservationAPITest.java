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
import reactor.core.publisher.Flux;

/**
 * Test for {@link ReservationRoutesConfiguration}
 */
@WebFluxTest
@Import({ReservationRoutesConfiguration.class})
@Log4j2
public class ReservationAPITest {

  // @Autowired WebTestClient webTestClient;

  @MockBean
  ReservationRepository mockRepository;

  @Autowired
  ReservationRoutesConfiguration httpConfiguration;

  @Test
  void get() {
    when(mockRepository.findAll())
        .thenReturn(Flux.just(Reservation.builder().id("1").name("unit-test...").build()));

    //@formatter:off
    given().
        standaloneSetup(httpConfiguration.routes()).
    when().
        get("/reservations").
    then().
        statusCode(200).
        body("name", equalTo("unit-test..."));
    //@formatter:on
  }
}
