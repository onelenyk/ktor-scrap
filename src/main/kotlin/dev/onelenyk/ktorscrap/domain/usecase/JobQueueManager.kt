package dev.onelenyk.ktorscrap.domain.usecase

import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobQueueManager(
    private val repository: ScrapingJobRepository,
    private val jobProcessor: JobProcessor,
    private val coroutineScope: CoroutineScope,
) {
    init {
        //     startMonitoring()
        coroutineScope.launch {
            processExistingJobs()
        }
    }

    private fun startMonitoring() {
        coroutineScope.launch {
            while (true) {
                try {
                    // Get all pending jobs
                    val pendingJobs = repository.getAll().filter { it.status == JobStatus.PENDING }

                    // Send each pending job to the processor
                    pendingJobs.forEach { job: ScrapingJob ->
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
            val pendingJobs = repository.getAll().filter { it.status == JobStatus.PENDING }

            // Send each pending job to the processor
            pendingJobs.forEach { job: ScrapingJob ->
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
}
