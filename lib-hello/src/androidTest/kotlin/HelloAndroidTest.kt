package sample

import kotlin.test.Test
import kotlin.test.assertTrue

class HelloAndroidTest {
    @Test
    fun testHello() {
        assertTrue("Android" in hello())
    }
}