package com.rsupport.rv.agent.myapplication

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel




    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
    }

    @Test
    fun testA() = runBlocking {

    }

}