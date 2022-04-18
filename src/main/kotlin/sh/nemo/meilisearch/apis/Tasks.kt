package sh.nemo.meilisearch.apis

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.responses.TaskResponse

suspend fun Meilisearch.listTasks(): List<TaskResponse> = this.client.get("/tasks").body()

suspend fun Meilisearch.getTask(taskUid: String): TaskResponse = this.client.get("/tasks/$taskUid").body()

suspend fun Meilisearch.listTasksByIndex(indexUid: String): List<TaskResponse> =
    this.client.get("/indexes/$indexUid/tasks").body()

suspend fun Meilisearch.getTaskByIndex(indexUid: String, taskUid: String): TaskResponse =
    this.client.get("/indexes/$indexUid/tasks/$taskUid").body()

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
