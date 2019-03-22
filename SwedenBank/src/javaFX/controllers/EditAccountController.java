package javaFX.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditAccountController {

   @FXML
   private TextField accountNameField;

   @FXML
   private CheckBox savingAccountCheckBox;

   @FXML
   private Label nameError;

   @FXML
   private Label connectionError;

   public TextField getAccountNameField() {
      return accountNameField;
   }

   public CheckBox getSavingAccountCheckBox() {
      return savingAccountCheckBox;
   }

   public Label getNameError() {
      return nameError;
   }

   public Label getConnectionError() {
      return connectionError;
   }
}


