package moe.scarlet.servermanager.entity

data class Response<out T>(
    val code: Int,
    val message: String,
    val content: T,
) {
    companion object {
        fun <T> ok(content: T) = Response(0, "OK", content)

        val DUPLICATE_PATH = Response(1, "Duplicate Path", null)
        val INVALID_PATH = Response(2, "Invalid Path", null)
        val NOT_FOUND = Response(3, "Not Found", null)
    }
}