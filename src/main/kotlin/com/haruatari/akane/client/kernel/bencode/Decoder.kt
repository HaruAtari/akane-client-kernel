package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.decoder.MetaInfoDecoder
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfoInterface
import java.io.InputStream

class Decoder : DecoderInterface {
    override fun decodeMetaInfo(stream: InputStream): MetaInfoInterface {
        return MetaInfoDecoder(stream).decode()
    }
}