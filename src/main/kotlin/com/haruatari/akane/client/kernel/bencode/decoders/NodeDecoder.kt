package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.Node
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException

internal abstract class NodeDecoder(protected val reader: Reader) {
    protected val listBeginToken: Byte = 108 // l
    protected val dictionaryBeginToken: Byte = 100; // d
    protected val intBeginToken: Byte = 105; // i
    protected val endToken: Byte = 101 // e
    protected val stringDelimiterToken: Byte = 58 // :
    protected val numberTokens = byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)
    protected val minusToken: Byte = 45 // -

    internal fun buildNext(reader: Reader): NodeDecoder {
        val nextByte = reader.seeNextByte() ?: throw generateException("Unexpected end of file.");

        return when (nextByte) {
            listBeginToken -> ListNodeDecoder(reader)
            dictionaryBeginToken -> DictionaryNodeDecoder(reader)
            intBeginToken -> IntNodeDecoder(reader)
            in numberTokens -> StringNodeDecoder(reader)
            else -> throw generateException("Undefined node's first char.")
        }
    }

    abstract fun decode(): Node

    protected fun generateException(message: String): DecoderException {
        return DecoderException(message + " Position: " + reader.getLastReadingPosition())
    }
}