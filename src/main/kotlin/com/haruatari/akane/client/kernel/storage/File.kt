package com.haruatari.akane.client.kernel.storage

import com.haruatari.akane.client.kernel.storage.exception.StorageException
import java.io.Closeable
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.nio.file.Path

class File(private val path: Path, val length: Long) : FileInterface, Closeable {
    private var fileAccess: RandomAccessFile? = null

    constructor(path: File, length: Long) : this(path.path, length)
    constructor(path: String, length: Long) : this(Path.of(path), length)

    override fun read(offset: Long, length: Int): ByteArray {
        if (length > this.length) {
            throw StorageException("Trying to read from out of file. Offset: ${offset}, Length: ${length}, Actual size: ${this.length}")
        }

        val buffer = ByteArray(length)
        val access = getAccess()
        access.seek(offset)
        access.read(buffer)

        return buffer
    }

    override fun write(offset: Long, data: ByteArray) {
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
                fileAccess = RandomAccessFile(path.toFile(), "rwd")
            } catch (e: FileNotFoundException) {
                throw StorageException("Can't create the '${path}' file.")
            }
        }

        return fileAccess!!
    }

    override fun close() {
        fileAccess?.close()
    }
}
