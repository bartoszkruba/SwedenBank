package javaFX.controllers.dialogs;

import javaFX.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.BankAccount;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.UnaryOperator;

public class ScheduledTransactionController {

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
   DatePicker datePicker;

   @FXML
   TextField descriptonField;

   ObservableList<BankAccount> accounts;

   @FXML
   private void initialize() {
      addFormatter();
      setupAccountChoiceBox();
      setupDatePicker();
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

   private void setupDatePicker() {
      DatePicker minDate = new DatePicker();
      minDate.setValue(LocalDate.now().plusDays(1));

      final Callback<DatePicker, DateCell> dayCellFactory;

      dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
         @Override
         public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (item.isBefore(minDate.getValue())) {
               setDisable(true);
               setStyle("-fx-background-color: #ffc0cb;");
            }
         }
      };
      datePicker.setDayCellFactory(dayCellFactory);
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
