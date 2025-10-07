package com.ifucolo.rickandmorty.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ifucolo.rickandmorty.domain.Episode
import kotlinx.coroutines.CompletableDeferred

/**
 * A PagingSource designed for testing. It allows a test to manually
 * control when data is loaded, making it possible to verify loading states.
 */
class ControllablePagingSource : PagingSource<Int, Episode>() {

    // A "deferred" is a coroutine primitive that can be completed later.
    // Our load() function will wait until this is completed by the test.
    private var deferred = CompletableDeferred<LoadResult<Int, Episode>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        // This will suspend the load() function until the test calls completeLoad()
        return deferred.await()
    }

    /**
     * Called by the test to complete the loading process with specific data.
     */
    fun completeLoad(data: List<Episode>, prevKey: Int?, nextKey: Int?) {
        deferred.complete(
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        )
    }

    /**
     * Called by the test to complete the loading process with an error.
     */
    fun completeLoadWithError(error: Throwable) {
        deferred.complete(LoadResult.Error(error))
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        // Standard implementation for getRefreshKey
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
