package javaFX.controllers.dialogs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.BankAccount;

import java.util.Optional;

public class DeleteAccountController {

   @FXML
   private ChoiceBox accountChoiceBox;

   @FXML
   private Label accountError;

   @FXML
   TextField descriptionField;

   private ObservableList<BankAccount> accounts;

   private SwedenBankDatasource swedenBankDatasource;

   private State state;

   @FXML
   private void initialize() {
      setupAccountChoiceBox();
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
   }


   private void setupAccountChoiceBox() {
      accounts = FXCollections.observableArrayList(state.getAccounts().filtered(a -> a != state.getCurrentAccount()));

      accountChoiceBox.setItems(accounts);
      if (!accountChoiceBox.getSelectionModel().isEmpty()) {
         accountChoiceBox.getSelectionModel().selectFirst();
      }

      accountChoiceBox.setConverter(new StringConverter<BankAccount>() {
         @Override
         public String toString(BankAccount object) {
            String format = "%.2f\n";
            return object.getName() + " (" + String.format(format, object.getBalance()) + " SEK)";
         }

         @Override
         public BankAccount fromString(String string) {
            return new BankAccount().setName(string);
         }
      });

      accountChoiceBox.setPrefWidth(500);
   }

   public boolean validateDeleteAccount() {
      BankAccount transferAccount = (BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem();
      if (transferAccount == null) {
         accountError.setVisible(true);
         return false;
      }
      return true;
   }

   public void processDeleteAccount(Optional<ButtonType> result) {
      if (result.isPresent() && result.get() == ButtonType.OK) {
         String description = descriptionField.getText();
         boolean sqlResult =
                 swedenBankDatasource.transferMoneyAndDeleteAccount(
                         state.getCurrentAccount().getAccountNumber(),
                         ((BankAccount) accountChoiceBox
                                 .getSelectionModel().getSelectedItem()).getAccountNumber(),
                         description);
         Alert alert;
         if (sqlResult) {
            state.getScheduledTransactionsTabController().renderTransactions();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Account Deleted");
            alert.setHeaderText("Account was deleted");
            alert.setContentText("Money transferred to " +
                    ((BankAccount) accountChoiceBox
                            .getSelectionModel().getSelectedItem()).getAccountNumber());
         } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Account could not be deleted");
            alert.setContentText("Please try again");
         }
         alert.showAndWait();
         state.getAccountsTabController().loadAccounts();
      }
   }

   public ChoiceBox getAccountChoiceBox() {
      return accountChoiceBox;
   }

   public Label getAccountError() {
      return accountError;
   }

   public TextField getDescriptionField() {
      return descriptionField;
   }
}
