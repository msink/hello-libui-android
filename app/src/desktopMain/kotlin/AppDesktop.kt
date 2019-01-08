package sample

import libui.ktx.*

fun main() = appWindow(
    title = "desktop-app",
    width = 320,
    height = 240
) {
    vbox {
        label(hello()) 
    }
}
