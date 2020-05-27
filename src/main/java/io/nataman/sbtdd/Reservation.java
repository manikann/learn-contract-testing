package io.nataman.sbtdd;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@With
@Builder
public class Reservation {

  String id;

  @NotBlank(message = "name is mandatory")
  String name;
}
