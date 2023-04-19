package com.haruatari.akane.client.kernel.bencode.dto.announce

data class Tracker(
    val failureReason: String?,
    val interval: Int?,
    val peers: List<Peer>
) {
    fun isSuccess(): Boolean = failureReason == null
}