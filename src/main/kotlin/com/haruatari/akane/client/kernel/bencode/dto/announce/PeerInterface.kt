package com.haruatari.akane.client.kernel.bencode.dto.announce

interface PeerInterface {
    val ip: String?
    val port: Int
    val id: String?
}