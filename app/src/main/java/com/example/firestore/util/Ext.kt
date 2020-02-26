package com.example.firestore.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Converts this task to an instance of [Deferred].
 * If task is cancelled then resulting deferred will be cancelled as well.
 */
public fun <T> Task<T>.asDeferred(): Deferred<T> { // chuyen tu callback sang deferred
    if (isComplete) {
        val e = exception
        return if (e == null) {
            @Suppress("UNCHECKED_CAST")
            CompletableDeferred<T>().apply { if (isCanceled) cancel() else complete(result as T) }
        } else {
            CompletableDeferred<T>().apply { completeExceptionally(e) }
        }
    }
    val result = CompletableDeferred<T>()
    addOnCompleteListener {
        val e = it.exception
        if (e == null) {
            @Suppress("UNCHECKED_CAST")
            if (isCanceled) result.cancel() else result.complete(it.result as T)
        } else {
            result.completeExceptionally(e)
        }
    }
    return result
}
/**
 * Awaits for completion of the task without blocking a thread.
 *
 * This suspending function is cancellable.
 * If the [Job] of the current coroutine is cancelled or completed while this suspending function is waiting, this function
 * stops waiting for the completion stage and immediately resumes with [CancellationException].
 */
public suspend fun <T> Task<T>.await(): T {
    // fast path
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException("Task $this was cancelled normally.")
            } else {
                @Suppress("UNCHECKED_CAST")
                result as T // return lai ket qua khi k co exc, va k cancle
            }
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                @Suppress("UNCHECKED_CAST")
                if (isCanceled) cont.cancel() else cont.resume(result as T)
            } else {
                cont.resumeWithException(e)
            }
        }
    }

}