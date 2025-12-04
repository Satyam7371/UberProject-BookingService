package org.example.uberbookingservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationDto {

    String driverId;
    Double latitude;
    Double longitude;
}
