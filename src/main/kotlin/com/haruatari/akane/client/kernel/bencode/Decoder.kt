package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.decoder.MetaInfoDecoder
import com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo.MetaInfo
import java.io.InputStream

class Decoder : DecoderInterface {
    override fun decodeMetaInfo(stream: InputStream): MetaInfo {
        return MetaInfoDecoder(Reader(stream)).decode()
    }
}