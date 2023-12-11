package ymsli.com.couriemate.xmpp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.*
import org.jivesoftware.smack.filter.StanzaTypeFilter
import org.jivesoftware.smack.packet.*
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smack.util.PacketParserUtils
import org.jivesoftware.smack.xml.XmlPullParser
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.pubsub.EventElement
import org.jivesoftware.smackx.pubsub.ItemsExtension
import org.jivesoftware.smackx.pubsub.PayloadItem
import org.jivesoftware.smackx.pubsub.SimplePayload
import org.jxmpp.jid.impl.JidCreate
import ymsli.com.couriemate.database.entity.ChatNotification
import ymsli.com.couriemate.database.entity.XMPPConfig
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.xmpp.model.MucSubIQ
import ymsli.com.couriemate.xmpp.model.MucSubListIQ

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @date   AUG 11, 2021
 * Copyright (c) 2021, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * XMPPManager : Provides all the XMPP MUC related functionality
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class XMPPManager(private val repo: CouriemateRepository,
                  private val connectionStateListener: ConnectionStateListener): ConnectionListener{

    private var config: XMPPConfig = repo.getXMPPConfig()
    private var connection: XMPPTCPConnection? = null
    private lateinit var muc: MultiUserChat
    private lateinit var mucManager: MultiUserChatManager
    private val roomEntityBareJid = JidCreate.entityBareFrom(config.roomName)
    private val userJid = "${config.userName}@${config.serverDomain}"

    private companion object {
        private const val TAG = "XMPPManager"
        private const val ROOM_NAME = "ychat@conference.Constants.SERVER_DOMAIN"
        private const val MOBILE_RESOURCE = "MOBILE"
        const val DELIMITER = "-"
        private const val XMPP_EXT_EVENT = "http://jabber.org/protocol/pubsub#event"

        fun getGroupMessageStanzaId(fromJid: String, toJid: String): String{
            return "$fromJid$DELIMITER$toJid$DELIMITER${System.currentTimeMillis()}"
        }
    }

    /**************** Implementation of ConnectionListener interface  *******************/
    override fun connected(connection: XMPPConnection?) {}
    override fun connectionClosed() {}
    override fun connectionClosedOnError(e: Exception?) {}
    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        connectionStateListener.onAuthResponse(true)
    }


    /**
     * This is the stanza listener responsible for all the XMPP events,
     * we receive events here and process accordingly.
     * @author Balraj VE00YM023
     */
    private val stanzaListener = StanzaListener { stanza ->
        if (stanza !is Message) return@StanzaListener
        (stanza.getExtension("event", XMPP_EXT_EVENT) as EventElement?)?.let {
            val itemsExtension: ItemsExtension = it.event as ItemsExtension
            itemsExtension.items?.forEach { namedElement ->
                val payloadItem = namedElement as PayloadItem<*>
                val simplePayload: SimplePayload = payloadItem.payload as SimplePayload
                val parserString = simplePayload.toXML(null as String?)?.toString()
                val xmlPullParser: XmlPullParser = PacketParserUtils.getParserFor(parserString)
                val message: Message = PacketParserUtils.parseMessage(xmlPullParser)
                val id = message.stanzaId
                val timestamp = message.getTimeStamp()
                val from = message.from.toString()
                var contactJid = from
                if (from.contains("/")) {
                    contactJid = "${from.split("/").toTypedArray()[1]}@${config.serverDomain}"
                }
                repo.saveChatMessage(message.stanzaId, contactJid, userJid, message.body, timestamp)

                /** Update notification table when message received from someone else */
                if (contactJid != userJid) {
                    val chatNotification = ChatNotification(message = message.body)
                    repo.insertChatNotification(chatNotification)
                }
            }
        }
    }

    /********* Initialize XMPP TCP connection and configure connection properties ***************/
    init {
        try{
            SmackConfiguration.DEBUG = true
            val conf = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(config.serverDomain)
                .setHost(config.serverDomain)
                .setResource(MOBILE_RESOURCE)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setConnectTimeout(10000)
                .setCompressionEnabled(true).build()
            connection = XMPPTCPConnection(conf)
            connection!!.addConnectionListener(this@XMPPManager)
            connection!!.addSyncStanzaListener(stanzaListener, StanzaTypeFilter.MESSAGE)
            mucManager = MultiUserChatManager.getInstanceFor(connection)
            val reconnectionManager = ReconnectionManager.getInstanceFor(connection)
            reconnectionManager.enableAutomaticReconnection()
            ReconnectionManager.setEnabledPerDefault(true)
        }
        catch (e: Exception){
            connectionStateListener.onAuthResponse(false)
        }
    }

    /**
     * Given a stable TCP connection is in place with XMPP server,
     * this function tries to login with given creds.
     * @author Balraj VE00YM023
     */
    fun login() {
        try {
            connection!!.connect()
            connection!!.login(config.userName, config.password)
        } catch (e: InterruptedException) {
            connectionStateListener.onAuthResponse(false)
            e.printStackTrace()
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun setupMUC(){
        try{
            muc = mucManager.getMultiUserChat(roomEntityBareJid)
            subscribeToMuc()
            if(!config.historyRestored){ setupMamManager() }
            val presence = PresenceBuilder.buildPresence("${config.userName}@${System.currentTimeMillis()}")
                .ofType(Presence.Type.available)
                .setStatus("Available")
                .build()
            connection?.sendStanza(presence)
        }
        catch(e: java.lang.Exception){
            Log.e(TAG, e.message?.toString()?:"ERROR PRINTING STACK TRACE")
        }
    }

    /**
     * Subscribe to MUC room using custom stanza,
     * this makes use of specific extension for Ejabberd and may not work with other servers.
     * @author Balraj VE00YM023
     */
    private fun subscribeToMuc(){
        val fullJid = JidCreate.entityFullFrom("$userJid/$MOBILE_RESOURCE")
        val mucIq = MucSubIQ.getMucSubIQ(roomEntityBareJid, fullJid, config.userName)
        val result = connection!!.sendIqRequestAndWaitForResponse<UnparsedIQ>(mucIq)
        Log.d(TAG, result.toString())
        val macListIQ = MucSubListIQ.getMucSubListIQ(roomEntityBareJid, fullJid)
        val list = connection!!.sendIqRequestAndWaitForResponse<UnparsedIQ>(macListIQ)
        Log.d(TAG, list.toString())
    }

    /**
     * Setup mam manager for archive management.
     * @author Balraj VE00YM023
     */
    private fun setupMamManager(){
        val mamMan = MamManager.getInstanceFor(muc)
        Log.d(TAG, mamMan.isSupported.toString())
        val mamQueryArgs = MamManager.MamQueryArgs.builder()
            .setResultPageSizeTo(config.historySize)
            .queryLastPage()
            .build()
        val mamQuery = mamMan.queryArchive(mamQueryArgs)
        mamQuery?.messages?.forEach { processMessage(it) }
        config.historyRestored = true
        repo.setXMPPConfigHistoryRestored()
        Log.d(TAG, mamQuery.toString())
    }

    fun sendMessage(body: String) {
        if(!connection!!.isConnected){
            connection!!.connect()
            connection!!.login(config.userName, config.password)
        }
        try {
            val stanzaId = getGroupMessageStanzaId(config.userName, ROOM_NAME)
            val messageBuilder = StanzaBuilder.buildMessage(stanzaId)
            val fullJid = JidCreate.entityFullFrom("$userJid/$MOBILE_RESOURCE")

            messageBuilder.ofType(Message.Type.groupchat)
                .setBody("$body")
                .to(roomEntityBareJid)
                .from(fullJid)
            connection!!.sendStanza(messageBuilder.build())
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun processMessage(message: Message){
        val from: String = message.from.toString()
        val timestamp = message.getTimeStamp()
        var contactJid = from
        if (from.contains("/")) {
            contactJid = "${from.split("/").toTypedArray()[1]}@${config.serverDomain}"
        }
        repo.saveChatMessage(message.stanzaId, contactJid, userJid, message.body, timestamp)
    }

    fun changeStatus(newStatus: Presence.Type) = GlobalScope.launch(Dispatchers.IO) {
        val presence = PresenceBuilder.buildPresence("${config.userName}@${System.currentTimeMillis()}")
            .ofType(newStatus)
            .setStatus(newStatus.toString())
            .build()
        connection?.sendStanza(presence)
    }
}

/**
 * Extracts the timestamp value from the message archive ID.
 * @author Balraj VE00YM023
 */
private fun Message.getTimeStamp(): Long {
    val timestamp = (extensions.first { it.elementName == "archived" } as StandardExtensionElement)
        .attributes["id"]?.let{ it.substring(0, it.length -3) }
    if(timestamp != null && timestamp.length >= 12 ){
        return timestamp.toLongOrNull() ?: System.currentTimeMillis()
    }
    return System.currentTimeMillis()
}
