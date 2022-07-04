package com.somethingsimple.garments.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.somethingsimple.garments.models.Garment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
internal class GarmentsViewModelTest {

    private var viewModel:GarmentsViewModel? = null
    @Mock
    lateinit var responseDataObserver : Observer<ArrayList<Garment>>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = GarmentsViewModel()
        MockitoAnnotations.initMocks(this)
        viewModel?.responseLiveData?.observeForever(responseDataObserver)
        val tempJson = "{\"garments\": [\n" +
                "    {\n" +
                "      \"name\": \"Dress\",\n" +
                "      \"timestamp\": \"10000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Pant\",\n" +
                "      \"timestamp\": \"20000\"\n" +
                "    }]}"
        viewModel?.fetchData(tempJson)
    }

    @Test
    fun fetchData() {
        val listClass: Class<ArrayList<Garment>> =
            ArrayList::class.java as Class<ArrayList<Garment>>
        val captor: ArgumentCaptor<ArrayList<Garment>> = ArgumentCaptor.forClass(listClass)

        captor.run {
            verify(responseDataObserver, times(1)).onChanged(capture())
        }

    }

    @Test
    fun addGarment() {
        viewModel?.addGarment("test_garment")

        val listClass: Class<ArrayList<Garment>> =
            ArrayList::class.java as Class<ArrayList<Garment>>
        val captor: ArgumentCaptor<ArrayList<Garment>> = ArgumentCaptor.forClass(listClass)

        captor.run {
            verify(responseDataObserver, times(2)).onChanged(capture())
            assert(value.size > 1)
        }
    }

    @Test
    fun createGarmentsForPersisting() {
        val tempJson = viewModel?.createGarmentsForPersisting()
        assert(!tempJson.isNullOrBlank())
    }

    @Test
    fun sortbyName() {
        viewModel?.sortbyName()
        val listClass: Class<ArrayList<Garment>> =
            ArrayList::class.java as Class<ArrayList<Garment>>
        val captor: ArgumentCaptor<ArrayList<Garment>> = ArgumentCaptor.forClass(listClass)

        captor.run {
            verify(responseDataObserver, times(2)).onChanged(capture())
            assert(value.size > 1)
        }
    }

    @Test
    fun sortByTime() {
        viewModel?.sortByTime()
        val listClass: Class<ArrayList<Garment>> =
            ArrayList::class.java as Class<ArrayList<Garment>>
        val captor: ArgumentCaptor<ArrayList<Garment>> = ArgumentCaptor.forClass(listClass)

        captor.run {
            verify(responseDataObserver, times(2)).onChanged(capture())
            assert(value.size > 1)
        }
    }
}