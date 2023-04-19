package com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo

data class Info(
    val pieceLength: Int,
    val pieces: Array<Piece>,
    val files: Array<File>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Info) return false

        if (pieceLength != other.pieceLength) return false
        if (!pieces.contentEquals(other.pieces)) return false
        if (!files.contentEquals(other.files)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pieceLength
        result = 31 * result + pieces.contentHashCode()
        result = 31 * result + files.contentHashCode()
        return result
    }
}