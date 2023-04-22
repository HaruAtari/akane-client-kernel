package com.haruatari.akane.client.kernel.bencode.dto.announce

data class Peer(
    override val ip: String?,
    override val port: Int,
    override val id: String?
) : PeerInterface
