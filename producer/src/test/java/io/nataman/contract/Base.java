package io.nataman.contract;

import static org.mockito.Mockito.when;

import io.nataman.sbtdd.ProducerApplication;
import io.nataman.sbtdd.Reservation;
import io.nataman.sbtdd.ReservationRepository;
import io.nataman.sbtdd.ReservationWebConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;

@Import(ReservationWebConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ProducerApplication.class)
public class Base {

  @MockBean ReservationRepository mockRepository;
  @LocalServerPort private int port;

  @BeforeEach
  void before() throws Exception {
    RestAssured.baseURI = "http://localhost:" + this.port;
    Reservation testData = Reservation.builder().id("1").name("contract-test...").build();
    when(mockRepository.findAll()).thenReturn(Flux.just(testData));
  }
}
