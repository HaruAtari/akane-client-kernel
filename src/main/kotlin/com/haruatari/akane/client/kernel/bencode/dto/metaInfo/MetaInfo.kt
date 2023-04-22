package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

data class MetaInfo(
    override val announce: String,
    override val info: InfoInterface
) : MetaInfoInterface