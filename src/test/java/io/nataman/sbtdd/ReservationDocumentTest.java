package io.nataman.sbtdd;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@DataMongoTest
@Log4j2
@RunWith(SpringRunner.class)
public class ReservationDocumentTest {

  @Autowired
  private ReservationRepository reservationRepository;

  @Test
  public void persist() {
    var reservation = Reservation.builder().name("unit-test").build();
    var save = reservationRepository.save(reservation);
    StepVerifier.create(save)
        .expectNextMatches(r -> r.getId() != null && r.getName().equals("unit-test"))
        .verifyComplete();
  }
}
