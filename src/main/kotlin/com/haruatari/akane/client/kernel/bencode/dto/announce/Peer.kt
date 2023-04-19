package com.haruatari.akane.client.kernel.bencode.dto.announce

data class Peer(
    val ip: String?,
    val port: Int,
    val id: String?
)
