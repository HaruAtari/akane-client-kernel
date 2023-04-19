package com.haruatari.akane.client.kernel.bencode.decoder

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.announce.Peer
import com.haruatari.akane.client.kernel.bencode.dto.announce.Tracker
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import com.haruatari.akane.client.kernel.bencode.tokenizer.TokenizerFacade
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.*
import java.io.InputStream

internal class AnnounceTrackerDecoder(stream: InputStream) {
    private val reader: Reader

    init {
        reader = Reader(stream)
    }

    fun decode(): Tracker {
        val root = TokenizerFacade(reader).tokenize()
        if (root !is DictionaryToken) {
            throw DecoderException("The root node should be a dictionary.")
        }

        val failureReasonToken = root.getValue()["failure reason"]

        if (failureReasonToken != null) {
            if (failureReasonToken !is StringToken) {
                throw DecoderException("Tracker response contains the 'failure reason' field but it's not a string.")
            }

            return Tracker(
                failureReason = failureReasonToken.getValue(),
                interval = null,
                peers = emptyList()
            );
        }

        return Tracker(
            failureReason = null,
            interval = decodeInterval(root.getValue()),
            peers = decodePeers(root.getValue())
        )
    }

    private fun decodeInterval(root: Map<String, Token>): Int {
        val node = root["interval"]
        if (node == null || node !is IntegerToken) {
            throw DecoderException("The root dictionary should contains the 'interval' integer element.")
        }

        return node.getValue()
    }

    private fun decodePeers(root: Map<String, Token>): List<Peer> {
        val node = root["peers"]
        if (node == null || node !is ListToken) {
            throw DecoderException("The root dictionary should contains the 'peers' list element.")
        }

        val result = mutableListOf<Peer>();
        for (item in node.getValue()) {
            if (item !is DictionaryToken) {
                throw DecoderException("The peers list should contains only dictionary elements.")
            }

            val ip = item.getValue()["ip"]
            if (ip == null || ip !is StringToken) {
                throw DecoderException("The peers should contains the 'ip' string element.")
            }

            val port = item.getValue()["port"]
            if (port == null || port !is IntegerToken) {
                throw DecoderException("The peers should contains the 'port' integer element.")
            }

            val id = item.getValue()["id"]
            if (id != null && id !is StringToken) {
                throw DecoderException("The peers.id should be a string element.")
            }

            result.add(
                Peer(
                    ip = ip.getValue(),
                    port = port.getValue(),
                    id = if (id is StringToken) id.getValue() else null
                )
            )
        }

        return result
    }
}