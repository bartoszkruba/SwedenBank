package javaFX.controllers.dialogs;

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
   private CheckBox cardAccountCheckBox;

   @FXML
   private CheckBox salaryAccountCheckBox;

   @FXML
   private Label nameError;

   @FXML
   private Label connectionError;

   @FXML
   private void cardAccountSelected() {
      if (cardAccountCheckBox.isSelected()) {
         savingAccountCheckBox.setSelected(false);
      }
   }

   @FXML
   private void savingsAccountSelected() {
      if (savingAccountCheckBox.isSelected()) {
         cardAccountCheckBox.setSelected(false);
      }
   }

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

   public CheckBox getCardAccountCheckBox() {
      return cardAccountCheckBox;
   }

   public CheckBox getSalaryAccountCheckBox() {
      return salaryAccountCheckBox;
   }
}


