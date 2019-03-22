package javaFX.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class EditAccountController {

   @FXML
   TextField accountNameField;

   @FXML
   CheckBox savingAccountCheckBox;


   public TextField getAccountNameField() {
      return accountNameField;
   }

   public CheckBox getSavingAccountCheckBox() {
      return savingAccountCheckBox;
   }

}


