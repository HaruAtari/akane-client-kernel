package com.haruatari.akane.client.kernel.helpers

import java.io.InputStreamReader
import java.net.URL


object NetworkHelper {
    val checkIpUrl = URL("http://checkip.amazonaws.com")

    // TODO: Add backup method to work properly if the service is unavailable
    fun getCurrentIp(): String {
        return InputStreamReader(checkIpUrl.openStream()).use {
            it.readText();
        }.trim()
    }
}