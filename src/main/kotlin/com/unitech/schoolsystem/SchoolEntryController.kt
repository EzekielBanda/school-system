package com.unitech.schoolsystem

import com.neovisionaries.i18n.CountryCode
import com.unitech.schoolsystem.database.SchoolDb
import com.unitech.schoolsystem.model.School
import com.unitech.schoolsystem.model.SchoolType
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import java.net.URL
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class SchoolEntryController : Initializable {

    @FXML
    private var address = TextArea()

    @FXML
    private lateinit var browseButton: Button

    @FXML
    private var city = TextField()

    @FXML
    private var contactNumber = TextField()

    @FXML
    private var country = ComboBox<String>()

    @FXML
    private var email = TextField()

    @FXML
    private var fax =  TextField()

    @FXML
    private lateinit var removeButton: Button

    @FXML
    private lateinit var schooSaveButton: Button

    @FXML
    private lateinit var schoolDelButton: Button

    @FXML
    private lateinit var schoolImage: ImageView

    @FXML
    private var schoolName = TextField()

    @FXML
    private lateinit var schoolNewButton: Button

    @FXML
    private var schoolTableView = TableView<School>()

    @FXML
    private var schoolTableNameColumn = TableColumn<School, String>()

    @FXML
    private var schoolTableTypeColumn = TableColumn<School, String>()

    @FXML
    private var schoolType = ComboBox<String>()

    @FXML
    private lateinit var schoolUpdateButton: Button;

    @FXML
    private var tableAddressColumn = TableColumn<School, String>()

    @FXML
    private var schoolRowNumberColumn = TableColumn<School, Int>()

    @FXML
    private var tableCityColumn = TableColumn<School, String>()

    @FXML
    private var tableContactNoColumn = TableColumn<School, String>()

    @FXML
    private var tableCountryColumn = TableColumn<School, String>()

    @FXML
    private var tableEmailColumn = TableColumn<School, String>()

    @FXML
    private var tableFaxColumn = TableColumn<School, String>()

    @FXML
    private var tableWebsiteColumn = TableColumn<School, String>()

    @FXML
    private var website = TextField()

    private lateinit var schoolEntryDataList: ObservableList<School>

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    private var connectToDb = SchoolDb()

    private lateinit var schoolAlert: Alert

    /*private var school =  schoolName.text
    private var type = schoolType.selectionModel.selectedItem
    private val schoolCity = city.text
    private val schoolCountry = country.selectionModel.selectedItem
    private val contactNum = contactNumber.text
    private val schoolAddress = address.text
    private val schoolEmail = email.text
    private val schoolFax = fax.text
    private val schoolWeb = website.text

     */

    @FXML
    fun onClickCountryDropDown() {

        // Get a list of all Countries
        val countryList = CountryCode.values()
            .map { it.getName() }
            .toList()
        // Create an ObservableList and add the country names

        // Set Items of the comboBox to the observableList
        country.items.addAll(countryList)
    }

    @FXML
    fun onClickSchoolTypeComboBox() {
        val connectToDb = SchoolDb()
        val schoolTypeSql = "SELECT schooltype FROM school_type "
        connection = connectToDb.connectionDb()!!

        try {
            preparedStatement = connection.prepareStatement(schoolTypeSql)
            resultSet = preparedStatement.executeQuery()

            // Create list to store items
            val schoolTypeList = mutableListOf<String>()

            // Populate the list with data
            while (resultSet.next()) {
                val schoolTypeName = resultSet.getString("schooltype")
                schoolTypeList.add(schoolTypeName)
            }
            schoolType.items.addAll(schoolTypeList)

        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }


    }

    @FXML
    fun onClickSchoolSaveButton() {
        //Database initialisation
        val schoolEntryQuery = "INSERT INTO school_data(" +
                "school_name, school_typ, city, country_name, contact_number, address, email, fax, website)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
        connection = connectToDb.connectionDb()!!

//        val school =  schoolName.text
//        val type = schoolType.selectionModel.selectedItem
//        val schoolCity = city.text
//        val schoolCountry = country.selectionModel.selectedItem
//        val contactNum = contactNumber.text
//        val schoolAddress = address.text
//        val schoolEmail = email.text
//        val schoolFax = fax.text
//        val schoolWeb = website.text

        try {

            val schoolInfo = extractSchoolInfo()

            if (schoolInfo.schoolName.isEmpty() ||
                schoolInfo.city.isEmpty() ||
                schoolInfo.contactNumber.isEmpty() ||
                schoolInfo.schoolAddress.isEmpty() ||
                schoolInfo.emailAddress.isEmpty() ||
                schoolInfo.fax.isEmpty() || schoolInfo.schoolWebsite.isEmpty()
            ) {

                // Alert Box
                schoolAlert = Alert(Alert.AlertType.ERROR)
                schoolAlert.title = "Empty Fields"
                schoolAlert.headerText = null
                schoolAlert.contentText = "Please fill required Fields"
                schoolAlert.showAndWait()

            } else {

                // Prevent Addition of the same School Type
                val schoolTypeCheck = "SELECT school_typ, city FROM school_data " +
                        "WHERE school_typ = '${schoolInfo.schoolType}' AND city = '${schoolInfo.city}'"
                preparedStatement = connection.prepareStatement(schoolTypeCheck)
                resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    // Check
                    schoolAlert = Alert(Alert.AlertType.ERROR)
                    schoolAlert.title = "Existed School Type"
                    schoolAlert.headerText = null
                    schoolAlert.contentText = "You cannot Have two schools of the same type\n" +
                            "in the same city"
                    schoolAlert.showAndWait()
                } else {
                    val newSchool = extractSchoolInfo()


                    preparedStatement = connection.prepareStatement(schoolEntryQuery)
                    preparedStatement.setString(1, newSchool.schoolName)
                    preparedStatement.setString(2, newSchool.schoolType)
                    preparedStatement.setString(3, newSchool.city)
                    preparedStatement.setString(4, newSchool.country)
                    preparedStatement.setString(5, newSchool.contactNumber)
                    preparedStatement.setString(6, newSchool.schoolAddress)
                    preparedStatement.setString(7, newSchool.emailAddress)
                    preparedStatement.setString(8, newSchool.fax)
                    preparedStatement.setString(9, newSchool.schoolWebsite)

                    preparedStatement.executeUpdate()

                    schoolAlert = Alert(Alert.AlertType.INFORMATION)
                    schoolAlert.title = "School Information"
                    schoolAlert.headerText = null
                    schoolAlert.contentText = "School Type Successfully Added"
                    schoolAlert.showAndWait()

                    //Show Table
                    availableSchoolEntryList()

                    // Clear data
                    clearTextFieldData()
                }
            }

        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }

    }

    private fun extractSchoolInfo(): School {
        return School(
            schoolName = schoolName.text,
            schoolType = schoolType.selectionModel.selectedItem,
            city = city.text,
            country = country.selectionModel.selectedItem,
            schoolAddress = address.text,
            contactNumber = contactNumber.text,
            fax = fax.text,
            emailAddress = email.text,
            schoolWebsite = website.text
        )
    }


    fun onClickSchoolDeleteButton() {
        val schoolInfo = extractSchoolInfo()

        try {
            // Check Empty Fields
            if (schoolInfo.schoolName.isEmpty() ||
                schoolInfo.city.isEmpty() ||
                schoolInfo.contactNumber.isEmpty() ||
                schoolInfo.schoolAddress.isEmpty() ||
                schoolInfo.emailAddress.isEmpty() ||
                schoolInfo.fax.isEmpty() || schoolInfo.schoolWebsite.isEmpty()
            ) {
                showAlert("Empty School Type", "Please Select School to delete")
            } else {
                val schoolDataCheck =
                    "SELECT school_typ, city FROM school_data " +
                            " WHERE school_typ = '${schoolInfo.schoolType}' AND city = '${schoolInfo.city}' "
                preparedStatement = connection.prepareStatement(schoolDataCheck)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    confirmAndDeleteSchool()
                } else {
                    showAlert("Existed School Type", "School with that data does not exist")
                    clearTextFieldData()
                }
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    private fun confirmAndDeleteSchool() {
        val deleteSchoolQuery = "" +
                " DELETE FROM school_data " +
                " WHERE school_typ = ? AND city = ?"

        schoolAlert = Alert(Alert.AlertType.CONFIRMATION)
        schoolAlert.title = "Delete School"
        schoolAlert.headerText = "You want to delete School"
        schoolAlert.contentText = "Are you sure?"

        val option: Optional<ButtonType> = schoolAlert.showAndWait()
        if (option.get() == ButtonType.OK) {
            try {
                preparedStatement = connection.prepareStatement(deleteSchoolQuery)
                preparedStatement.setString(1, schoolType.selectionModel.selectedItem)
                preparedStatement.setString(2, city.text)

                preparedStatement.executeUpdate()

                showAlert("Deleted School", "Successfully Deleted")
                availableSchoolEntryList()
                clearTextFieldData()
            } catch (sqlException: SQLException) {
                sqlException.printStackTrace()
            }
        }
    }

    private fun showAlert(title: String, content: String) {
        schoolAlert = Alert(Alert.AlertType.ERROR)
        schoolAlert.title = title
        schoolAlert.headerText = null
        schoolAlert.contentText = content
        schoolAlert.showAndWait()
    }

    @FXML
    fun onClickNewButton() {
        reset()
    }

    fun onClickTextField() {
        schooSaveButton.isDisable = false
        schoolUpdateButton.isDisable = false
        schoolDelButton.isDisable = false
    }

    fun availableSchoolEntrySelectData() {
        val selectedSchoolEntry: School = schoolTableView.selectionModel.selectedItem
        val number: Int = schoolTableView.selectionModel.selectedIndex
        if ((number - 1) < -1) {
            return
        }
        schoolName.text = selectedSchoolEntry.schoolName
        schoolType.value = selectedSchoolEntry.schoolType.toString()
        city.text = selectedSchoolEntry.city
        country.value = selectedSchoolEntry.country.toString()
        contactNumber.text = selectedSchoolEntry.contactNumber
        address.text = selectedSchoolEntry.schoolAddress
        email.text = selectedSchoolEntry.emailAddress
        fax.text = selectedSchoolEntry.fax
        website.text = selectedSchoolEntry.schoolWebsite
    }

    fun availableSchoolEntryList() {
        schoolEntryDataList = schoolEntryListData()

        schoolTableNameColumn.cellValueFactory = PropertyValueFactory("schoolName")
        schoolTableTypeColumn.cellValueFactory = PropertyValueFactory("schoolType")
        tableCityColumn.cellValueFactory = PropertyValueFactory("city")
        tableCountryColumn.cellValueFactory = PropertyValueFactory("country")
        tableContactNoColumn.cellValueFactory = PropertyValueFactory("contactNumber")
        tableAddressColumn.cellValueFactory = PropertyValueFactory("schoolAddress")
        tableEmailColumn.cellValueFactory = PropertyValueFactory("emailAddress")
        tableFaxColumn.cellValueFactory = PropertyValueFactory("fax")
        tableWebsiteColumn.cellValueFactory = PropertyValueFactory("schoolWebsite")
        schoolRowNumberColumn.setCellValueFactory { SimpleIntegerProperty(it.value.schoolRowNumber).asObject() }
        schoolTableView.items = schoolEntryDataList

    }

    private fun schoolEntryListData(): ObservableList<School> {
        val schoolTypeListData = FXCollections.observableArrayList<School>()
        val connectToDb = SchoolDb()
        val selectQuery = "SELECT * FROM school_data"

        connection = connectToDb.connectionDb()!!

        try {
            //var schoolTypeData: SchoolType
            preparedStatement = connection.prepareStatement(selectQuery)
            resultSet = preparedStatement.executeQuery()

            var schoolRowNumber = 1
            while (resultSet.next()) {
                val columnSchoolName = resultSet.getString("school_name")
                val columnSchoolType = resultSet.getString("school_typ")
                val columnSchoolCity = resultSet.getString("city")
                val columnSchoolCountry = resultSet.getString("country_name")
                val columnSchoolContactNo = resultSet.getString("contact_number")
                val columnSchoolAddress = resultSet.getString("address")
                val columnSchoolEmail = resultSet.getString("email")
                val columnSchoolFax = resultSet.getString("fax")
                val columnSchoolWebsite = resultSet.getString("website")
                val schoolEntryData = School(
                    schoolRowNumber,
                    columnSchoolName,
                    columnSchoolType,
                    columnSchoolCity,
                    columnSchoolCountry,
                    columnSchoolContactNo,
                    columnSchoolAddress,
                    columnSchoolEmail,
                    columnSchoolFax,
                    columnSchoolWebsite
                )

                schoolTypeListData.add(schoolEntryData)
                schoolRowNumber++
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
        return schoolTypeListData
    }

    private fun reset() {
        schoolName.text = ""
        email.text = ""
        city.text = ""
        fax.text = ""
        contactNumber.text = ""
        country.selectionModel.clearSelection()
        website.text = ""
        address.text = ""
        schoolType.selectionModel.clearSelection()
        schooSaveButton.isDisable = true
        schoolDelButton.isDisable = true
        schoolUpdateButton.isDisable = true
    }

    private fun clearTextFieldData() {
        schoolName.clear()
        email.clear()
        city.clear()
        fax.clear()
        contactNumber.clear()
        country.selectionModel.clearSelection()
        website.clear()
        address.clear()
        schoolType.selectionModel.clearSelection()
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        //
        availableSchoolEntryList()
    }
}