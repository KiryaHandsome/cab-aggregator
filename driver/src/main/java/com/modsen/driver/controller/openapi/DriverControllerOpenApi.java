package com.modsen.driver.controller.openapi;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.dto.response.ErrorResponse;
import com.modsen.driver.dto.response.ValidationErrorResponse;
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

public interface DriverControllerOpenApi {

    @Operation(
            tags = "Driver",
            summary = "Get driver by id",
            description = "Get driver by specified id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 2,
                                                        "name": "vodila",
                                                        "surname": "vibe",
                                                        "email": "driver@gmail.com",
                                                        "phoneNumber": "5431283768912",
                                                        "status": "OFFLINE"
                                                    }
                                                    """
                                    ),
                                    schema = @Schema(implementation = DriverResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Driver with such id not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 404,
                                                        "errorMessage": "Driver with such id not found. id=22"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<DriverResponse> getDriver(Integer id);

    @Operation(
            tags = "Driver",
            summary = "Get drivers",
            description = "Get drivers with pagination",
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
                                                        "name": "vodila",
                                                        "surname": "vibe",
                                                        "email": "driver@gmail.com",
                                                        "phoneNumber": "5431283768912",
                                                        "status": "OFFLINE"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                        "sorted": false,
                                                        "unsorted": true,
                                                        "empty": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 1,
                                                "first": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                    "sorted": false,
                                                    "unsorted": true,
                                                    "empty": true
                                                },
                                                "numberOfElements": 1,
                                                "empty": false
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<DriverResponse>> getAllDrivers(Pageable pageable);

    @Operation(
            tags = "Driver",
            summary = "Partial update driver",
            description = "Partially update driver with specified ID. All values are optional.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = DriverUpdate.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "vodile",
                                                "surname": "vibe",
                                                "email": "email@gmail.com",
                                                "phoneNumber": "12345678999",
                                                "status": "AVAILABLE"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated driver",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 2,
                                                        "name": "vodile",
                                                        "surname": "vibe",
                                                        "email": "email@gmail.com",
                                                        "phoneNumber": "12345678999",
                                                        "status": "AVAILABLE"
                                                    }                                           
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DriverResponse.class)
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
                                                            "Invalid email format",
                                                            "Name must not be blank",
                                                            "Phone number must contain only digits",
                                                            "Surname must not be blank"
                                                        ]
                                                    }                                           
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Driver not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Response when email or phoneNumber already exist",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 409,
                                                        "errorMessage": "Email already exists. email=email@gmail.com"
                                                    }                                         
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
            }
    )
    ResponseEntity<DriverResponse> updateDriver(Integer id, DriverUpdate request);

    @Operation(
            tags = "Driver",
            summary = "Create new driver",
            description = "Create new driver",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = DriverUpdate.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 3,
                                                "name": "vodila",
                                                "surname": "vib1e",
                                                "email": "driver@gmaiwl.com",
                                                "phoneNumber": "54312837684912",
                                                "status": "OFFLINE"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Response when driver is created successfully",
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
                                    schema = @Schema(implementation = DriverResponse.class)
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
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Response when email or phone number already exist",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "statusCode": 409,
                                                        "errorMessage": "Email already exists. email=driver@gmaiwl.com"
                                                    }                                      
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DriverResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<DriverResponse> createDriver(DriverCreate request);

    @Operation(
            tags = "Driver",
            summary = "Delete driver",
            description = "Delete driver by specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Driver deleted"
    )
    ResponseEntity<?> deleteDriver(Integer id);
}
