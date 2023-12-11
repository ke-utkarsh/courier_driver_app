package ymsli.com.couriemate.xmpp.model

import org.jivesoftware.smack.packet.IQ
import org.jxmpp.jid.Jid

class MucSubIQ private constructor() : IQ(
    CHILD_ELEMENT_NAME,
    CHILD_ELEMENT_NAMESPACE
) {

    private lateinit var nick: String

    companion object {
        private const val CHILD_ELEMENT_NAME = "subscribe";
        private const val CHILD_ELEMENT_NAMESPACE = "urn:xmpp:mucsub:0"

        fun getMucSubIQ(to: Jid, from: Jid, nick: String): MucSubIQ {
            return MucSubIQ().apply {
                this.to = to
                this.from = from
                this.type = Type.set
                this.nick = nick
            }
        }
    }

    override fun getIQChildElementBuilder(xml: IQChildElementXmlStringBuilder): IQChildElementXmlStringBuilder {
        xml.attribute("nick", nick)
        xml.rightAngleBracket()
        xml.halfOpenElement( "event ")
        xml.attribute("node", "urn:xmpp:mucsub:nodes:messages")
        xml.rightAngleBracket()
        xml.closeElement("event")
        return xml
    }
}
