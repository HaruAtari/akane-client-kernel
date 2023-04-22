package com.haruatari.akane.client.kernel.bencode.dto.announce

data class Announce(
    override val failureReason: String?,
    override val interval: Int?,
    override val peers: List<PeerInterface>
) : AnnounceInterface {
    override val isSuccess: Boolean = failureReason == null
}