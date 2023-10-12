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
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class ForgotPasswordController {

    @FXML
    private lateinit var resetCode: TextField

    @FXML
    private lateinit var  resetCodeForm: AnchorPane


    @FXML
    private lateinit var submitButton: Button;

    private lateinit var alert: Alert
    
    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet

    @FXML
    fun onClickSubmitButton () {
        val connectDb = SchoolDb()
        val codeQuery = "SELECT * FROM code_table WHERE resetCode = ?"
        connection = connectDb.connectionDb()!!
        
        try {
            preparedStatement = connection.prepareStatement(codeQuery)
            preparedStatement.setString(1, resetCode.text)

            // Execute Query
            resultSet = preparedStatement.executeQuery()

            if (resetCode.text.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Blank field"
                alert.headerText = null
                alert.contentText = "Please fill the code"
                alert.showAndWait()
            } else {
                if (resultSet.next()) {
                    submitButton.scene.window.hide()
                    val newPassword = FXMLLoader(
                        NewPasswordController::class.java.getResource("new-password.fxml"))
                    val newPasswordStage = Stage ()
                    val scene = Scene(newPassword.load())
                    newPasswordStage.scene = scene
                    newPasswordStage.title = "Change Password"
                    newPasswordStage.show()
                } else {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Wrong Reset Code"
                    alert.headerText = null
                    alert.contentText = "Your Reset password code is Wrong\nConsult Admin"
                    alert.showAndWait()
                    resetCode.clear()
                }
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}