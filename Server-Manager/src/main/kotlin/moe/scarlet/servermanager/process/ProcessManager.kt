package moe.scarlet.servermanager.process

import kotlinx.coroutines.*
import moe.scarlet.servermanager.entity.ServerConfig
import moe.scarlet.servermanager.entity.executable
import moe.scarlet.servermanager.entity.root
import moe.scarlet.servermanager.event.ProcessOutputEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class ProcessManager(
    private val eventPublisher: ApplicationEventPublisher,
) {

    private val processes = ConcurrentHashMap<ServerConfig, Pair<Process, Job>>()

    private suspend fun ServerConfig.start(): Process = withContext(Dispatchers.IO) {
        val commands = listOf(executable.absolutePath, "--port", port.toString()) + processParams
        ProcessBuilder(commands).directory(root).start()
    }

    private suspend fun Process.stop() = withContext(Dispatchers.IO) {
        outputWriter(Charsets.UTF_8).write("/quit")
        // TODO: Check if successfully terminated
    }

    suspend fun message(path: String, text: String) = withContext(Dispatchers.IO) {
        processes.filterKeys { it.path == path }.values.forEach { (process, _) ->
            process.outputWriter(Charsets.UTF_8).write(text)
        }
    }

    operator fun get(serverConfig: ServerConfig): Process? = processes[serverConfig]?.first

    suspend operator fun plus(serverConfig: ServerConfig) {
        val process = serverConfig.start()
        val job = CoroutineScope(Dispatchers.IO).launch {
            process.inputReader(Charsets.UTF_8).useLines {
                it.forEach { line ->
                    eventPublisher.publishEvent(ProcessOutputEvent(this, serverConfig.path, line))
                }
            }
        }

        processes[serverConfig] = process to job
    }

    suspend operator fun minus(serverConfig: ServerConfig) {
        val (process, job) = processes[serverConfig] ?: return
        process.stop()
        job.cancel()
        processes.remove(serverConfig)
    }

}
