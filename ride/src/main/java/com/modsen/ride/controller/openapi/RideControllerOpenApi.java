package com.modsen.ride.controller.openapi;

import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.WaitingRideResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RideControllerOpenApi {

    @Operation(
            tags = "Ride",
            summary = "Book a ride",
            description = "Book a ride for passenger",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "passengerId": 10,
                                                "from": "minsk",
                                                "to": "volkovysk"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    )
            }
    )
    ResponseEntity<?> bookRide(RideRequest rideRequest);

    @Operation(
            tags = "Ride",
            summary = "Get waiting rides",
            description = "Get rides that aren't picked up by drivers yet",
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "id": "65567291ca047d144a7bd6da",
                                                        "passengerId": 10,
                                                        "from": "minsk",
                                                        "to": "volovysk"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                        "unsorted": true,
                                                        "sorted": false,
                                                        "empty": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "totalPages": 1,
                                                "totalElements": 1,
                                                "last": true,
                                                "numberOfElements": 1,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                    "unsorted": true,
                                                    "sorted": false,
                                                    "empty": true
                                                },
                                                "first": true,
                                                "empty": false
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<Page<WaitingRideResponse>> getWaitingRides(Pageable pageable);

    @Operation(
            tags = "Ride",
            summary = "Get passenger's rides",
            description = "Get passenger's rides by passenger id",
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "id": "65567a71ca047d144a7bd6dc",
                                                        "driverId": 1,
                                                        "passengerId": 3,
                                                        "from": "Vitebsk",
                                                        "to": "Mogilev",
                                                        "startTime": "2023-11-16T20:24:17.247",
                                                        "finishTime": "2023-11-16T20:24:22.228"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                        "unsorted": true,
                                                        "sorted": false,
                                                        "empty": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "totalPages": 1,
                                                "totalElements": 1,
                                                "last": true,
                                                "numberOfElements": 1,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                    "unsorted": true,
                                                    "sorted": false,
                                                    "empty": true
                                                },
                                                "first": true,
                                                "empty": false
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<Page<RideDto>> getPassengerRides(Integer passengerId, Pageable pageable);

    @Operation(
            tags = "Ride",
            summary = "Get driver's rides",
            description = "Get driver's rides by driver id",
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "id": "65567a71ca047d144a7bd6dc",
                                                        "driverId": 1,
                                                        "passengerId": 3,
                                                        "from": "Vitebsk",
                                                        "to": "Mogilev",
                                                        "startTime": "2023-11-16T20:24:17.247",
                                                        "finishTime": "2023-11-16T20:24:22.228"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                        "unsorted": true,
                                                        "sorted": false,
                                                        "empty": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "totalPages": 1,
                                                "totalElements": 1,
                                                "last": true,
                                                "numberOfElements": 1,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                    "unsorted": true,
                                                    "sorted": false,
                                                    "empty": true
                                                },
                                                "first": true,
                                                "empty": false
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<Page<RideDto>> getDriverRides(Integer driverId, Pageable pageable);

    @Operation(
            tags = "Ride",
            summary = "Start a ride",
            description = "Start a ride for passenger",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "driverId" : 2
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": "655c9a57a9c41e0a6d8e6005",
                                                        "driverId": 2,
                                                        "passengerId": 9,
                                                        "from": "Nemiga",
                                                        "to": "Smolyachkova",
                                                        "cost": 127.49305,
                                                        "startTime": "2023-11-21T14:54:17.751881231",
                                                        "finishTime": null
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                         "statusCode": 404,
                                                         "errorMessage": "WaitingRide with id=655c9a57a9c41e0a6d8e600 not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<RideDto> startRide(String waitingRideId, RideStart rideDto);

    @Operation(
            tags = "Ride",
            summary = "End a ride",
            description = "End a ride for passenger",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": "655c9df0c43d7f04b0bae9ae",
                                                        "driverId": 2,
                                                        "passengerId": 9,
                                                        "from": "Nemiga",
                                                        "to": "Smolyachkova",
                                                        "cost": 141.69,
                                                        "startTime": "2023-11-21T15:15:42.385",
                                                        "finishTime": "2023-11-21T15:15:49.324321137"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 404,
                                                        "errorMessage": "Ride with id=asafs not found."
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<RideDto> endRide(String rideId);
}
