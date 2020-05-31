package io.nataman.sbtdd;

import javax.validation.constraints.NotBlank;
import lombok.Value;

@Value
class Reservation {

  @NotBlank String id;
  @NotBlank String name;
}
