package javaFX.controllers.dialogs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.BankAccount;

import java.util.Optional;
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

   private SwedenBankDatasource swedenBankDatasource;

   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
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

   public boolean validateAccount() {
      nameError.setVisible(false);
      connectionError.setVisible(false);
      String personNr = state.getUser().getPersonNr();
      String name = accountNameField.getText();
      try {
         BankAccount account = swedenBankDatasource.queryAccountOnName(personNr, name);
         if (account != null && !name.equals(state.getCurrentAccount().getName())) {
            nameError.setVisible(true);
            return false;
         }
      } catch (Exception e) {
         connectionError.setVisible(true);
         return false;
      }
      return true;
   }

   public void fillUpEditAccountDialog() {
      accountNameField.setText(state.getCurrentAccount().getName());

      String savingAccount = state.getCurrentAccount().getSavingAccount();
      String cardAccount = state.getCurrentAccount().getCardAccount();
      String salaryAccount = state.getCurrentAccount().getSalaryAccount();

      int limit = (int) state.getCurrentAccount().getLimit();

      transactionLimit.setText(Integer.toString(limit));

      if (savingAccount.equals("Y")) {
         savingAccountCheckBox.setSelected(true);
      } else {
         savingAccountCheckBox.setSelected(false);
      }

      if (cardAccount.equals("Y")) {
         cardAccountCheckBox.setSelected(true);
      } else {
         cardAccountCheckBox.setSelected(false);
      }

      if (salaryAccount.equals("Y")) {
         cardAccountCheckBox.setSelected(true);
      } else {
         cardAccountCheckBox.setSelected(false);
      }
   }

   public void processEditAccount(Optional<ButtonType> result) {
      if (result.isPresent() && result.get() == ButtonType.OK) {
         String newName = accountNameField.getText();

         boolean isSavingAccount = savingAccountCheckBox.isSelected();
         boolean isCardAccount = cardAccountCheckBox.isSelected();
         boolean isSalaryAccount = salaryAccountCheckBox.isSelected();
         double accountLimit = Double.parseDouble(transactionLimit.getText());

         String accountNumber = state.getCurrentAccount().getAccountNumber();

         swedenBankDatasource.updateAccount(
                 accountNumber, newName, isSavingAccount, isCardAccount, isSalaryAccount, accountLimit);

         state.getAccountsTabController().loadAccounts();
      }
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


