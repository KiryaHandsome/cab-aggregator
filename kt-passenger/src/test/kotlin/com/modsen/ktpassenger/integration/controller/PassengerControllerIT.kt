package com.modsen.ktpassenger.integration.controller

import com.modsen.ktpassenger.dto.ErrorResponse
import com.modsen.ktpassenger.dto.PassengerCreate
import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import com.modsen.ktpassenger.dto.ValidationErrorResponse
import com.modsen.ktpassenger.dto.toResponse
import com.modsen.ktpassenger.integration.BaseIntegrationTest
import com.modsen.ktpassenger.integration.testclient.PassengerTestClient
import com.modsen.ktpassenger.repository.PassengerRepository
import com.modsen.ktpassenger.util.TestData
import com.modsen.ktpassenger.util.TestEntities
import jakarta.annotation.PostConstruct
import java.util.stream.Stream
import lombok.SneakyThrows
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


@Sql(
    scripts = ["classpath:sql/delete-all.sql", "classpath:sql/create-data.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PassengerControllerIT : BaseIntegrationTest() {
    @LocalServerPort
    private val port: Int? = null

    @Autowired
    private lateinit var passengerRepository: PassengerRepository
    private lateinit var passengerTestClient: PassengerTestClient

    @PostConstruct
    fun setUp() {
        passengerTestClient = PassengerTestClient.withPort(port ?: 8080)
    }

    @Test
    fun passenger_shouldReturnExpectedPassenger() {
        val passengerId = TestEntities.JOHN_ID
        val expected = TestEntities.johnDoe().toResponse()

        val actual: PassengerResponse = passengerTestClient.getPassengerById(passengerId)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun passenger_shouldReturnNotFoundCode() {
        val passengerId = 100

        val actual: ErrorResponse = passengerTestClient.getPassengerByIdForError(passengerId)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun allPassengers_shouldReturnPageOfPassengersWithExpectedSizes() {
        val pageNumber = 0
        val pageSize = 2
        val totalElements = 3

        passengerTestClient.getPassengers(pageNumber, pageSize)
            .body("totalElements", CoreMatchers.`is`(totalElements))
    }

    @Test
    @SneakyThrows
    fun updatePassenger_shouldReturnUpdatedPassenger() {
        val requestBody = PassengerUpdate(
            TestData.NEW_NAME,
            null,
            TestData.NEW_EMAIL,
            null
        )
        val expected = PassengerResponse(
            TestEntities.JOHN_ID,
            TestData.NEW_NAME,
            TestEntities.JOHN_SURNAME,
            TestData.NEW_EMAIL,
            TestEntities.JOHN_PHONE_NUMBER
        )
        val passengerId = TestEntities.JOHN_ID

        val actual: PassengerResponse = passengerTestClient.updatePassenger(passengerId, requestBody)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updatePassenger_shouldReturnNotFoundCode() {
        val passengerId = 100
        val request = TestData.defaultPassengerUpdate()

        val actual: ErrorResponse = passengerTestClient.updatePassengerForNotFound(passengerId, request)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    @SneakyThrows
    fun updatePassenger_withExistingEmail_shouldReturnConflictStatus() {
        val passengerId = 2
        val requestBody = PassengerUpdate(null, null, TestEntities.JOHN_EMAIL, null)

        val actual: ErrorResponse = passengerTestClient.updatePassengerForConflict(passengerId, requestBody)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidPassengerUpdatesForBadRequest")
    fun updatePassenger_shouldReturnBadRequestStatus(requestBody: PassengerUpdate?) {
        val passengerId = 2

        val actual: ValidationErrorResponse = passengerTestClient
            .updatePassengerForValidationError(passengerId, requestBody)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    @SneakyThrows
    fun createPassenger_withExistingEmail_shouldReturnConflictStatus() {
        val requestBody = PassengerCreate(
            TestData.NAME,
            TestData.SURNAME,
            TestEntities.JOHN_EMAIL,
            TestData.NEW_PHONE_NUMBER
        )

        val actual: ErrorResponse = passengerTestClient.createPassengerForConflict(requestBody)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @Test
    @SneakyThrows
    fun createPassenger_withExistingPhoneNumber_shouldReturnConflictStatus() {
        val requestBody = PassengerCreate(
            TestData.NAME,
            TestData.SURNAME,
            TestData.NEW_EMAIL,
            TestEntities.JOHN_PHONE_NUMBER
        )

        val actual: ErrorResponse = passengerTestClient.createPassengerForConflict(requestBody)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @Test
    @SneakyThrows
    fun createPassenger_shouldReturnCreatedPassenger() {
        val requestBody: PassengerCreate = TestData.newPassengerCreate()
        val expectedId = 4
        val expected = PassengerResponse(
            expectedId,
            TestData.NEW_NAME,
            TestData.NEW_SURNAME,
            TestData.NEW_EMAIL,
            TestData.NEW_PHONE_NUMBER
        )

        val actual: PassengerResponse = passengerTestClient.createPassenger(requestBody)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun deletePassenger_shouldReturnNoContentStatus() {
        val passengerId = 1

        passengerTestClient.deletePassengerById(passengerId)
            .statusCode(HttpStatus.NO_CONTENT.value())

        assertThat(passengerRepository.findById(passengerId)).isNotPresent()
    }

    companion object {

        @JvmStatic
        private fun invalidPassengerUpdatesForBadRequest(): Stream<PassengerUpdate> {
            return Stream.of(
                PassengerUpdate("", null, null, null),  // invalid name
                PassengerUpdate(" ", null, null, null),  // invalid name
                PassengerUpdate("a", null, null, null),  // invalid name
                PassengerUpdate(null, "", null, null),  // invalid surname
                PassengerUpdate(null, " ", null, null),  // invalid surname
                PassengerUpdate(null, "a", null, null),  // invalid surname
                PassengerUpdate(null, null, "", null),  // invalid email
                PassengerUpdate(null, null, " ", null),  // invalid email
                PassengerUpdate(null, null, "email", null),  // invalid email
                PassengerUpdate(null, null, "email@", null),  // invalid email
                PassengerUpdate(null, null, "email@com", null),  // invalid email
                PassengerUpdate(null, null, null, "aaa"),  // invalid phone
                PassengerUpdate(null, null, null, ""),  // invalid phone
                PassengerUpdate(null, null, null, " "),  // invalid phone
                PassengerUpdate(null, null, null, "21381914"),  // invalid phone
                PassengerUpdate(null, null, null, "+1234567890123") // invalid phone
            )
        }
    }
}