package com.pokedexplatzi.utils.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pokedexplatzi.data.model.enums.ErrorCode
import com.pokedexplatzi.data.model.enums.ErrorCode.*
import com.pokedexplatzi.utils.AppSession
import com.pokedexplatzi.utils.PokedexException
import com.pokedexplatzi.utils.UseCaseResult
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import org.koin.core.component.getScopeName
import retrofit2.HttpException
import timber.log.Timber

/**
 * Executes a given block of code reporting all the process status via the [UseCaseResult] class within
 * a single-shot LiveData object.
 *
 * @param work Block of code containing the needed work to report.
 *
 * @return LiveData object reporting the work results.
 * @see UseCaseResult
 * @see useCaseExecution
 */
fun <T : Any> resultLiveData(work: suspend () -> T?) = liveData {
    emit(UseCaseResult.Loading)
    try {
        emit(UseCaseResult.Success(work()))
    } catch (e: PokedexException) {
        Timber.e(e)
        emit(UseCaseResult.Error(e))
    } catch (e: HttpException) {
        when {
            e.code() == 404 -> emit(UseCaseResult.Error(PokedexException(HTTP_NOT_FOUND)))
            e.code() >= 500 -> emit(UseCaseResult.Error(PokedexException(HTTP_SERVER_ERROR)))
        }
    } catch (t: Throwable) {
        Timber.e(t)
        emit(UseCaseResult.Error(PokedexException(UNKNOWN_ERROR)))
    }
}

/**
 * Executes a given block of code reporting all the process (except [UseCaseResult.Loading]) status via the [UseCaseResult] class within
 * a single-shot LiveData object.
 *
 * @param work Block of code containing the needed work to report.
 *
 * @return LiveData object reporting the work results.
 * @see UseCaseResult
 * @see useCaseExecution
 */
fun <T : Any> resultLiveDataWithoutLoading(work: suspend () -> T?) = liveData {
    try {
        emit(UseCaseResult.Success(work()))
    } catch (e: PokedexException) {
        Timber.e(e)
        emit(UseCaseResult.Error(e))
    } catch (e: HttpException) {
        when {
            e.code() == 404 -> emit(UseCaseResult.Error(PokedexException(HTTP_NOT_FOUND)))
            e.code() >= 500 -> emit(UseCaseResult.Error(PokedexException(HTTP_SERVER_ERROR)))
        }
    } catch (t: Throwable) {
        Timber.e(t)
        emit(UseCaseResult.Error(PokedexException(UNKNOWN_ERROR)))
    }
}

/**
 * Runs a block of code in a [CoroutineScope] bound to the [Default] coroutine dispatcher, as use
 * case classes should perform calculations.
 *
 * Any IO operations should be done in the [Dispatchers.IO]
 * dispatcher, so be aware of switching dispatchers correctly for I/O operations.
 *
 * Any exceptions thrown by the work parameter must be handled, Exceptions are not handled in this
 * function, they are reported to [Timber.e]
 *
 * @param work Block of code to be executed.
 * @return [UseCaseResult] object reporting the block execution result.
 *
 * @see resultLiveData
 */
suspend fun <T : Any> useCaseExecution(
    dispatcher: CoroutineDispatcher = Default,
    work: suspend CoroutineScope.() -> T?
): T? {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }
    val coroutineName = CoroutineName("UseCase-Coroutine:${work.getScopeName()}")
    return withContext(Job() + dispatcher + exceptionHandler + coroutineName) { work() }
}

/**
 * Handles the observation of a [LiveData] that reports a [UseCaseResult] object.
 *
 * @param onError -> Executes block when the [UseCaseResult] object reports an error.
 * @param onSuccess -> Executes block when the [UseCaseResult] object reports a success.
 */
fun <T : Any> LiveData<UseCaseResult<T>>.observeWith(
    lifecycleOwner: LifecycleOwner,
    onSuccess: (payload: T?) -> Unit = {},
    onError: (error: PokedexException) -> Unit = {}
): Unit = observe(lifecycleOwner) { result ->
    when (result) {
        UseCaseResult.Loading -> AppSession.showGlobalProgressBar(true)
        is UseCaseResult.Error -> {
            AppSession.showGlobalProgressBar(false)
            onError(result.exception)
        }
        is UseCaseResult.Success -> {
            onSuccess(result.payload)
            AppSession.showGlobalProgressBar(false)
        }
    }
}