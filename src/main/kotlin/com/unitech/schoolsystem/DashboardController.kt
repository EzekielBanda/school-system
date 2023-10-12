package com.unitech.schoolsystem

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.MenuItem
import javafx.stage.Stage

class DashboardController {

    @FXML
    private lateinit var classEntry : MenuItem

    @FXML
    private lateinit var classSection: MenuItem

    @FXML
    private lateinit var schoolType: MenuItem

    @FXML
    private lateinit var classType: MenuItem

    @FXML
    private lateinit var employeeDepartment: MenuItem

    @FXML
    private lateinit var employeeDesignation: MenuItem

    @FXML
    private lateinit var feeTypes: MenuItem

    @FXML
    private lateinit var schoolEntry: MenuItem

    @FXML
    private lateinit var schoolFeesEntry: MenuItem

    @FXML
    private lateinit var sectionEntry: MenuItem

    @FXML
    fun onSelectSchoolEntry () {
        val schoolEntry = SchoolEntryController()
        val schoolEntryForm = FXMLLoader(
            SchoolEntryController::class.java
                .getResource("school-entry.fxml"))
        val schoolEntryStage = Stage ()
        val scene = Scene(schoolEntryForm.load())
        schoolEntryStage.scene = scene
        schoolEntryStage.title = "School Entry"
        schoolEntryStage.isResizable = false
        schoolEntryStage.show()
        schoolEntry.availableSchoolEntryList()
    }

    @FXML
    fun onSelectClassType () {
        val classTypeForm = FXMLLoader(
            ClassTypeController::class.java
                .getResource("class-type.fxml"))
        val schoolEntryStage = Stage ()
        val scene = Scene(classTypeForm.load())
        schoolEntryStage.scene = scene
        schoolEntryStage.title = "Class Type"
        schoolEntryStage.isResizable = false
        schoolEntryStage.show()
    }

    @FXML
    fun onSelectClassEntry () {
        val classEntryForm = FXMLLoader(
            ClassEntryController::class.java
                .getResource("class-entry.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Class Entry"
        classEntryStage.isResizable = false
        classEntryStage.show()
    }

    @FXML
    fun onSelectSectionEntry () {
        val sectionEntryForm = FXMLLoader(
            SectionEntryController::class.java
                .getResource("section-entry.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(sectionEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Section Entry"
        classEntryStage.isResizable = false
        classEntryStage.show()
    }

    @FXML
    fun onSelectClassSection () {
        val classEntryForm = FXMLLoader(
            ClassSectionsController::class.java
                .getResource("class-sections.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Class Section"
        classEntryStage.isResizable = false
        classEntryStage.show()
    }

    @FXML
    fun onSelectEmployeeDepartment () {
        val showEmployeeDepartment = EmployeeDepartmentController()
        val classEntryForm = FXMLLoader(
            EmployeeDepartmentController::class.java
                .getResource("employee-department.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Employee Department"
        classEntryStage.isResizable = false
        classEntryStage.show()
        showEmployeeDepartment.availableEmployeeDepartmentList()
    }

    @FXML
    fun onSelectFeeType () {
        val classEntryForm = FXMLLoader(
            FeeTypeController::class.java
                .getResource("fee-types.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Fees Type"
        classEntryStage.isResizable = false
        classEntryStage.show()
    }

    @FXML
    fun onSelectSchoolFeeEntry () {
        val classEntryForm = FXMLLoader(
            SchoolFeeEntryController::class.java
                .getResource("school-fee-entry.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "School Fee "
        classEntryStage.isResizable = false
        classEntryStage.show()
    }

    @FXML
    fun onSelectSchoolTypeEntry () {
        val showTableData = SchoolTypeController()
        val schoolTypeForm = FXMLLoader(
            SchoolTypeController::class.java
                .getResource("school-type.fxml"))
        val schoolTypeStage = Stage ()
        val scene = Scene(schoolTypeForm.load())
        schoolTypeStage.scene = scene
        schoolTypeStage.title = "School Type "
        schoolTypeStage.isResizable = false
        schoolTypeStage.show()
        showTableData.availableSchoolTypeList()
    }

    @FXML
    fun onSelectEmployeeDesignation () {
        val classEntryForm = FXMLLoader(
            EmployeeDesignation::class.java
                .getResource("employee-designation.fxml"))
        val classEntryStage = Stage ()
        val scene = Scene(classEntryForm.load())
        classEntryStage.scene = scene
        classEntryStage.title = "Employee Designation "
        classEntryStage.isResizable = false
        classEntryStage.show()
    }
}