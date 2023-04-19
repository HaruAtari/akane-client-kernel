package com.haruatari.akane.client.kernel.exchange

import com.haruatari.akane.client.kernel.exchange.exception.StorageException
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.nio.file.Path
import javax.imageio.IIOException
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

class Storage internal constructor(private val root: Path, relativeFilePaths: List<InputFile>) {
    internal data class InputFile(val relativePath: String, val length: Int);

    private val accesses: List<FileAccess>

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

        accesses = relativeFilePaths.map {
            FileAccess(
                "${root.pathString}${File.separator}${it.relativePath}",
                it.length
            )
        }
    }

    internal fun read(offset: Int, length: Int): ByteArray {
        TODO()
    }

    internal fun write(offset: Int, data: ByteArray) {
        TODO()
    }
}

private class FileAccess(private val path: String, val length: Int) {
    private var fileAccess: RandomAccessFile? = null

    val access: RandomAccessFile
        get() {
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