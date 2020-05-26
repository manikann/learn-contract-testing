package io.nataman.sbtdd;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@With
@Builder
public class Reservation {

  @NotBlank String id;
  @NotBlank String name;
}
