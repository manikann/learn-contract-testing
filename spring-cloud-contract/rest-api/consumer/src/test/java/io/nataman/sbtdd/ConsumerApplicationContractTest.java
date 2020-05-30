package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = NONE, args = "--stubrunner.cloud.loadbalancer.enabled=false")
@AutoConfigureStubRunner(ids = "io.nataman:producer:+:8080", stubsMode = StubsMode.LOCAL)
class ConsumerApplicationContractTest {

  @Autowired private ReservationClient client;

  @Test
  void contextLoads() {
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