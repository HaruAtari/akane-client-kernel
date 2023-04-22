package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

interface InfoInterface {
    val hash: HashInterface
    val name: String
    val pieceLength: Int
    val pieces: Array<HashInterface>
    val length: Long?
    val files: Array<FileInterface>

    override fun equals(other: Any?): Boolean
}