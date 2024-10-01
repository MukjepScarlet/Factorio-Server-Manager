package moe.scarlet.servermanager.repository

import moe.scarlet.servermanager.entity.ServerConfig
import org.springframework.data.mongodb.repository.MongoRepository

interface ServerConfigRepository : MongoRepository<ServerConfig, String>
