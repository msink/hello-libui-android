package sample

import kotlin.test.Test
import kotlin.test.assertTrue

class AppDesktopTest {
    @Test
    fun testHello() {
        assertTrue("Desktop" in hello())
    }
}