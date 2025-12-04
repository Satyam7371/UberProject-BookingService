package org.example.uberbookingservice.services;

import org.example.uberbookingservice.dtos.CreateBookingDto;
import org.example.uberbookingservice.dtos.CreateBookingResponseDto;
import org.example.uberbookingservice.dtos.DriverLocationDto;
import org.example.uberbookingservice.dtos.NearbyDriversRequestDto;
import org.example.uberbookingservice.repositories.BookingRepository;
import org.example.uberbookingservice.repositories.PassengerRepository;
import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;                              // using this to make api call to locationService, can be done using retrofit as well

    // adding logger interface for logging i,e debug error ,warn, etc
    private final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private static final String LOCATION_SERVICE = "http://localhost:7777";

    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .passenger(passenger.get())
                .build();

        Booking newBooking = bookingRepository.save(booking);

        NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        // make an api call to locationService to fetch nearby driver
        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/location/nearby/drivers", request, DriverLocationDto[].class);

        // getting out the fetched drivers
        if(result.getStatusCode().is2xxSuccessful() && result.getBody()!=null) {
            List<DriverLocationDto> driverLocations = Arrays.asList(result.getBody());
            driverLocations.forEach(driverLocationDto -> {
                System.out.println("ID = " + driverLocationDto.getDriverId() + " lat = " + driverLocationDto.getLatitude() + " longi = " + driverLocationDto.getLongitude());
            });
        }

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }
}
