package moe.scarlet.servermanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.socket.config.annotation.EnableWebSocket

@EnableWebSocket
@ConfigurationPropertiesScan
@SpringBootApplication
class ServerManagerApplication

fun main(args: Array<String>) {
    runApplication<ServerManagerApplication>(*args)
}
