package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

data class Info(
    val hash: Hash,
    val name: String,
    val pieceLength: Int,
    val pieces: Array<Hash>,
    val length: Int?,
    val files: Array<File>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Info

        if (hash != other.hash) return false
        if (name != other.name) return false
        if (pieceLength != other.pieceLength) return false
        if (!pieces.contentEquals(other.pieces)) return false
        if (length != other.length) return false
        if (!files.contentEquals(other.files)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + pieceLength
        result = 31 * result + pieces.contentHashCode()
        result = 31 * result + (length ?: 0)
        result = 31 * result + files.contentHashCode()
        return result
    }
}