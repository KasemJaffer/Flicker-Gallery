package com.tigerspike.flickergallery.repository

import com.tigerspike.flickergallery.api.FlickerService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
class FlickerRepositoryTest {
    private lateinit var service: FlickerService
    private lateinit var repository: FlickerRepository

    @Before
    fun setup() {
        service = mock(FlickerService::class.java)
        repository = FlickerRepository(service)
    }

    @Test
    fun getFeeds() {
        repository.feeds
        verify(service).feeds("json")
    }
}