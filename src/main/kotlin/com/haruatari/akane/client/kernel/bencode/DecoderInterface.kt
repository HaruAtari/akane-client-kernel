package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfoInterface
import java.io.InputStream

interface DecoderInterface {
    fun decodeMetaInfo(stream: InputStream): MetaInfoInterface
}