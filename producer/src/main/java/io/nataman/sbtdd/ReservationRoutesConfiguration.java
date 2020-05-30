package io.nataman.sbtdd;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class ReservationRoutesConfiguration {

  private final ReservationRepository reservationRepository;

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route().GET("/reservations", this::handleGet).build();
  }

  private Mono<ServerResponse> handleGet(ServerRequest request) {
    log.info("handleGet: {}", request);
    return ok().contentType(MediaType.APPLICATION_JSON)
        .body(reservationRepository.findAll(), Reservation.class);
  }
}
