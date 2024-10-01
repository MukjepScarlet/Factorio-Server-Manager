package moe.scarlet.servermanager.event

import org.springframework.context.ApplicationEvent

class ProcessOutputEvent(
    source: Any,
    val path: String,
    val message: String
) : ApplicationEvent(source)
