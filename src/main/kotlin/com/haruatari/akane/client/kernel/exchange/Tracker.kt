package com.haruatari.akane.client.kernel.exchange

import com.haruatari.akane.client.kernel.bencode.decoder.AnnounceDecoder
import com.haruatari.akane.client.kernel.bencode.dto.announce.Announce
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.Hash
import java.net.URL

class Tracker internal constructor(
    private val announceUrl: String,
    private val peerIp: String,
    private val peerPort: Int,
    private val peerId: String,
    private val infoHash: Hash
) {
    internal enum class Action(val value: String?) {
        STARTED("started"),
        COMPLETED("completed"),
        STOPPED("stopped"),
        NONE(null)
    }

    internal fun announce(action: Action, downloaded: Int, uploaded: Int, left: Int): Announce {
        val url = generateUrl(action, downloaded, uploaded, left)
        val response = URL(url).openStream()

        return AnnounceDecoder(response).decode()
    }

    private fun generateUrl(action: Action, downloaded: Int, uploaded: Int, left: Int): String {
        val sb = StringBuilder(announceUrl)
            .append("?info_hash=${infoHash.getUrlEncoded()}")
            .append("&peer_id=${peerId}")
            .append("&ip=${peerIp}")
            .append("&port=${peerPort}")
            .append("&uploaded=${uploaded}")
            .append("&downloaded=${downloaded}")
            .append("&left=${left}")

        if (action != Action.NONE) {
            sb.append("&action=${action}")
        }

        return sb.toString()
    }
}