package com.haruatari.akane.client.kernel.storage

import com.haruatari.akane.client.kernel.storage.exception.StorageException
import java.nio.ByteBuffer

class Storage(private val files: List<FileInterface>) : StorageInterface {
    private val totalLength: Long = files.sumOf { it.length }

    override fun read(offset: Long, length: Int): ByteArray {
        if (length == 0) {
            return byteArrayOf()
        }

        val res = ByteBuffer.allocate(length)
        accessData(offset, length) { file, fileOffset, fileLength ->
            res.put(file.read(fileOffset, fileLength))
        }

        return res.array()
    }

    override fun write(offset: Long, data: ByteArray) {
        if (data.isEmpty()) {
            return
        }

        var dataOffset = 0

        accessData(offset, data.size) { file, fileOffset, fileLength ->
            file.write(
                fileOffset,
                data.slice(dataOffset until dataOffset + fileLength).toByteArray()
            )
            dataOffset += fileLength
        }
    }

    override fun toString(): String {
        return "Storage(files: ${files})";
    }

    override fun equals(other: Any?): Boolean {
        return other is Storage
            && other.files == this.files
    }

    override fun hashCode(): Int {
        return files.hashCode()
    }

    private fun accessData(offset: Long, length: Int, callback: (file: FileInterface, offset: Long, length: Int) -> Unit) {
        val totalBeginning = offset
        val totalEnd = offset + length - 1
        if (totalEnd > totalLength) {
            throw StorageException("Trying to access storage out of the bounds. Offset: ${offset}, Length: ${length}, Actual size: $totalLength")
        }

        var fileBeginning = 0L;
        var fileEnd = 0L;
        for ((i, storageFile) in files.withIndex()) {
            if (i != 0) {
                fileBeginning = fileEnd + 1;
            }
            fileEnd = fileBeginning + storageFile.length - 1

            if (fileEnd < totalBeginning || fileBeginning > totalEnd) {
                continue;
            }

            val accessBeginning = if (totalBeginning < fileBeginning) {
                0
            } else {
                totalBeginning - fileBeginning
            }

            val accessLength = if (totalEnd > fileEnd) {
                storageFile.length - accessBeginning;
            } else {
                totalEnd - fileBeginning - accessBeginning + 1
            }

            callback(storageFile, accessBeginning, accessLength.toInt())
        }
    }
}