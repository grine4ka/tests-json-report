package com.hellofresh.mobile_devops_test

import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class BTests {

    @Test
    fun test_example1_alwaysSuccess() {
        Assert.assertTrue(true)
    }

    @Test
    fun test_example2_alwaysFailure() {
        Assert.assertTrue(false)
    }

    @Test(timeout = 500)
    fun test_example3_testTakesTooLong() {
        Thread.sleep(1_000)
        Assert.assertTrue(true)
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
        Assert.assertTrue(value % 2 == 0)
    }
}