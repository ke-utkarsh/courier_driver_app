package ymsli.com.couriemate.xmpp

interface ConnectionStateListener{
    fun onAuthResponse(status: Boolean)
}