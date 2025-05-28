package dev.onelenyk.ktorscrap.features.vacanciesandroid.processor

import com.mongodb.client.model.changestream.OperationType
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.onelenyk.ktorscrap.data.db.MongoDBManager
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model.JobStatus
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model.ScrapingJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobQueueManager(
    private val repository: ScrapingJobRepository,
    private val jobProcessor: JobProcessor,
    private val coroutineScope: CoroutineScope,
    private val mongoDBManager: MongoDBManager,
) {
    private val collection: MongoCollection<ScrapingJob> by lazy {
        mongoDBManager.getCollection("scraping_jobs", ScrapingJob::class.java)
    }

    init {
        //  startMonitoring()
        startWatcher()
        coroutineScope.launch {
            processExistingJobs()
        }
    }

    private fun startMonitoring() {
        coroutineScope.launch {
            while (true) {
                try {
                    // Get all pending jobs
                    val pendingJobs = repository.readAll().filter { it.status == JobStatus.PENDING }

                    // Send each pending job to the processor
                    pendingJobs.forEach { job ->
                        jobProcessor.addJob(job)
                    }

                    // Wait before next check
                    withContext(Dispatchers.IO) {
                        kotlinx.coroutines.delay(5000) // Check every 5 seconds
                    }
                } catch (e: Exception) {
                    println("Error in job queue monitoring: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun processExistingJobs() {
        try {
            // Get all pending jobs
            val pendingJobs = repository.readAll().filter { it.status == JobStatus.PENDING }

            // Send each pending job to the processor
            pendingJobs.forEach { job ->
                jobProcessor.addJob(job)
            }
        } catch (e: Exception) {
            println("Error processing existing jobs: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun addNewJob(source: ScrapeTarget): ScrapingJob {
        val job = repository.createJob(source)
        jobProcessor.addJob(job)
        return job
    }

    private fun startWatcher() {
        coroutineScope.launch {
            try {
                collection.watch().collect { change ->
                    when (change.operationType) {
                        OperationType.INSERT -> {
                            val newJob = change.fullDocument
                            if (newJob != null) {
                                jobProcessor.addJob(newJob)
                            }
                        }

                        else -> {} // Ignore other operations
                    }
                }
            } catch (e: Exception) {
                println("Error in MongoDB watcher: ${e.message}")
                e.printStackTrace()
                // Attempt to restart watcher after a delay
                delay(5000)
                startWatcher()
            }
        }
    }
}
