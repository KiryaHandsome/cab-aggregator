package com.modsen.ktpassenger.service.impl

import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import com.modsen.ktpassenger.exception.EmailAlreadyExistsException
import com.modsen.ktpassenger.exception.PassengerNotFoundException
import com.modsen.ktpassenger.exception.PhoneNumberAlreadyExistsException
import com.modsen.ktpassenger.model.Passenger
import com.modsen.ktpassenger.repository.PassengerRepository
import com.modsen.ktpassenger.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*


@ExtendWith(MockitoExtension::class)
class PassengerServiceImplTest {
    @MockK
    lateinit var passengerRepository: PassengerRepository

    @InjectMockKs
    lateinit var passengerService: PassengerServiceImpl

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun findById_shouldThrowPassengerNotFoundException() {
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns Optional.empty()

        assertThrows(PassengerNotFoundException::class.java) { passengerService.findById(TestData.PASSENGER_ID) }

        io.mockk.verify { passengerRepository.findById(TestData.PASSENGER_ID) }
    }

    @Test
    fun findById_shouldReturnExpectedResponse() {
        val expected: PassengerResponse = TestData.defaultPassengerResponse()
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns Optional.of(passenger)

        val actual: PassengerResponse = passengerService.findById(TestData.PASSENGER_ID)

        assertThat(actual).isEqualTo(expected)
        io.mockk.verify { passengerRepository.findById(TestData.PASSENGER_ID) }
    }

    @Test
    fun findAll_shouldReturnExpectedPage() {
        val content: List<Passenger> = listOf(TestData.defaultPassenger(), TestData.defaultPassenger())
        val pageable = Pageable.ofSize(2)
        every { passengerRepository.findAll(pageable) } returns PageImpl(content)

        val actual: Page<PassengerResponse> = passengerService.findAll(pageable)

        assertThat(actual).hasSize(2)
        assertThat(actual).contains(TestData.defaultPassengerResponse())
        io.mockk.verify { passengerRepository.findAll(pageable) }
    }

    @Test
    fun update_shouldThrowPassengerNotFoundException() {
        val request = PassengerUpdate(null, null, null, null)
        val stub: Optional<Passenger> = Optional.empty()
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns stub

        assertThrows<PassengerNotFoundException> {
            passengerService.update(TestData.PASSENGER_ID, request)
        }

        io.mockk.verify { passengerRepository.findById(TestData.PASSENGER_ID) }
    }

    @Test
    fun update_shouldThrowEmailAlreadyExistsException() {
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns Optional.of(passenger)
        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.of(passenger)

        assertThrows<EmailAlreadyExistsException> {
            passengerService.update(TestData.PASSENGER_ID, TestData.defaultPassengerUpdate())
        }

        io.mockk.verify { passengerRepository.findById(eq(TestData.PASSENGER_ID)) }
        io.mockk.verify { passengerRepository.findByEmail(eq(TestData.EMAIL)) }
    }

    @Test
    fun update_shouldThrowPhoneNumberAlreadyExistsException() {
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns Optional.of(passenger)
        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.empty()
        every { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) } returns Optional.of(passenger)

        assertThrows<PhoneNumberAlreadyExistsException> {
            passengerService.update(
                TestData.PASSENGER_ID,
                TestData.defaultPassengerUpdate()
            )
        }

        io.mockk.verify { passengerRepository.findById(TestData.PASSENGER_ID) }
        io.mockk.verify { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) }
    }

    @Test
    fun update_shouldSaveUpdatedPassenger() {
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findById(TestData.PASSENGER_ID) } returns Optional.of(passenger)
        every { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) } returns Optional.empty()
        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.empty()
        every { passengerRepository.save(passenger) } returns passenger

        passengerService.update(TestData.PASSENGER_ID, TestData.defaultPassengerUpdate())

        io.mockk.verify { passengerRepository.findById(TestData.PASSENGER_ID) }
        io.mockk.verify { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) }
        io.mockk.verify { passengerRepository.findByEmail(TestData.EMAIL) }
        io.mockk.verify { passengerRepository.save(passenger) }
    }

    @Test
    fun deleteById_shouldCallDeleteMethod() {
        every { passengerRepository.deleteById(TestData.PASSENGER_ID) } returns Unit

        passengerService.deleteById(TestData.PASSENGER_ID)

        io.mockk.verify { passengerRepository.deleteById(TestData.PASSENGER_ID) }
    }

    @Test
    fun create_shouldReturnCreatedObject() {
        val passenger = TestData.defaultPassenger()
        val expected = TestData.defaultPassengerResponse()

        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.empty()
        every { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) } returns Optional.empty()
        every { passengerRepository.save(passenger.copy(id = null)) } answers {
            val savedPassenger = arg<Passenger>(0)
            savedPassenger.id = TestData.PASSENGER_ID
            savedPassenger
        }

        val actual = passengerService.create(TestData.defaultPassengerCreate())

        assertThat(actual).isEqualTo(expected)
        io.mockk.verify { passengerRepository.save(passenger) }
    }

    @Test
    fun create_shouldThrowEmailAlreadyExistsException() {
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findByEmail(TestData.PHONE_NUMBER) } returns Optional.empty()
        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.of(passenger)

        assertThrows<EmailAlreadyExistsException> {
            passengerService.create(TestData.defaultPassengerCreate())
        }

        io.mockk.verify { passengerRepository.findByEmail(TestData.EMAIL) }
    }

    @Test
    fun create_shouldThrowPhoneNumberAlreadyExistsException() {
        val passenger: Passenger = TestData.defaultPassenger()
        every { passengerRepository.findByEmail(TestData.EMAIL) } returns Optional.empty()
        every { passengerRepository.findByPhoneNumber(TestData.PHONE_NUMBER) } returns Optional.of(passenger)

        assertThrows<PhoneNumberAlreadyExistsException> {
            passengerService.create(TestData.defaultPassengerCreate())
        }

        io.mockk.verify { passengerRepository.findByPhoneNumber(eq(TestData.PHONE_NUMBER)) }
    }
}
