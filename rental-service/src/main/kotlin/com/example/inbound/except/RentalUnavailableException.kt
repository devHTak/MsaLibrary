package com.example.inbound.except

import java.lang.RuntimeException

class RentalUnavailableException(message: String): RuntimeException(message) {
}