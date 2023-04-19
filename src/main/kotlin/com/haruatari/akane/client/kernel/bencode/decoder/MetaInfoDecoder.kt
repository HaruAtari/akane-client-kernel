package com.haruatari.akane.client.kernel.bencode.decoder

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo.File
import com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo.Info
import com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo.MetaInfo
import com.haruatari.akane.client.kernel.bencode.decoder.dto.metaInfo.Piece
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import com.haruatari.akane.client.kernel.bencode.tokenizer.TokenizerFacade
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.*
import java.util.*

internal class MetaInfoDecoder(private val reader: Reader) {
    internal fun decode(): MetaInfo {
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

    private fun decodeInfo(root: Map<String, Token>): Info {
        val node = root["info"]
        if (node == null || node !is DictionaryToken) {
            throw DecoderException("The root dictionary should contains the 'info' dictionary element.")
        }

        return Info(
            pieceLength = decodePieceLength(node.getValue()),
            pieces = decodePieces(node.getValue()),
            files = decodeFiles(node.getValue())
        )
    }

    private fun decodePieceLength(infoRoot: Map<String, Token>): Int {
        val node = infoRoot["piece length"]
        if (node == null || node !is IntegerToken) {
            throw DecoderException("The info dictionary should contains the 'piece length' integer element.")
        }
        return node.getValue()
    }

    private fun decodePieces(infoRoot: Map<String, Token>): Array<Piece> {
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
            Piece(pieceContent)
        }
    }

    private fun decodeFiles(infoRoot: Map<String, Token>): Array<File> {
        val name = infoRoot["name"]
        if (name == null || name !is StringToken) {
            throw DecoderException("The info dictionary should contains the 'name' string element.")
        }

        return if (infoRoot.containsKey("files")) decodeMultipleFiles(infoRoot) else arrayOf(decodeSingleFile(infoRoot))
    }

    private fun decodeSingleFile(infoRoot: Map<String, Token>): File {
        val length = infoRoot["length"]
        if (length == null || length !is IntegerToken) {
            throw DecoderException("For the single file torrent the info dictionary should contains the 'length' integer element.")
        }

        val name = infoRoot["name"] as StringToken

        return File(name.getValue(), length.getValue())
    }

    private fun decodeMultipleFiles(infoRoot: Map<String, Token>): Array<File> {
        val node = infoRoot["files"]
        if (node == null || node !is ListToken) {
            throw DecoderException("For the multi files torrent the info dictionary should contains the 'files' list element.")
        }

        val name = infoRoot["name"] as StringToken

        val result = mutableListOf<File>();
        for (item in node.getValue()) {
            if (item !is DictionaryToken) {
                throw DecoderException("The info.files list should contains only dictionary element.")
            }

            val length = item.getValue()["length"]
            if (length == null || length !is IntegerToken) {
                throw DecoderException("The info.files should contains the 'length' integer element.")
            }

            val path = item.getValue()["path"]
            if (path == null || path !is ListToken) {
                throw DecoderException("The info.files should contains the 'path' list element.")
            }

            val pathParts = mutableListOf<String>(name.getValue())
            for (pathItem in path.getValue()) {
                if (pathItem !is StringToken) {
                    throw DecoderException("The info.files.path should contains only string element.")
                }

                pathParts.add(pathItem.getValue())
            }

            result.add(
                File(
                    pathParts.joinToString(System.getProperty("file.separator")),
                    length.getValue()
                )
            )
        }

        return result.toTypedArray()
    }
}