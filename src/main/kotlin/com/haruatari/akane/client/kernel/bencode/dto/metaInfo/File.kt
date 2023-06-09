package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

data class File(
    override val path: Array<String>,
    override val length: Long
) : FileInterface {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as File

        if (!path.contentEquals(other.path)) return false
        if (length != other.length) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.contentHashCode()
        result = (31 * result + length).toInt()
        return result
    }
}