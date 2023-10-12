package com.unitech.schoolsystem

import com.unitech.schoolsystem.database.SchoolDb
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class NewPasswordController {

    @FXML
    private lateinit var changePasswordButton: Button

    @FXML
    private lateinit var newPassword: TextField

    @FXML
    private lateinit var confirmPassword: TextField

    @FXML
    private lateinit var newPasswordForm: AnchorPane;

    @FXML
    private lateinit var newPasswordUsername: TextField

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var result: ResultSet

    // Alert Box
    private lateinit var alertBox: Alert

    @FXML
    fun onClickChangePasswordButton () {
        val password = newPassword.text
        val confirmPass = confirmPassword.text
        val username = newPasswordUsername.text
        val connectDb = SchoolDb ()
        connection = connectDb.connectionDb()!!

        // Check username
        val checkUsernameQuery = "SELECT * FROM admin WHERE username = ?"
        preparedStatement = connection.prepareStatement(checkUsernameQuery)
        preparedStatement.setString(1, username)

        // Execute Query
        result = preparedStatement.executeQuery()
        // Update password Query
        val updatePasswordQuery =
            "UPDATE admin SET password = '$password' WHERE username = '$username'"
        connection = connectDb.connectionDb()!!

        val statement = connection.createStatement()

        try {
            // Blank Fields
            if (username.isEmpty() ||
                password.isEmpty() ||
                confirmPass.isEmpty()) {
                //Alert Box
                alertBox = Alert(Alert.AlertType.ERROR)
                alertBox.title = "Empty Field"
                alertBox.headerText = null
                alertBox.contentText = "Please Fill all the Fields"
                alertBox.showAndWait()
            } else {
                if ((confirmPass != password)) {
                    alertBox = Alert(Alert.AlertType.ERROR)
                    alertBox.title = "Unmatched password input"
                    alertBox.headerText = null
                    alertBox.contentText = "Please enter Matched password"
                    alertBox.showAndWait()
                } else if ((password.length < 6 && confirmPass.length < 6)) {
                    alertBox = Alert(Alert.AlertType.ERROR)
                    alertBox.title = "Weak Password"
                    alertBox.headerText = null
                    alertBox.contentText = "Password must be at least 6 characters"
                    alertBox.showAndWait()
                } else {
                    if (result.next()) {
                        // Update password
                        statement.executeUpdate(updatePasswordQuery)
                        alertBox = Alert(Alert.AlertType.INFORMATION)
                        alertBox.title = "Password Changed"
                        alertBox.headerText = null
                        alertBox.contentText = "You have successfully Changed Your Password"
                        alertBox.showAndWait()

                        // Back to Login Window
                        changePasswordButton.scene.window.hide()
                        val loginWindow = Stage()
                        val root = FXMLLoader(SchoolManagement::class.java.getResource("home.fxml"))
                        val scene = Scene(root.load())
                        loginWindow.scene = scene
                        loginWindow.initStyle(StageStyle.TRANSPARENT)
                        loginWindow.show()
                    } else {
                        alertBox = Alert(Alert.AlertType.ERROR)
                        alertBox.title = "Unknown user"
                        alertBox.headerText = null
                        alertBox.contentText = "Sorry!, Username is not Available"
                        alertBox.showAndWait()

                        // Clear input Fields
                        newPasswordUsername.clear()
                        newPassword.clear()
                        confirmPassword.clear()
                    }

                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}