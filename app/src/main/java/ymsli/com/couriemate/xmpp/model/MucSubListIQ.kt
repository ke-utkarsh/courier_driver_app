package ymsli.com.couriemate.xmpp.model

import org.jivesoftware.smack.packet.IQ
import org.jxmpp.jid.Jid


class MucSubListIQ private constructor(): IQ(
    CHILD_ELEMENT_NAME,
    CHILD_ELEMENT_NAMESPACE
) {

    companion object {
        private const val CHILD_ELEMENT_NAME = "subscriptions";
        private const val CHILD_ELEMENT_NAMESPACE = "urn:xmpp:mucsub:0"

        fun getMucSubListIQ(to: Jid, from: Jid): MucSubListIQ {
            return MucSubListIQ().apply {
                this.to = to
                this.from = from
                this.type = Type.get
            }
        }
    }

    override fun getIQChildElementBuilder(xml: IQChildElementXmlStringBuilder): IQChildElementXmlStringBuilder {
        xml.rightAngleBracket()
        return xml
    }
}