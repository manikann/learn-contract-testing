package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link Reservation }
 */
public class ReservationPojoTest {

  @Test
  void create() {
    var r = Reservation.builder().id("1").name("name").build();
    assertThat(r.getId()).isEqualTo("1");
  }
}
