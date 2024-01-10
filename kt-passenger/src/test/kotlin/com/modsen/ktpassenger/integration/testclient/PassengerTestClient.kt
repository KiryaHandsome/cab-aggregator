package com.modsen.ktpassenger.integration.testclient

import com.modsen.ktpassenger.dto.*
import com.modsen.ktpassenger.util.HostUtil
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.http.HttpStatus

class PassengerTestClient private constructor(port: Int) {
    private val baseUrl: String

    init {
        baseUrl = HostUtil.host + port + "/api/v1/passengers"
    }

    fun getPassengerById(id: Int?): PassengerResponse {
        return RestAssured.`when`()["$baseUrl/{id}", id]
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .extract()
            .`as`(PassengerResponse::class.java)
    }

    fun getPassengers(pageNumber: Int?, pageSize: Int?): ValidatableResponse {
        return RestAssured.given()
            .queryParam("page", pageNumber)
            .queryParam("size", pageSize)
            .`when`()[baseUrl]
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
    }

    fun updatePassenger(driverId: Int?, requestBody: PassengerUpdate?): PassengerResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .patch("$baseUrl/{id}", driverId)
            .then()
            .log().all()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`<PassengerResponse>(object :
                TypeRef<PassengerResponse?>() {})
    }

    fun updatePassengerForValidationError(driverId: Int?, requestBody: PassengerUpdate?): ValidationErrorResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .patch("$baseUrl/{id}", driverId)
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract()
            .`as`<ValidationErrorResponse>(ValidationErrorResponse::class.java)
    }

    fun createPassenger(requestBody: PassengerCreate?): PassengerResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(baseUrl)
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .`as`(PassengerResponse::class.java)
    }

    fun createPassengerForConflict(requestBody: PassengerCreate?): ErrorResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(baseUrl)
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.CONFLICT.value())
            .extract()
            .`as`(ErrorResponse::class.java)
    }

    fun deletePassengerById(driverId: Int?): ValidatableResponse {
        return RestAssured.`when`()
            .delete("$baseUrl/{id}", driverId)
            .then()
            .log().all()
    }

    fun getPassengerByIdForError(passengerId: Int): ErrorResponse {
        return RestAssured.`when`()["$baseUrl/{id}", passengerId]
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .extract()
            .`as`(ErrorResponse::class.java)
    }

    fun updatePassengerForNotFound(passengerId: Int, requestBody: PassengerUpdate?): ErrorResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .patch("$baseUrl/{id}", passengerId)
            .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(ErrorResponse::class.java)
    }

    fun updatePassengerForConflict(passengerId: Int, requestBody: PassengerUpdate?): ErrorResponse {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .patch("$baseUrl/{id}", passengerId)
            .then()
            .log().all()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.CONFLICT.value())
            .extract()
            .`as`(ErrorResponse::class.java)
    }

    companion object {
        fun withPort(port: Int): PassengerTestClient {
            return PassengerTestClient(port)
        }
    }
}
