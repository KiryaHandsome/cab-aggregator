package com.modsen.passenger.controller.openapi;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.ErrorResponse;
import com.modsen.passenger.dto.response.PassengerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PassengerControllerOpenApi {

    @Operation(
            tags = "Passenger",
            summary = "Get passenger by id",
            description = "Get passenger by specified id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PassengerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Passenger with such id not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<PassengerResponse> getPassenger(Integer id);

    @Operation(
            tags = "Passenger",
            summary = "Get passengers",
            description = "Get passengers with pagination",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10"),
                    @Parameter(name = "sort", description = "Sorting by field", example = "id")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                                            {
                                                 "content": [
                                                     {
                                                         "id": 2,
                                                         "name": "Alice",
                                                         "surname": "Smith",
                                                         "email": "alice.smith@example.com",
                                                         "phoneNumber": "+987654321"
                                                     },
                                                     {
                                                         "id": 3,
                                                         "name": "bob",
                                                         "surname": "jons",
                                                         "email": "bob.johnson@example.com",
                                                         "phoneNumber": "+111223344"
                                                     }
                                                 ],
                                                 "pageable": {
                                                     "pageNumber": 0,
                                                     "pageSize": 2,
                                                     "sort": {
                                                         "sorted": true,
                                                         "empty": false,
                                                         "unsorted": false
                                                     },
                                                     "offset": 0,
                                                     "paged": true,
                                                     "unpaged": false
                                                 },
                                                 "last": false,
                                                 "totalPages": 6,
                                                 "totalElements": 11,
                                                 "sort": {
                                                     "sorted": true,
                                                     "empty": false,
                                                     "unsorted": false
                                                 },
                                                 "first": true,
                                                 "size": 2,
                                                 "number": 0,
                                                 "numberOfElements": 2,
                                                 "empty": false
                                             }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PassengerResponse>> getAllPassengers(Pageable pageable);

    @Operation(
            tags = "Passenger",
            summary = "Partial update passenger",
            description = "Partially update passenger with specified ID. All values are optional.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = PassengerUpdate.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "name",
                                                "surname" : "jammy",
                                                "email" : "email@asdf.com",
                                                "phoneNumber": "12347819231239"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated passenger",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 3,
                                                        "name": "name",
                                                        "surname": "jammy",
                                                        "email": "email@asdf.com",
                                                        "phoneNumber": "12347819231239"
                                                    }                                               
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PassengerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Response when request body is invalid ",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 400,
                                                        "errors": [
                                                            "Phone number must be between 10 and 15 characters",
                                                            "Invalid email format",
                                                            "Name size must be between 2 and 255",
                                                            "Surname size must be between 2 and 255"
                                                        ]
                                                    }                                            
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PassengerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Passenger not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<PassengerResponse> updatePassenger(Integer id, PassengerUpdate request);

    @Operation(
            tags = "Passenger",
            summary = "Create new passenger",
            description = "Create new passenger",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = PassengerUpdate.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "name",
                                                "surname" : "jammy",
                                                "email" : "email@asdf.com",
                                                "phoneNumber": "12347819231239"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Response when passenger is created successfully",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 3,
                                                        "name": "name",
                                                        "surname": "jammy",
                                                        "email": "email@asdf.com",
                                                        "phoneNumber": "12347819231239"
                                                    }                                               
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PassengerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Response when request body is invalid ",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 400,
                                                        "errors": [
                                                            "Email must not be null",
                                                            "Email must not be blank",
                                                            "Phone number must not be null",
                                                            "Name must not be null",
                                                            "Phone number must not be blank",
                                                            "Surname must not be null"
                                                        ]
                                                    }                                        
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PassengerResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<PassengerResponse> createPassenger(PassengerCreate request);

    @Operation(
            tags = "Passenger",
            summary = "Delete passenger",
            description = "Delete passenger by specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Passenger deleted"
    )
    ResponseEntity<?> deletePassenger(Integer id);
}
