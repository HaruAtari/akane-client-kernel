package com.haruatari.akane.client.kernel.bencode.dto.announce

data class Announce(
    val failureReason: String?,
    val interval: Int?,
    val peers: List<Peer>
) {
    val isSuccess: Boolean = failureReason == null
}