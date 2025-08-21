package dev.onelenyk.ktorscrap.presentation

import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager

fun main(args: Array<String>) {
    EnvironmentManager.load(args)

    val server = Server()
    server.start()
}
