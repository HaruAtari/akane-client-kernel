package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import java.io.InputStream

interface DecoderInterface {
    fun decodeMetaInfo(stream: InputStream): MetaInfo
}