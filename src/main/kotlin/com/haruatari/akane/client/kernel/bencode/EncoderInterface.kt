package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import java.io.InputStream

interface EncoderInterface {
    fun encodeMetaInfo(metaInfo: MetaInfo): InputStream;
}