package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

interface HashInterface {
    fun getBytes(): ByteArray
    fun getHex(): String
    fun getUrlEncoded(): String
}