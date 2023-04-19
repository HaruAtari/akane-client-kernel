package com.haruatari.akane.client.kernel.bencode.dto

internal class DictionaryNode(private val content: Map<String, Node>) : ContainerNode() {
    override fun getValue(): Map<String, Node> = content
}