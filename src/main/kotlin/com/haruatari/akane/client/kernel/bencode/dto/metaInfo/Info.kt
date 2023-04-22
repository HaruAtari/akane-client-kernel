package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

data class Info(
    override val hash: HashInterface,
    override val name: String,
    override val pieceLength: Int,
    override val pieces: Array<HashInterface>,
    override val length: Long?,
    override val files: Array<FileInterface>
) : InfoInterface {
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
        result = (31 * result + (length ?: 0)).toInt()
        result = 31 * result + files.contentHashCode()
        return result
    }
}