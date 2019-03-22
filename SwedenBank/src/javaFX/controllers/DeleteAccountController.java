package javaFX.controllers;

import javaFX.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import models.BankAccount;

public class DeleteAccountController {

   @FXML
   ChoiceBox accountChoiceBox;

   ObservableList<BankAccount> accounts;

   @FXML
   private void initialize() {
      setupAccountChoiceBox();
   }

   private void setupAccountChoiceBox() {
      accounts = FXCollections.observableArrayList(State.getInstance().getAccounts().filtered(a -> a != State.getInstance().getCurrentAccount()));

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

}
