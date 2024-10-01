package moe.scarlet.servermanager.controller

import moe.scarlet.servermanager.entity.Response
import moe.scarlet.servermanager.entity.ServerConfig
import moe.scarlet.servermanager.entity.isAvailable
import moe.scarlet.servermanager.entity.saveJson
import moe.scarlet.servermanager.extension.log
import moe.scarlet.servermanager.repository.ServerConfigRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController(
    private val serverConfigRepository: ServerConfigRepository,
) {

    data class CreationParams(
        val name: String = "",
        val path: String = "",
    )

    private fun CreationParams.toEntity() = ServerConfig(name, path)

    @PutMapping
    fun addConfig(@RequestBody params: CreationParams): Response<ServerConfig?> {
        if (serverConfigRepository.existsById(params.path))
            return Response.DUPLICATE_PATH

        val entity = params.toEntity()

        if (!entity.isAvailable)
            return Response.INVALID_PATH

        log.info("Add config: ${params.name} -> ${params.path}")
        return Response.ok(serverConfigRepository.save(params.toEntity()))
    }

    @DeleteMapping
    fun removeConfig(@RequestBody params: CreationParams): Response<Unit?> {
        return if (serverConfigRepository.existsById(params.path))
            Response.NOT_FOUND
        else {
            log.info("Remove config: ${params.name} -> ${params.path}")
            Response.ok(serverConfigRepository.deleteById(params.path))
        }
    }

    @GetMapping("/{path}")
    fun getConfig(@PathVariable("path") path: String): Response<ServerConfig?> {
        return Response.ok(serverConfigRepository.findByIdOrNull(path))
    }

    @GetMapping
    fun getAllConfig(): Response<List<ServerConfig>> {
        return Response.ok(serverConfigRepository.findAll())
    }

    @PostMapping
    fun updateConfig(@RequestBody serverConfig: ServerConfig): Response<ServerConfig> {
        log.info("Update config: ${serverConfig.name} -> ${serverConfig.path}")
        serverConfig.saveJson()
        return Response.ok(serverConfigRepository.save(serverConfig))
    }

    data class CopyParams(
        val from: String,
        val to: String,
    )

    @PutMapping("/copy")
    fun copyConfig(@RequestBody params: CopyParams): Response<ServerConfig?> {
        val origin = serverConfigRepository.findByIdOrNull(params.from)
        return if (origin == null)
            Response.NOT_FOUND
        else
            Response.ok(serverConfigRepository.save(origin.copy(path = params.to)))
    }

}