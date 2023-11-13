package com.modsen.driver.controller.openapi;

import com.modsen.driver.dto.DriverResponse;
import com.modsen.driver.dto.ErrorResponse;
import com.modsen.driver.dto.RatingResponse;
import com.modsen.driver.dto.ScoreRequest;
import com.modsen.driver.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RatingControllerOpenApi {


    @Operation(
            tags = "Rating",
            summary = "Add score to driver",
            description = "Add new score to driver",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ScoreRequest.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "score" : 5
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Response when score added successfully",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "averageRating": 3.67,
                                                        "totalRatings": 9
                                                    }                                            
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RatingResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Response when request body is invalid ",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "timestamp": "2023-11-13T14:45:23.405+00:00",
                                                        "status": 400,
                                                        "error": "Bad Request",
                                                        "path": "/api/v1/ratings/5"
                                                    }                                      
                                                    """
                                    ),
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<RatingResponse> addScore(Integer driverId, ScoreRequest request);

    @Operation(
            tags = "Rating",
            summary = "Get rating",
            description = "Get rating by driver id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DriverResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "averageRating": 4.5,
                                                        "totalRatings": 5
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Rating with such driver id not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "statusCode": 404,
                                                                "errorMessage": "Rating with such driverId not found, driverId=1123"
                                                            }
                                                            """
                                            )
                                    }

                            )
                    )
            }
    )
    ResponseEntity<RatingResponse> getRating(Integer driverId);
}
