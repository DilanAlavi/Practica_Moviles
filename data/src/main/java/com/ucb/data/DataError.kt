package com.ucb.data

sealed interface DataError {

    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MAY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        INSUFFUCIENTE_FUNDS,
        UNKNOWN
    }
}