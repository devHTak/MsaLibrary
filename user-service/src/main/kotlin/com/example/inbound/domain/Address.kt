package com.example.inbound.domain

import javax.persistence.Embeddable


@Embeddable
class Address(
    var city: String,
    var street: String,
    var zipCode: String
) {
    constructor(): this("", "", "")
}