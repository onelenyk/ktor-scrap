package dev.onelenyk.ktorscrap.presentation.monitoring

import dev.onelenyk.ktorscrap.presentation.di.SystemHealthChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SystemMonitor(
    private val healthChecker: SystemHealthChecker,
    private val scope: CoroutineScope,
) {
    fun start() {
        scope.launch {
            // Run an initial check after a short delay
            delay(5_000) // 5 seconds
            healthChecker.checkAndLog()

            // Continue checking every minute
            while (true) {
                delay(60_000) // 60 seconds
                healthChecker.checkAndLog()
            }
        }
    }
}
