package javaFX.controllers.dialogs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.BankAccount;

import java.util.function.UnaryOperator;

public class NewTransactionController {

   @FXML
   private TextField amountField;

   @FXML
   private TextField accountNumberField;

   @FXML
   private ChoiceBox accountChoiceBox;

   @FXML
   private Label accountError;

   @FXML
   private Label moneyError;

   @FXML
   TextField descriptonField;

   ObservableList<BankAccount> accounts;

   @FXML
   private void initialize() {
      addFormatter();
      setupAccountChoiceBox();
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
      amountField.setTextFormatter(textFormatter);
      textFormatter = new TextFormatter<>(filter);
      accountNumberField.setTextFormatter(textFormatter);
   }

   private void setupAccountChoiceBox() {
      accounts = FXCollections.observableArrayList(State.getInstance().getAccounts().filtered(a -> a.getSavingAccount().equals("N")));
      accountChoiceBox.setItems(accounts);
      if (!accountChoiceBox.getSelectionModel().isEmpty()) {
         accountChoiceBox.getSelectionModel().selectFirst();
      }

      accountChoiceBox.setConverter(new StringConverter<BankAccount>() {
         @Override
         public String toString(BankAccount object) {
            String format = "%.2f";
            return object.getName() + " (" + String.format(format, object.getBalance()) + " SEK)";
         }

         @Override
         public BankAccount fromString(String string) {
            return new BankAccount().setName(string);
         }
      });

      accountChoiceBox.setPrefWidth(500);
   }

   public boolean validateNewTransaction() {
      accountError.setVisible(false);
      moneyError.setVisible(false);

      BankAccount account = (BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem();

      if (account == null) {
         return false;
      }

      if (amountField.getText().equals("")) {
         return false;
      }

      double amount = Double.parseDouble(amountField.getText());
      String sender = account.getAccountNumber();

      try {
         double balance = SwedenBankDatasource.getInstance().queryAccountBalance(sender);
         if (amount > balance) {
            moneyError.setText("Not enough money");
            moneyError.setVisible(true);
            return false;
         }
         if (!SwedenBankDatasource.getInstance().checkAccountLimit(sender, amount)) {
            System.out.println("number: " + sender + " amount: " + amount);
            moneyError.setText("Transaction above account limit");
            moneyError.setVisible(true);
            return false;
         }
      } catch (IllegalStateException e) {
         Platform.runLater(() -> accountError.setVisible(true));
         return false;
      } catch (Exception e) {
         e.printStackTrace();
      }

      return true;
   }

   public TextField getAmountField() {
      return amountField;
   }

   public TextField getAccountNumberField() {
      return accountNumberField;
   }

   public Label getAccountError() {
      return accountError;
   }

   public Label getMoneyError() {
      return moneyError;
   }

   public ChoiceBox getAccountChoiceBox() {
      return accountChoiceBox;
   }

   public TextField getDescriptonField() {
      return descriptonField;
   }
}
