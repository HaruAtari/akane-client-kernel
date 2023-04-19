package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.StringNode

internal class DictionaryNodeDecoder(reader: Reader) : NodeDecoder(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_KEY,
        READ_VALUE,
        READ_END_TOKEN
    }

    override fun decode(): StringNode {
        return StringNode(ByteArray(0))
//        TODO("Not yet implemented")
    }
}