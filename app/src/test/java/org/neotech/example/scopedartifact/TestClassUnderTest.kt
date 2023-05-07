package org.neotech.example.scopedartifact

import org.junit.Assert.assertEquals
import org.junit.Test

class TestClassUnderTest {

    @Test
    fun test_add() {
        assertEquals(2, ClassUnderTest().add(1, 1))
    }
}