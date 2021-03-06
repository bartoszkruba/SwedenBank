package javaFX.controllers.dialogs;

import datasource.SwedenBankDatasource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.BankAccount;

import javaFX.State;
import util.AccountNumberGenerator;

import java.util.Optional;


public class NewAccountController {

   @FXML
   private Label connectionError;

   @FXML
   private TextField accountNameTextField;

   @FXML
   private Label accountNameError;

   @FXML
   private CheckBox savingAccountCheckBox;

   @FXML
   private CheckBox cardAccountCheckBox;

   @FXML
   private CheckBox salaryAccountCheckBox;

   private SwedenBankDatasource swedenBankDatasource;

   private State state;


   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
   }

   public BankAccount processResults() {
      BankAccount account = new BankAccount();
      account.setName(accountNameTextField.getText());

      if (savingAccountCheckBox.isSelected()) {
         account.setSavingAccount("Y");
      } else {
         account.setSavingAccount("N");
      }

      if (cardAccountCheckBox.isSelected()) {
         account.setCardAccount("Y");
      } else {
         account.setCardAccount("N");
      }

      if (salaryAccountCheckBox.isSelected()) {
         account.setSalaryAccount("Y");
      } else {
         account.setSalaryAccount("N");
      }

      account.setPersonNumber(state.getUser().getPersonNr())
              .setAccountNumber(AccountNumberGenerator.generateUniqueNumber(swedenBankDatasource));

      return account;
   }

   public boolean validateNewAccount() {
      accountNameError.setVisible(false);
      connectionError.setVisible(false);

      String name = accountNameTextField.getText();
      String personNumber = state.getUser().getPersonNr();

      if (name.equals("")) return false;

      try {
         BankAccount account = swedenBankDatasource.queryAccountOnName(personNumber, name);
         if (account != null) {
            accountNameError.setVisible(true);
            return false;
         }
         return true;
      } catch (Exception e) {
         connectionError.setVisible(true);
         return false;
      }
   }

   public void processNewAccount(Optional<ButtonType> result) {
      if (result.isPresent() && result.get() == ButtonType.OK) {
         new Thread(() -> {
            BankAccount account = processResults();
            swedenBankDatasource.insertIntoTable(account);
            Platform.runLater(() -> {
               state.getAccountsTabController().loadAccounts();
            });
         }).start();
      }
   }

   @FXML
   private void savingAccountSelected() {
      if (savingAccountCheckBox.isSelected()) {
         cardAccountCheckBox.setSelected(false);
      }
   }

   @FXML
   private void cardAccountSelected() {
      if (cardAccountCheckBox.isSelected()) {
         savingAccountCheckBox.setSelected(false);
      }
   }

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
