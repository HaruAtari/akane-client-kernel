package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader

internal abstract class ContainerNodeDecoder<T>(reader: Reader) : NodeDecoder(reader)