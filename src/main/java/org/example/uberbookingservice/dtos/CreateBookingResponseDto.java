package org.example.uberbookingservice.dtos;

import lombok.*;
import org.example.uberprojectentityservice.models.Driver;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponseDto {
    private long bookingId;

    private String bookingStatus;

    private Optional<Driver>  driver;
}
