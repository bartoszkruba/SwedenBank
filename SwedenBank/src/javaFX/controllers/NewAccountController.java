package javaFX.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class NewAccountController {

   @FXML
   private Label connectionError;

   @FXML
   private TextField accountNameTextField;

   @FXML
   private Label accountNameError;

   @FXML
   private CheckBox savingAccountCheckBox;


   public TextField getAccountNameTextField() {
      return accountNameTextField;
   }

   public Label getAccountNameError() {
      return accountNameError;
   }

   public CheckBox getSavingAccountCheckBox() {
      return savingAccountCheckBox;
   }

   public Label getConnectionError() {
      return connectionError;
   }
}
