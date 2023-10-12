package com.unitech.schoolsystem

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle

class SchoolManagement : Application() {
    private var x = 0.0
    private var y = 0.0
    override fun start(primaryStage: Stage) {
        val fxmlLoader = FXMLLoader(
            SchoolManagement::class
                .java.getResource("home.fxml")
        )
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            this::class.java
                .getResource("design.css")?.toExternalForm() ?: String()
        )
        scene.setOnMousePressed { event ->
            x = event.sceneX
            y = event.sceneY
        }
        scene.setOnMouseDragged { event ->
            primaryStage.x = event.screenX - x
            primaryStage.y = event.screenY - y
            primaryStage.opacity = 0.9
        }
        scene.setOnMouseReleased {
            primaryStage.opacity = 1.0
        }
        primaryStage.initStyle(StageStyle.TRANSPARENT)
        primaryStage.title = "Hello!"
        primaryStage.scene = scene
        primaryStage.show()
    }

}

fun main() {
    Application.launch(SchoolManagement::class.java)
}