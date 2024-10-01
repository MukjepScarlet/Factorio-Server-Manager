package moe.scarlet.servermanager.entity

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.File
import java.io.FileNotFoundException


data class BanUser(val username: String, val reason: String?)

data class ServerSettings(
    val name: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),

    val maxPlayers: Int = 0,

    val visibility: Visibility = Visibility(public = true, lan = true),

    val username: String = "",
    val password: String = "",
    val token: String = "",

    val gamePassword: String = "",

    val requireUserVerification: Boolean = true,

    val maxUploadInKilobytesPerSecond: Int = 0,
    val maxUploadSlots: Int = 5,
    val minimumLatencyInTicks: Int = 0,
    val maxHeartbeatsPerSecond: Int = 60,
    val ignorePlayerLimitForReturningPlayers: Boolean = false,

    @JsonDeserialize(using = CommandPermission.Deserializer::class)
    @JsonSerialize(using = CommandPermission.Serializer::class)
    val allowCommands: CommandPermission = CommandPermission.ADMINS_ONLY,

    val autosaveInterval: Int = 10,
    val autosaveSlots: Int = 5,

    val afkAutokickInterval: Int = 0,

    val autoPause: Boolean = true,

    val onlyAdminsCanPauseTheGame: Boolean = true,

    val autosaveOnlyOnServer: Boolean = true,

    val nonBlockingSaving: Boolean = false,

    val minimumSegmentSize: Int = 25,
    val minimumSegmentSizePeerCount: Int = 20,
    val maximumSegmentSize: Int = 100,
    val maximumSegmentSizePeerCount: Int = 10,
) {
    data class Visibility(
        val public: Boolean,
        val lan: Boolean,
    )

    enum class CommandPermission {
        ALLOW,
        DISALLOW,
        ADMINS_ONLY;

        class Deserializer : JsonDeserializer<CommandPermission>() {
            override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): CommandPermission {
                val node: JsonNode = parser.codec.readTree(parser)
                return when {
                    node.isBoolean -> if (node.asBoolean()) ALLOW else DISALLOW
                    else -> ADMINS_ONLY
                }
            }
        }

        class Serializer : JsonSerializer<CommandPermission>() {
            override fun serialize(value: CommandPermission, gen: JsonGenerator, serializers: SerializerProvider) =
                when (value) {
                    ALLOW -> gen.writeBoolean(true)
                    DISALLOW -> gen.writeBoolean(false)
                    ADMINS_ONLY -> gen.writeString("admins-only")
                }
        }
    }
}

private val objectMapper = jacksonObjectMapper()
private val objectMapperSnakeCase = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    enable(SerializationFeature.INDENT_OUTPUT)
}

@Document("config")
data class ServerConfig(
    val name: String = "",
    @Id val path: String = "",
    val port: Int = 34197,
    val processParams: List<String> = emptyList(),
    val adminList: List<String> = try {
        objectMapperSnakeCase.readTree(File(path, "server-adminlist.json")).takeIf {
            it.isArray
        }?.map {
            it.asText()
        } ?: emptyList()
    } catch (_: FileNotFoundException) {
        emptyList()
    },
    val banList: List<BanUser> = try {
        objectMapperSnakeCase.readTree(File(path, "server-banlist.json")).takeIf {
            it.isArray
        }?.mapNotNull {
            when {
                it.isTextual -> BanUser(it.asText(), null)
                it.isObject -> objectMapperSnakeCase.treeToValue(it)
                else -> null
            }
        } ?: emptyList()
    } catch (_: FileNotFoundException) {
        emptyList()
    },
    val settings: ServerSettings = try {
        objectMapperSnakeCase.readValue(File(path, "server-settings.json"))
    } catch (e: FileNotFoundException) {
        ServerSettings()
    },
)

val ServerConfig.root: File
    get() = File(path)

val ServerConfig.lock: File
    get() = File(path, ".lock")

val ServerConfig.executable: File
    get() = File(path, "bin/x64/factorio.exe")

val ServerConfig.isAvailable: Boolean
    get() = root.isDirectory && executable.isFile

val ServerConfig.isRunning: Boolean
    get() = lock.isFile

// JSON

val ServerConfig.settingsJson: File
    get() = File(path, "server-settings.json")

val ServerConfig.adminListJson: File
    get() = File(path, "admin-list.json")

val ServerConfig.banListJson: File
    get() = File(path, "server-banlist.json")

// Write JSON config
fun ServerConfig.saveJson() = runBlocking(Dispatchers.IO) {
    objectMapperSnakeCase.writeValue(settingsJson, settings)
    objectMapperSnakeCase.writeValue(adminListJson, adminList)
    objectMapperSnakeCase.writeValue(banListJson, banList)
}
