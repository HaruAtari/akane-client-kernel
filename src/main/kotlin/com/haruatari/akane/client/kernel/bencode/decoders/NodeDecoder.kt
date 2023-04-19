package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException

internal abstract class NodeDecoder<T>(protected val reader: Reader) {
    abstract fun decode(): T

    protected fun generateException(message: String): DecoderException {
        return DecoderException(message + " Position: " + reader.getLastReadingPosition())
    }
}