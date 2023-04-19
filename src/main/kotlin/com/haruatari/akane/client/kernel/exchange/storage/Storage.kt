package com.haruatari.akane.client.kernel.exchange.storage

import com.haruatari.akane.client.kernel.exchange.exception.StorageException
import java.nio.ByteBuffer
import java.nio.file.Path
import javax.imageio.IIOException
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

class Storage internal constructor(private val root: Path, files: List<File>) {
    internal data class File(val relativePath: String, val length: Long);

    private val storageFiles: List<StorageFile>
    private val totalLength: Long

    init {
        if (!root.exists()) {
            try {
                root.createDirectory()
            } catch (e: IIOException) {
                throw StorageException("Can't create the root directory '${root.pathString}'.")
            }
        } else if (!root.isDirectory()) {
            throw StorageException("The root path '${root.pathString}' is not a directory.")
        }

        storageFiles = files.map {
            StorageFile(
                "${root.pathString}${java.io.File.separator}${it.relativePath}",
                it.length
            )
        }

        totalLength = storageFiles.sumOf { it.length }
    }

    internal fun read(offset: Int, length: Int): ByteArray {
        val res = ByteBuffer.allocate(length)
        accessData(offset, length) { file, fileOffset, fileLength ->
            res.put(file.read(fileOffset, fileLength))
        }

        return res.array()
    }

    internal fun write(offset: Int, data: ByteArray) {
        var dataOffset = 0

        accessData(offset, data.size) { file, fileOffset, fileLength ->
            file.write(
                fileOffset,
                data.slice(dataOffset until dataOffset + fileLength).toByteArray()
            )
            dataOffset += fileLength
        }
    }

    private fun accessData(offset: Int, length: Int, callback: (file: StorageFile, offset: Long, length: Int) -> Unit) {
        val totalBeginning = offset
        val totalEnd = offset + length - 1
        if (totalEnd > totalLength) {
            throw StorageException("Trying to access storage out of the bounds. Offset: ${offset}, Length: ${length}, Actual size: $totalLength")
        }

        var fileBeginning = 0L;
        var fileEnd = 0L;
        for ((i, storageFile) in storageFiles.withIndex()) {
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