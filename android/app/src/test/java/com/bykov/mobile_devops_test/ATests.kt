package com.bykov.mobile_devops_test

import org.junit.Test

import org.junit.Assert.*
import kotlin.AssertionError
import kotlin.random.Random

class ATests {

    @Test
    fun test_example1_alwaysSuccess() {
        assertTrue(true)
    }

    @Test
    fun test_example2_alwaysFailure() {
        assertTrue(false)
    }

    @Test(timeout = 500)
    fun test_example3_testTakesTooLong() {
        Thread.sleep(1_000)
        assertTrue(true)
    }

    @Test
    fun test_example4_unexpectedException() {
        check(false)
    }

    @Test(expected = AssertionError::class)
    fun test_example5_expectedException() {
        throw AssertionError("Intentional failure")
    }

    @Test
    fun test_example6_flakyTest() {
        val value = Random.nextInt(10)
        assertTrue(value % 2 == 0)
    }
}