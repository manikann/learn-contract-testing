package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test for {@link Reservation }
 */
@Log4j2
public class ReservationPojoTest {

  @Test
  void builder_test() {
    var r = Reservation.builder().id("1").name("name").build();
    assertThat(r.getId()).isEqualTo("1");
  }

  @Test
  void reactive_test() {
    StepVerifier.create(Flux.just(Reservation.builder().id("2").name("flux-test").build()))
        .assertNext(
            r -> {
              assertThat(r.getId()).isEqualTo("2");
              assertThat(r.getName()).isEqualTo("flux-test");
            })
        .verifyComplete();
  }
}
