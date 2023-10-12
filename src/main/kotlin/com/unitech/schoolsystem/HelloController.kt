package com.unitech.schoolsystem

import com.unitech.schoolsystem.database.SchoolDb
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet


class HelloController {
    @FXML
    private lateinit var forgetPasswordButton: Button

    @FXML
    private lateinit var loginButton: Button

    @FXML
    private lateinit var menuBar: AnchorPane

    @FXML
    private lateinit var password: PasswordField

    @FXML
    private lateinit var username: TextField

    // Alert Box
    private lateinit var alert: Alert

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet


    @FXML
    fun onClickLoginButton () {
        val connectDb = SchoolDb()
        val query = "SELECT * FROM admin WHERE username = ? and password = ?"
        connection = connectDb.connectionDb()!!


        try {
            // Database Connectivity
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, username.text)
            preparedStatement.setString(2, password.text)
            // Query Execution
            resultSet = preparedStatement.executeQuery()

            if (username.text.isEmpty() || password.text.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Blank fields"
                alert.headerText = null
                alert.contentText = "Please fill all Fields"
                alert.showAndWait()
            } else {
                // For Correct Input
                if (resultSet.next()) {
                    loginButton.scene.window.hide()
                    val root = FXMLLoader(DashboardController::class.java.getResource("dashboard.fxml"))
                    val stage = Stage()
                    val scene = Scene(root.load())
                    stage.scene = scene
                    stage.title = "Home"
                    stage.isMaximized = true
                    stage.show()
                } else {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Wrong inputs"
                    alert.headerText = null
                    alert.contentText = "Wrong username or Password"
                    alert.showAndWait()
                    username.clear()
                    password.clear()
                }
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @FXML
    fun onClickForgotPasswordButton () {
        /* Alert Box
        alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Confirm Password Change"
        alert.headerText = null
        alert.contentText = "Are you Sure ?"
        alert.showAndWait()
         */
        // Hide login Form
        forgetPasswordButton.scene.window.hide()
        val forgotPassword = FXMLLoader(ForgotPasswordController::class.java.getResource("forgot-password.fxml"))
        val forgetPasswordStage = Stage ()
        val scene = Scene(forgotPassword.load())
        forgetPasswordStage.scene = scene
        forgetPasswordStage.title = "Reset Password"
        forgetPasswordStage.show()



    }
}