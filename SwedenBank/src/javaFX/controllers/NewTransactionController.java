package javaFX.controllers;

import javaFX.State;
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
