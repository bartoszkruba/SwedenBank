package javaFX.controllers.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;

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
   TextField transactionLimit;

   @FXML
   private void initialize() {
      addFormatter();
   }

   private void addFormatter() {
      UnaryOperator<TextFormatter.Change> filter = change -> {
         String text = change.getText();

         if (text.matches("[0-9]*")) {
            return change;
         }

         return null;
      };
      TextFormatter<String> textFormatter = new TextFormatter<>(filter);
      transactionLimit.setTextFormatter(textFormatter);
   }

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

   public TextField getTransactionLimit() {
      return transactionLimit;
   }
}


