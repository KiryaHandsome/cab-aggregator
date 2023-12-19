package com.modsen.ktpassenger.integration

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@ActiveProfiles("test")
@Testcontainers
open class BaseIntegrationTest {

    companion object {
        private const val POSTGRES_IMAGE_NAME = "postgres:15-alpine"

        private val postgresContainer = PostgreSQLContainer(
            DockerImageName.parse(
                POSTGRES_IMAGE_NAME
            )
        )

        @JvmStatic
        @DynamicPropertySource
        fun init(registry: DynamicPropertyRegistry) {
            postgresContainer.start()
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.datasource.password") { postgresContainer.password }
            registry.add("spring.datasource.username") { postgresContainer.username }
        }
    }
}
