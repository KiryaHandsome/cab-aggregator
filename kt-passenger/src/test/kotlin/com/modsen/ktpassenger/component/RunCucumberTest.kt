package com.modsen.ktpassenger.component

import com.modsen.ktpassenger.KtPassengerApplication
import com.modsen.ktpassenger.integration.BaseIntegrationTest
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest


@RunWith(Cucumber::class)
@CucumberContextConfiguration
@CucumberOptions(features = ["classpath:features"])
@SpringBootTest(
    classes = [KtPassengerApplication::class, RunCucumberTest::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RunCucumberTest : BaseIntegrationTest()
