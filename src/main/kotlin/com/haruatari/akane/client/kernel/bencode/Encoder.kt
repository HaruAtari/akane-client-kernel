package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfoInterface
import com.haruatari.akane.client.kernel.bencode.encoder.MetaInfoEncoder
import java.io.InputStream

class Encoder : EncoderInterface {
    override fun encodeMetaInfo(metaInfo: MetaInfoInterface): InputStream {
        return MetaInfoEncoder().encode(metaInfo)
    }
}