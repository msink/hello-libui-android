package sample

import kotlin.test.Test
import kotlin.test.assertTrue

class HelloDesktopTest {
    @Test
    fun testHello() {
        assertTrue("Desktop" in hello())
    }
}