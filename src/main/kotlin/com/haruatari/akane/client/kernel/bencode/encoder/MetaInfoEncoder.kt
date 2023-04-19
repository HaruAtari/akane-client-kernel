package com.haruatari.akane.client.kernel.bencode.encoder

import com.haruatari.akane.client.kernel.bencode.boxer.DictionaryBoxer
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.File
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.Info
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.*
import java.io.ByteArrayInputStream
import java.io.InputStream

internal class MetaInfoEncoder {
    fun encode(metaInfo: MetaInfo): InputStream {
        val rootToken = DictionaryToken(
            mapOf(
                "announce" to StringToken(metaInfo.announce),
                "info" to encodeInfo(metaInfo.info),
            )
        )

        return ByteArrayInputStream(DictionaryBoxer().box(rootToken))
    }

    private fun encodeInfo(info: Info): DictionaryToken {
        val map = mutableMapOf<String, Token>(
            "name" to StringToken(info.name),
            "piece length" to NumberToken(info.pieceLength.toLong()),
            "pieces" to StringToken(buildList {
                for (piece in info.pieces) {
                    addAll(piece.getBytes().toList())
                }
            }.toByteArray())
        )

        if (info.length != null) {
            map["length"] = NumberToken(info.length)
        }

        if (info.files.isNotEmpty()) {
            map["files"] = ListToken(buildList<DictionaryToken> {
                for (file in info.files) {
                    add(encodeFile(file))
                }
            })
        }

        return DictionaryToken(map)
    }

    private fun encodeFile(file: File): DictionaryToken {
        return DictionaryToken(
            mapOf(
                "length" to NumberToken(file.length),
                "path" to ListToken(buildList {
                    for (pathItem in file.path) {
                        add(StringToken(pathItem))
                    }
                })
            )
        )
    }
}