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

class SchoolTypeController : Initializable{


    @FXML
    private var columnSchoolType = TableColumn<SchoolType, String>()

    @FXML
    private lateinit var schoolTypeDeleteButton: Button

    @FXML
    private lateinit var schoolTypeNewButton: Button

    @FXML
    private lateinit var schoolTypeSaveButton: Button

    @FXML
    private var schoolTypeTableView = TableView<SchoolType>()

    @FXML
    private var rowNumberColumn = TableColumn<SchoolType, Int>()

    @FXML
    private lateinit var schoolTypeUpdateButton: Button

    @FXML
    private lateinit var schoolTypeField: TextField

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    private lateinit var alert: Alert

    private lateinit var schoolTypeDataList: ObservableList<SchoolType>

    fun onClickSchoolTypeField() {
        // Activate all Buttons
        schoolTypeDeleteButton.isDisable = false
        schoolTypeUpdateButton.isDisable = false
        schoolTypeSaveButton.isDisable = false
    }

    fun onClickSaveButton() {
        // Database connection
        val connectToDb = SchoolDb()
        val schoolTypeDataQuery = "INSERT INTO school_type(schooltype) VALUES (?)"
        connection = connectToDb.connectionDb()!!

        try {
            val schoolTypeName = schoolTypeField.text
            if (schoolTypeName.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty Field"
                alert.headerText = null
                alert.contentText = "Please Enter School Type"
                alert.showAndWait()
            } else {
                // Prevent Addition of the same School Type
                val schoolTypeCheck = "SELECT schooltype FROM school_type WHERE schooltype ='$schoolTypeName'"
                preparedStatement = connection.prepareStatement(schoolTypeCheck)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Existed School Type"
                    alert.headerText = null
                    alert.contentText = "School Type $schoolTypeName already Exists"
                    alert.showAndWait()
                } else {
                    preparedStatement = connection.prepareStatement(schoolTypeDataQuery)
                    //preparedStatement.setString(1, "")
                    preparedStatement.setString(1, schoolTypeName)

                    preparedStatement.executeUpdate()

                    alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "School Type Data"
                    alert.headerText = null
                    alert.contentText = "School Type Successfully Added"
                    alert.showAndWait()

                    //Show on table
                    availableSchoolTypeList()
                    schoolTypeField.clear()
                }
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }

    }

    fun availableSchoolTypeSelectData() {
        val selectedSchooType: SchoolType = schoolTypeTableView.selectionModel.selectedItem
        val number: Int = schoolTypeTableView.selectionModel.selectedIndex
        if ((number - 1) < -1) {
            return
        }
        schoolTypeField.text = selectedSchooType.schoolType
    }

    private fun schoolTypeListData(): ObservableList<SchoolType> {
        val schoolTypeListData = FXCollections.observableArrayList<SchoolType>()
        val connectToDb = SchoolDb()
        val selectQuery = "SELECT * FROM school_type"

        connection = connectToDb.connectionDb()!!

        try {
            //var schoolTypeData: SchoolType
            preparedStatement = connection.prepareStatement(selectQuery)
            resultSet = preparedStatement.executeQuery()

            var rowNumber = 1
            while (resultSet.next()) {
                val columnSchoolType = resultSet.getString("Schooltype")
                val schoolTypeData = SchoolType(rowNumber, columnSchoolType)

                schoolTypeListData.add(schoolTypeData)
                rowNumber++
            }
        } catch(sqlException: SQLException) {
            sqlException.printStackTrace()
        }
        return schoolTypeListData
    }

    fun availableSchoolTypeList() {
        schoolTypeDataList = schoolTypeListData()

        columnSchoolType.cellValueFactory = PropertyValueFactory("schoolType")
        rowNumberColumn.setCellValueFactory { SimpleIntegerProperty(it.value.rowNumber).asObject() }
        schoolTypeTableView.items = schoolTypeDataList

    }

    fun onClickNewButton() {
        reset()
    }

    private fun reset() {
        schoolTypeField.text = ""
        schoolTypeDeleteButton.isDisable = true
        schoolTypeUpdateButton.isDisable = true
        schoolTypeSaveButton.isDisable = true
    }

    fun onClickDeleteButton() {
        val schoolType = schoolTypeField.text
        val dbConnection = SchoolDb()
        val deleteSchoolTypeQuery = "DELETE FROM school_type WHERE schooltype = '$schoolType'"

        connection = dbConnection.connectionDb()!!

        try {
            //Check Empty Fields
            if (schoolType.isEmpty()) {
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty School Type"
                alert.headerText = null
                alert.contentText = "Please Select School Type to delete"
                alert.showAndWait()
            } else {

                // Prevent Addition of the same School Type
                val schoolTypeCheck = "SELECT schooltype FROM school_type WHERE schooltype ='$schoolType'"
                preparedStatement = connection.prepareStatement(schoolTypeCheck)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {

                    alert = Alert(Alert.AlertType.CONFIRMATION)
                    alert.title = "Delete School Type"
                    alert.headerText = "You want to delete School Type"
                    alert.contentText = "Are you sure?"

                    val option: Optional<ButtonType> = alert.showAndWait()
                    if (option.get() == ButtonType.OK) {
                        statement = connection.createStatement()
                        statement.executeUpdate(deleteSchoolTypeQuery)

                        alert = Alert(Alert.AlertType.INFORMATION);
                        alert.title = "Deleted School Type"
                        alert.headerText = null;
                        alert.contentText = "Successfully Deleted";
                        alert.showAndWait()

                        availableSchoolTypeList()
                        schoolTypeField.clear()
                    }
                } else {
                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Existed School Type"
                    alert.headerText = null
                    alert.contentText = "School Type $schoolType does not Exists"
                    alert.showAndWait()
                    schoolTypeField.clear()
                }

            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    fun onClickUpdateButton() {

    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        availableSchoolTypeList()
    }

}