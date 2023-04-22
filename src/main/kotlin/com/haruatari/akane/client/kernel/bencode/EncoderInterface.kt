package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfoInterface
import java.io.InputStream

interface EncoderInterface {
    fun encodeMetaInfo(metaInfo: MetaInfoInterface): InputStream
}