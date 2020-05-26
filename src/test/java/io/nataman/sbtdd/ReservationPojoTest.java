package io.nataman.sbtdd;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Test for {@link io.nataman.sbtdd.Reservation }
 */
public class ReservationPojoTest {

  @Test
  public void create() {
    var r = Reservation.builder().id("1").name("name").build();
    assertThat(r.getId()).isEqualTo("1");
  }
}
