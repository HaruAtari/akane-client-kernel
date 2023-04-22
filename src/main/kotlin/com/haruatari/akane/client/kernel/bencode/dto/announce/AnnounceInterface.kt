package com.haruatari.akane.client.kernel.bencode.dto.announce

interface AnnounceInterface {
    val failureReason: String?
    val interval: Int?
    val peers: List<PeerInterface>
    val isSuccess: Boolean
}