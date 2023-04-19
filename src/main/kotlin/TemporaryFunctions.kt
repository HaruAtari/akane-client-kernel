import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import java.io.InputStreamReader
import java.net.URL

 fun sendTrackerRequest(metaInfo: MetaInfo): String {
    val address = "http://bttracker.debian.org:6969/announce?" +
            "info_hash=" + metaInfo.info.hash.getUrlEncoded() + "&peer_id=12345678901234567890&ip=188.255.23.34&port=6584&uploaded=0&downloaded=0" +
            "&left=" + metaInfo.info.length + "&action=started"

    val url = URL(address)
    val connection = url.openConnection()
    return InputStreamReader(connection.getInputStream()).use {
        it.readText();
    }
}