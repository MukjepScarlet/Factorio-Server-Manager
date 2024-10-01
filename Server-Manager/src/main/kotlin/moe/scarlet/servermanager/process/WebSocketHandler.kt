package moe.scarlet.servermanager.process

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.scarlet.servermanager.event.ProcessOutputEvent
import moe.scarlet.servermanager.extension.log
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketHandler(
    private val processManager: ProcessManager,
) : TextWebSocketHandler() {

    private val WebSocketSession.path: String
        get() = uri?.path?.split("/")?.last()?.let {
            val b64decoded = Base64.getDecoder().decode(it).toString(Charsets.UTF_8)
            URLDecoder.decode(b64decoded, Charsets.UTF_8)
        } ?: error("Bad WebSocket session")

    private val sessions = ConcurrentHashMap<String, MutableSet<WebSocketSession>>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) = runBlocking(Dispatchers.IO) {
        val path = session.path
        log.info("Get WS message: ${message.payload} for path $path")
        processManager.message(path, message.payload)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val path = session.path
        if (sessions.contains(path)) {
            sessions[path]!! += session
        } else {
            sessions[path] = mutableSetOf(session)
        }
        log.info("New WS session ${session.id} has been established for path $path")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val path = session.path
        sessions[path]?.remove(session)
        log.info("WS session ${session.id} has been closed for path $path")
    }

    private fun broadcast(path: String, text: String) = runBlocking(Dispatchers.IO) {
        sessions[path]?.forEach {
            launch {
                it.sendMessage(TextMessage(text))
            }
        }
    }

    @EventListener
    fun handleProcessOutputEvent(event: ProcessOutputEvent) {
        broadcast(event.path, event.message)
    }
}
