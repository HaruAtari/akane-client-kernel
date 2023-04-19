package com.haruatari.akane.client.kernel.bencode.dto

internal class ListNode(private val content: List<Node>) : ContainerNode() {
    override fun getValue(): List<Node> = content
}