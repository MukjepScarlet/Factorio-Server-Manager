package moe.scarlet.servermanager.controller

import moe.scarlet.servermanager.entity.Response
import moe.scarlet.servermanager.repository.ServerConfigRepository
import moe.scarlet.servermanager.process.ProcessManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/process")
class ProcessController(
    private val serverConfigRepository: ServerConfigRepository,
    private val processManager: ProcessManager,
) {
    data class ProcessKey(val path: String)

    @GetMapping
    suspend fun status(@RequestParam path: String): Response<Boolean?> {
        val server = serverConfigRepository.findByIdOrNull(path) ?: return Response.NOT_FOUND
        return Response.ok(processManager[server]?.isAlive)
    }

    @PostMapping("/start")
    suspend fun start(@RequestBody data: ProcessKey): Response<Unit?> {
        val (path) = data
        val server = serverConfigRepository.findByIdOrNull(path) ?: return Response.NOT_FOUND
        return Response.ok(processManager + server)
    }

    @PostMapping("/stop")
    suspend fun stop(@RequestBody data: ProcessKey): Response<Unit?> {
        val (path) = data
        val server = serverConfigRepository.findByIdOrNull(path) ?: return Response.NOT_FOUND
        return Response.ok(processManager + server)
    }

}
