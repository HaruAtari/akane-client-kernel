package com.haruatari.akane.client.kernel.bencode.decoder

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.boxer.DictionaryBoxer
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.*
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import com.haruatari.akane.client.kernel.bencode.tokenizer.TokenizerFacade
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.*
import java.io.InputStream
import java.security.MessageDigest

internal class MetaInfoDecoder(stream: InputStream) {
    private val reader: Reader

    init {
        reader = Reader(stream)
    }

    fun decode(): MetaInfoInterface {
        val root = TokenizerFacade(reader).tokenize()
        if (root !is DictionaryToken) {
            throw DecoderException("The root node should be a dictionary.")
        }

        return MetaInfo(
            announce = decodeAnnounce(root.getValue()),
            info = decodeInfo(root.getValue())
        )
    }

    private fun decodeAnnounce(root: Map<String, Token>): String {
        val node = root["announce"]
        if (node == null || node !is StringToken) {
            throw DecoderException("The root dictionary should contains the 'announce' string element.")
        }

        return node.getValue()
    }

    private fun decodeInfo(root: Map<String, Token>): InfoInterface {
        val node = root["info"]
        if (node == null || node !is DictionaryToken) {
            throw DecoderException("The root dictionary should contains the 'info' dictionary element.")
        }

        val nodeValue = node.getValue()

        if (!nodeValue.containsKey("files") && !nodeValue.containsKey("length")) {
            throw DecoderException("The info dictionary should contains one of the next elements: 'length' or 'files`.")
        }
        if (nodeValue.containsKey("files") && nodeValue.containsKey("length")) {
            throw DecoderException("The info dictionary should contains only one of the next elements: 'length' or 'files`.")
        }

        return Info(
            hash = Hash(
                MessageDigest
                    .getInstance("SHA-1")
                    .digest(DictionaryBoxer().box(node))
            ),
            name = decodeName(nodeValue),
            pieceLength = decodePieceLength(nodeValue),
            pieces = decodePieces(nodeValue),
            length = decodeLength(nodeValue),
            files = decodeFiles(nodeValue)
        )
    }

    private fun decodeName(infoRoot: Map<String, Token>): String {
        val name = infoRoot["name"]
        if (name == null || name !is StringToken) {
            throw DecoderException("The info dictionary should contains the 'name' string element.")
        }

        return name.getValue()
    }

    private fun decodePieceLength(infoRoot: Map<String, Token>): Int {
        val node = infoRoot["piece length"]
        if (node == null || node !is NumberToken) {
            throw DecoderException("The info dictionary should contains the 'piece length' integer element.")
        }
        return node.getValue().toInt()
    }

    private fun decodePieces(infoRoot: Map<String, Token>): Array<HashInterface> {
        val node = infoRoot["pieces"]
        if (node == null || node !is StringToken) {
            throw DecoderException("The info dictionary should contains the 'pieces' string element.")
        }

        val bytes = node.getRawContent()
        val pieceSize = 20

        if (bytes.size % pieceSize != 0) {
            throw DecoderException("The info.pieces element should have the length multiple of $pieceSize.")
        }

        return Array(bytes.size / pieceSize) { i ->
            val pieceContent = bytes.copyOfRange(i * pieceSize, (i + 1) * pieceSize)
            Hash(pieceContent)
        }
    }

    private fun decodeLength(infoRoot: Map<String, Token>): Long? {
        val length = infoRoot["length"] ?: return null

        if (length !is NumberToken) {
            throw DecoderException("The info.length element should be an integer.")
        }

        return length.getValue()
    }

    private fun decodeFiles(infoRoot: Map<String, Token>): Array<FileInterface> {
        val node = infoRoot["files"] ?: return emptyArray()

        if (node !is ListToken) {
            throw DecoderException("The info.files element should be a list.")
        }

        val result = mutableListOf<FileInterface>()
        for (item in node.getValue()) {
            if (item !is DictionaryToken) {
                throw DecoderException("The info.files list should contains only dictionary elements.")
            }

            val length = item.getValue()["length"]
            if (length == null || length !is NumberToken) {
                throw DecoderException("The info.files should contains the 'length' integer element.")
            }

            val path = item.getValue()["path"]
            if (path == null || path !is ListToken) {
                throw DecoderException("The info.files should contains the 'path' list element.")
            }

            val pathParts = mutableListOf<String>()
            for (pathItem in path.getValue()) {
                if (pathItem !is StringToken) {
                    throw DecoderException("The info.files.path should contains only string elements.")
                }

                pathParts.add(pathItem.getValue())
            }

            result.add(
                File(
                    pathParts.toTypedArray(),
                    length.getValue()
                )
            )
        }

        return result.toTypedArray()
    }
}