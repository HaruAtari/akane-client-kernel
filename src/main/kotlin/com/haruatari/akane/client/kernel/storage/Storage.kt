package com.haruatari.akane.client.kernel.storage

import com.haruatari.akane.client.kernel.storage.exception.StorageException
import java.nio.ByteBuffer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

class Storage(private val root: Path, private val files: List<File>) : StorageInterface {
    private val totalLength: Long = files.sumOf { it.length }

    constructor(root: String, files: List<File>) : this(Path.of(root), files)

    init {
        if (root.exists() && !root.isDirectory()) {
            throw StorageException("The root path '${root.pathString}' is not a directory.")
        }
    }

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

    private fun accessData(offset: Long, length: Int, callback: (file: File, offset: Long, length: Int) -> Unit) {
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