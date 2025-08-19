package dev.onelenyk.ktorscrap.domain.model

interface Logger {
    fun info(msg: String)

    fun warn(msg: String)

    fun error(
        msg: String,
        throwable: Throwable? = null,
    )

    fun stage(
        stage: String,
        detail: String? = null,
    )

    fun time(
        stage: String,
        block: () -> Unit,
    )
}

class ConsoleLogger(private val prefix: String = "", private val enabled: Boolean = true) : Logger {
    override fun info(msg: String) {
        if (enabled) println("\u001B[36m$prefix[INFO]\u001B[0m $msg")
    }

    override fun warn(msg: String) {
        if (enabled) println("\u001B[33m$prefix[WARN]\u001B[0m $msg")
    }

    override fun error(
        msg: String,
        throwable: Throwable?,
    ) {
        if (enabled) {
            println("\u001B[31m$prefix[ERROR]\u001B[0m $msg")
            throwable?.printStackTrace()
        }
    }

    override fun stage(
        stage: String,
        detail: String?,
    ) {
        if (!enabled) return
        if (detail != null) {
            println("\u001B[35m$prefix[STAGE]\u001B[0m $stage: $detail")
        } else {
            println("\u001B[35m$prefix[STAGE]\u001B[0m $stage")
        }
    }

    override fun time(
        stage: String,
        block: () -> Unit,
    ) {
        if (!enabled) {
            block()
            return
        }
        val start = System.currentTimeMillis()
        stage(stage, "started")
        block()
        val end = System.currentTimeMillis()
        stage(stage, "finished in ${end - start} ms")
    }
}
