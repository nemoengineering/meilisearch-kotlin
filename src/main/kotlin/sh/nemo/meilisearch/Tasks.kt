package sh.nemo.meilisearch

import sh.nemo.meilisearch.responses.TaskResponse
import io.ktor.client.request.get
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

suspend fun Meilisearch.listTasks() = this.client.get<List<TaskResponse>>("/tasks")

suspend fun Meilisearch.getTask(taskUid: String) = this.client.get<TaskResponse>("/tasks/$taskUid")

suspend fun Meilisearch.listTasksByIndex(indexUid: String) =
    this.client.get<List<TaskResponse>>("/indexes/$indexUid/tasks")

suspend fun Meilisearch.getTaskByIndex(indexUid: String, taskUid: String) =
    this.client.get<TaskResponse>("/indexes/$indexUid/tasks/$taskUid")

suspend fun Meilisearch.waitForTask(taskUid: String) = waitForTask(taskUid, 5000, 50)

suspend fun Meilisearch.waitForTask(taskUid: String, timeoutInMs: Int, intervalInMs: Int) {
    var task: TaskResponse
    var status = ""
    val startTime: Instant = Clock.System.now()
    var elapsedTime: Long = 0

    while (status != "succeeded" && status != "failed") {
        if (elapsedTime >= timeoutInMs) {
            throw Exception()
        }
        task = getTask(taskUid)
        status = task.status
        Thread.sleep(intervalInMs.toLong())
        elapsedTime = (Clock.System.now() - startTime).inWholeMilliseconds
    }
}
