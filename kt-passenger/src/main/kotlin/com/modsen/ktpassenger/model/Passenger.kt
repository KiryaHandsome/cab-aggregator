package com.modsen.ktpassenger.model

import jakarta.persistence.*

@Entity
@Table(name = "passengers")
data class Passenger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String,
)
