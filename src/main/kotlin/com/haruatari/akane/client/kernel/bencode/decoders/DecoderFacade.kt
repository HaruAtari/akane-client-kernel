package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.Node

internal class DecoderFacade(reader: Reader) : NodeDecoder(reader) {
    override fun decode(): Node {
        return buildDecoderForNextNode(reader).decode()
    }
}