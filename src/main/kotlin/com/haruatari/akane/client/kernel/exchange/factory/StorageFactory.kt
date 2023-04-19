package com.haruatari.akane.client.kernel.exchange.factory

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import com.haruatari.akane.client.kernel.exchange.storage.Storage
import java.io.File
import java.nio.file.Path

class StorageFactory {
    fun build(root: Path, metaInfo: MetaInfo): Storage {
        val files = mutableListOf<Storage.File>()

        if (metaInfo.info.length != null) {
            files.add(Storage.File(metaInfo.info.name, metaInfo.info.length))
        } else {
            for (fileData in metaInfo.info.files) {
                files.add(
                    Storage.File(
                        fileData.path.joinToString(File.separator),
                        fileData.length
                    )
                )
            }
        }

        return Storage(root, files)
    }
}