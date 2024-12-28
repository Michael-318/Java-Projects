package com.example.lab5;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML public TextField tfStudentId;
    @FXML public TextField tfGPATerm1;
    @FXML public TextField tfGPATerm2;
    @FXML public TextField tfGPATerm3;

    @FXML public Label lblOutput;

    @FXML private TableView<GPA> GPATableView;
    @FXML private TableColumn<GPA, Integer> colId;
    @FXML private TableColumn<GPA, Double> colGPA1;
    @FXML private TableColumn<GPA, Double> colGPA2;
    @FXML private TableColumn<GPA, Double> colGPA3;
    @FXML private TableColumn<GPA, Double> colCGPA;

    DatabaseConnector dbConnector = DatabaseConnector.getInstance();


    @FXML protected void onSaveButtonClick() {
        lblOutput.setText("GPA Information");

        String output = "GPA Information\n";
        int id = 0;
        double gpa1 = 0;
        double gpa2 = 0;
        double gpa3 = 0;


        try {
            id = Integer.parseInt(this.tfStudentId.getText());
            if (id < 10000 || id >99999) {
                output += "Id must be 5 digit ";
            }
            else if (this.dbConnector.isIdDuplicate(id)) {
                output += "Id already exists\n";
            }
        } catch (NumberFormatException e) {
            output += "Invalid Id input : " + e;
        }

        try {
            gpa1 = Double.parseDouble(this.tfGPATerm1.getText());
            if (gpa1 < 0 || gpa1 > 4) {
                output += "GPA must be between 0 and 4";
                gpa1 = 0;
            }
        } catch (NumberFormatException e) {
            output += "Invalid GPA input : " + e;
        }


        try {
            gpa2 = Double.parseDouble(this.tfGPATerm2.getText());
            if (gpa2 < 0 || gpa2 > 4) {
                output += "GPA must be between 0 and 4";
                gpa2 = 0;
            }
        } catch (NumberFormatException e) {
            output += "Invalid GPA input : " + e;
        }

        try {
            gpa3 = Double.parseDouble(this.tfGPATerm3.getText());
            if (gpa3 < 0 || gpa3 > 4) {
                output += "GPA must be between 0 and 4";
                gpa3 = 0;
            }
        } catch (NumberFormatException e) {
            output += "Invalid GPA input : " + e;
        }

        lblOutput.setText(output);

        if (output.equals("GPA Information\n")) {
            GPA gpaToInsert = new GPA(id, gpa1, gpa2, gpa3);
            boolean success = this.dbConnector.insertToDB(gpaToInsert);
            if (success) {
                this.GPATableView.setItems(this.dbConnector.GPAList);
                this.GPATableView.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Insert - Success");
                alert.setContentText(gpaToInsert.getId() + " saved to database");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Insert - Error");
                alert.setContentText(gpaToInsert.getId() + " not saved to database");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Insert - Error");
            alert.setContentText("Please enter all details correctly");
            alert.show();
        }
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //associate the table View columns to Book class properties
        this.colId.setCellValueFactory(new PropertyValueFactory<GPA, Integer>("id"));
        this.colGPA1.setCellValueFactory(new PropertyValueFactory<GPA, Double>("gpa1"));
        this.colGPA2.setCellValueFactory(new PropertyValueFactory<GPA, Double>("gpa2"));
        this.colGPA3.setCellValueFactory(new PropertyValueFactory<GPA, Double>("gpa3"));
        this.colCGPA.setCellValueFactory(new PropertyValueFactory<GPA, Double>("cgpa"));



        this.GPATableView.setItems(this.dbConnector.GPAList);
        this.GPATableView.refresh();
    }

    public void onQuitClick(ActionEvent actionEvent) {
        //close database connection
        this.dbConnector.closeConnections();

        //close GUI window
        Platform.exit();
    }
}