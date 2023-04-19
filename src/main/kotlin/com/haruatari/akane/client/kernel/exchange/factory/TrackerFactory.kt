package com.haruatari.akane.client.kernel.exchange.factory

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import com.haruatari.akane.client.kernel.exchange.Tracker
import com.haruatari.akane.client.kernel.exchange.dto.PeerInfo

class TrackerFactory {
    fun build(metaInfo: MetaInfo, peerInfo: PeerInfo): Tracker {
        return Tracker(
            metaInfo.announce,
            peerInfo.ip,
            peerInfo.port,
            peerInfo.id,
            metaInfo.info.hash
        )
    }
}