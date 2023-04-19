package com.haruatari.akane.client.kernel.exchange

import com.haruatari.akane.client.kernel.bencode.dto.announce.Peer
import com.haruatari.akane.client.kernel.exchange.storage.Storage

class Exchange(private val tracker: Tracker, private val storage: Storage) {
    enum class State {
        DOWNLOADING,
        SEEDING,
        INACTIVE,
        FAILED
    }

    var downloaded: Int = 0
        private set
    var uploaded: Int = 0
        private set
    var left = 0
        private set
    var state: State = State.INACTIVE
        private set
    private var peers: List<Peer> = emptyList()

    public fun start() {

    }

    public fun stop() {

    }
}