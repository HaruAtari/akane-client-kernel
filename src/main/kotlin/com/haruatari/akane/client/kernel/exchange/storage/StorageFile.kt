package com.haruatari.akane.client.kernel.exchange.storage

import com.haruatari.akane.client.kernel.exchange.exception.StorageException
import java.io.FileNotFoundException
import java.io.RandomAccessFile

internal data class StorageFile(private val path: String, val length: Long) {
    private var fileAccess: RandomAccessFile? = null

    fun read(offset: Long, length: Int): ByteArray {
        if (length > this.length) {
            throw StorageException("Trying to read from out of file. Offset: ${offset}, Length: ${length}, Actual size: ${this.length}")
        }

        val buffer = ByteArray(length)
        val access = getAccess()
        access.seek(offset)
        access.read(buffer)

        return buffer
    }

    fun write(offset: Long, data: ByteArray) {
        if (length > this.length) {
            throw StorageException("Trying to write to out of file. Offset: ${offset}, Length: ${length}, Actual size: ${this.length}")
        }

        val access = getAccess()
        access.seek(offset)
        access.write(data)
    }

    private fun getAccess(): RandomAccessFile {
        if (fileAccess == null) {
            try {
                fileAccess = RandomAccessFile(path, "rwd")
            } catch (e: FileNotFoundException) {
                throw StorageException("Can't create the '${path}' file.")
            }
        }

        return fileAccess!!
    }
}
