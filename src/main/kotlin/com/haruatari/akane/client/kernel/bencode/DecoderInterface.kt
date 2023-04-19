package com.haruatari.akane.client.kernel.bencode

import com.haruatari.akane.client.kernel.bencode.dto.announce.Announce
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import java.io.InputStream

interface DecoderInterface {
    fun decodeMetaInfo(stream: InputStream): MetaInfo;

    fun decodeAnnounce(stream: InputStream): Announce;
}