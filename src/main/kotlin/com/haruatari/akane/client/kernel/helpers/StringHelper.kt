package com.haruatari.akane.client.kernel.helpers

import java.util.*
import kotlin.streams.asSequence

internal object StringHelper {

    fun generateRandomString(length: Number): String {
        val source = "abcdefghigklmnopqrstuvwxyz0123456789"

        return Random()
            .ints(length.toLong(), 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
    }
}