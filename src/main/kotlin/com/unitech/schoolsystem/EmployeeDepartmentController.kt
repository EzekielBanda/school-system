package com.unitech.schoolsystem

import com.unitech.schoolsystem.database.SchoolDb
import com.unitech.schoolsystem.model.EmployeeDepartment
import com.unitech.schoolsystem.model.SchoolType
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class EmployeeDepartmentController: Initializable {
    @FXML
    private var departmentColumn = TableColumn<EmployeeDepartment, String>()

    @FXML
    private var rowNumberColumn = TableColumn<EmployeeDepartment, Int>()

    @FXML
    private lateinit var departmentDeleteButton: Button

    @FXML
    private lateinit var departmentNewButton: Button

    @FXML
    private lateinit var departmentSaveButton: Button

    @FXML
    private var departmentTableView = TableView<EmployeeDepartment>()

    @FXML
    private lateinit var departmentUpdateButton: Button

    @FXML
    private lateinit var employeeDepartmentField: TextField

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    private lateinit var alert: Alert

    private lateinit var employeeDepartmentDataList: ObservableList<EmployeeDepartment>

    @FXML
    fun onClickEmployeeDepartTextField() {
        // Activate all Buttons
        departmentDeleteButton.isDisable = false
        departmentSaveButton.isDisable = false
        departmentUpdateButton.isDisable = false
    }

    fun onClickEmployeeDepartmentSaveButton() {
        // Database connection
        val connectToDb = SchoolDb()
        val departmentDataQuery = "INSERT INTO employee_department(employee_depart) VALUES (?)"
        connection = connectToDb.connectionDb()!!

        try {
            val departmentName = employeeDepartmentField.text
            if (departmentName.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty Field"
                alert.headerText = null
                alert.contentText = "Please Enter Department Name"
                alert.showAndWait()
            } else {
                // Prevent Addition of the same School Type
                val departmentNameCheck = "SELECT employee_depart FROM employee_department WHERE employee_depart ='$departmentName'"
                preparedStatement = connection.prepareStatement(departmentNameCheck)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Existed Department Name"
                    alert.headerText = null
                    alert.contentText = "$departmentName Department already Exists"
                    alert.showAndWait()
                } else {
                    preparedStatement = connection.prepareStatement(departmentDataQuery)
                    preparedStatement.setString(1, departmentName)

                    preparedStatement.executeUpdate()

                    alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Department Name"
                    alert.headerText = null
                    alert.contentText = "Department Name Successfully Added"
                    alert.showAndWait()

                    //Show on table
                    availableEmployeeDepartmentList()
                    employeeDepartmentField.clear()

                }
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    fun onClickEmployeeDepartmentDeleteButton() {
        val departmentName = employeeDepartmentField.text
        val dbConnection = SchoolDb()
        val deleteQuery = "DELETE FROM employee_department WHERE employee_depart = '$departmentName'"

        connection = dbConnection.connectionDb()!!

        try {
            //Check Empty Fields
            if (departmentName.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty Department"
                alert.headerText = null
                alert.contentText = "Please Select Department to delete"
                alert.showAndWait()
            } else {
                // Prevent deletion of the unknown Department
                val departmentNameCheck = "SELECT employee_depart FROM employee_department WHERE employee_depart ='$departmentName'"
                preparedStatement = connection.prepareStatement(departmentNameCheck)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {

                    alert = Alert(Alert.AlertType.CONFIRMATION)
                    alert.title = "Delete Department"
                    alert.headerText = "You want to delete Department"
                    alert.contentText = "Are you sure?"

                    val option: Optional<ButtonType> = alert.showAndWait()
                    if (option.get() == ButtonType.OK) {
                        statement = connection.createStatement()
                        statement.executeUpdate(deleteQuery)

                        alert = Alert(Alert.AlertType.INFORMATION);
                        alert.title = "Deleted Department"
                        alert.headerText = null;
                        alert.contentText = "Successfully Deleted";
                        alert.showAndWait()

                        availableEmployeeDepartmentList()
                        employeeDepartmentField.clear()
                    }
                } else {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Existed Department Name"
                    alert.headerText = null
                    alert.contentText = "$departmentName Department does not Exists"
                    alert.showAndWait()
                    employeeDepartmentField.clear()
                }

            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    fun onClickEmployeeDepartmentUpdateButton() {
        val departmentName = employeeDepartmentField.text
        val dbConnection = SchoolDb()
        val updateQuery = "UPDATE employee_department SET employee_depart = '$departmentName' " +
                "WHERE employee_depart = '$departmentName'"

        connection = dbConnection.connectionDb()!!

        try {
            //Check Empty Fields
            if (departmentName.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty Department"
                alert.headerText = null
                alert.contentText = "Please Select Department to update"
                alert.showAndWait()
            } else {
                preparedStatement = connection.prepareStatement(updateQuery)
                preparedStatement.executeUpdate()
                alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Department Name"
                alert.headerText = null
                alert.contentText = "Department Successfully Updated"
                alert.showAndWait()

                availableEmployeeDepartmentList()
                employeeDepartmentField.clear()
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    fun onClickNewButton() {
        reset()
    }

    private fun employeeDepartmentListData(): ObservableList<EmployeeDepartment> {
        val employeeDepartmentListData = FXCollections.observableArrayList<EmployeeDepartment>()
        val connectToDb = SchoolDb()
        val selectQuery = "SELECT * FROM employee_department"

        connection = connectToDb.connectionDb()!!

        try {
            //var employeeDepartmentData: EmployeeDepartment
            preparedStatement = connection.prepareStatement(selectQuery)
            resultSet = preparedStatement.executeQuery()

            var rowNumber = 1
            while (resultSet.next()) {
                val columnEmployeeDepart = resultSet.getString("employee_depart")
                val employeeDepartmentData = EmployeeDepartment(rowNumber, columnEmployeeDepart)

                employeeDepartmentListData.add(employeeDepartmentData)
                rowNumber++
            }
        } catch(sqlException: SQLException) {
            sqlException.printStackTrace()
        }
        return employeeDepartmentListData
    }

    fun availableEmployeeDepartmentList() {
        employeeDepartmentDataList = employeeDepartmentListData()

        departmentColumn.cellValueFactory = PropertyValueFactory("employeeDepartment")
        rowNumberColumn.setCellValueFactory { SimpleIntegerProperty(it.value.rowNumber).asObject()}
        departmentTableView.items = employeeDepartmentDataList

    }

    fun availableEmployeeDepartmentSelectData() {
        val selectedDepartment: EmployeeDepartment = departmentTableView.selectionModel.selectedItem
        val number: Int = departmentTableView.selectionModel.selectedIndex
        if ((number - 1) < -1) {
            return
        }
        employeeDepartmentField.text = selectedDepartment.employeeDepartment
    }

    private fun reset() {
        employeeDepartmentField.text = ""
        departmentDeleteButton.isDisable = true
        departmentSaveButton.isDisable = true
        departmentUpdateButton.isDisable = true
    }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        availableEmployeeDepartmentList()
    }
}