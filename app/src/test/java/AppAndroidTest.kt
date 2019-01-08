package sample

import kotlin.test.Test
import kotlin.test.assertTrue

class AppAndroidTest {
    @Test
    fun testHello() {
        assertTrue("Android" in hello())
    }
}