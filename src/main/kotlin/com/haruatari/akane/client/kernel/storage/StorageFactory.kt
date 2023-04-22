package com.haruatari.akane.client.kernel.storage

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

class StorageFactory {
    fun buildFromMetaInfo(root: Path, metaInfo: MetaInfo): StorageInterface {
        val files = mutableListOf<FileInterface>()

        if (metaInfo.info.length != null) {
            files.add(File(Paths.get(root.pathString, metaInfo.info.name), metaInfo.info.length))
        } else {
            for (fileData in metaInfo.info.files) {
                files.add(
                    File(
                        fileData.path.joinToString(java.io.File.separator),
                        fileData.length
                    )
                )
            }
        }

        return Storage(files)
    }

    fun buildFromMetaInfo(root: String, metaInfo: MetaInfo): StorageInterface {
        return buildFromMetaInfo(Path.of(root), metaInfo)
    }
}