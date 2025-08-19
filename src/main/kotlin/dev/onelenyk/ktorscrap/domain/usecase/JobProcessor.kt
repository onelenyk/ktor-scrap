package dev.onelenyk.ktorscrap.domain.usecase

import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.scraper.ScraperManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class JobProcessor(
    private val jobOutputManager: JobOutputManager,
    private val scraperManager: ScraperManager,
) {
    private val jobQueue = ConcurrentLinkedQueue<ScrapingJob>()
    private val isProcessing = AtomicBoolean(false)
    private val mutex = Mutex()
    private val workerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        startWorker()
    }

    private fun startWorker() {
        workerScope.launch {
            while (true) {
                try {
                    processNextJob()
                } catch (e: Exception) {
                    println("Error in job processor worker: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun addJob(job: ScrapingJob) {
        mutex.withLock {
            jobQueue.add(job)
            if (!isProcessing.get()) {
                processNextJob()
            }
        }
    }

    private suspend fun processNextJob() {
        if (!isProcessing.compareAndSet(false, true)) {
            return
        }

        try {
            while (true) {
                val job =
                    mutex.withLock {
                        jobQueue.poll()
                    } ?: break

                try {
                    processJob(job)
                } catch (e: Exception) {
                    println("Error processing job ${job.id}: ${e.message}")
                    e.printStackTrace()
                    jobOutputManager.failJob(job.id, e.message ?: "Unknown error occurred")
                }
            }
        } finally {
            isProcessing.set(false)
        }
    }

    private suspend fun processJob(job: ScrapingJob) {
        // Update job status to processing
        jobOutputManager.startProcessing(job.id)

        // Process the job using ScraperManager
        val result =
            withContext(Dispatchers.IO) {
                scraperManager.scrape(job.source)
            }

        // Update job with results
        jobOutputManager.completeJob(job.id, result)
    }

    fun getQueueSize(): Int = jobQueue.size

    fun isCurrentlyProcessing(): Boolean = isProcessing.get()
}
