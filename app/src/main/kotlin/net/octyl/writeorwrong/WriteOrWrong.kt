package net.octyl.writeorwrong

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage

fun main(args: Array<String>) {
    Application.launch(WriteOrWrong::class.java, *args)
}

class WriteOrWrong : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Write or Wrong!"
        primaryStage.scene = Scene(GridPane())
        primaryStage.show()
    }
}
