package com.dicoding.doanda.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.doanda.storyapp.data.paging.StoryAdapter
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem
import com.dicoding.doanda.storyapp.utils.DataDummy
import com.dicoding.doanda.storyapp.utils.MainDispatcherRules
import com.dicoding.doanda.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRules()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when Get Stories Successful Should Not Null and Return Data`() = runTest {
        val dummyStories = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> = StoryPagingResource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()

        expectedStories.value = data
        `when`(storyRepository.getAllStories()).thenReturn(expectedStories)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStories: PagingData<ListStoryItem> = storyViewModel.getAllStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        // Memastikan data tidak null
        assertNotNull(differ.snapshot())
        // Memastikan jumlah data sesuai dengan yang diharapkan.
        assertEquals(dummyStories.size, differ.snapshot().size)
        // Memastikan data pertama yang dikembalikan sesuai.
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()

        expectedStories.value = data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        `when`(storyRepository.getAllStories()).thenReturn(expectedStories)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStories: PagingData<ListStoryItem> = storyViewModel.getAllStories().getOrAwaitValue()

        differ.submitData(actualStories)
        // 				□ Memastikan jumlah data yang dikembalikan nol.
        assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingResource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}