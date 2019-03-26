package javaFX.controllers.dialogs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.BankAccount;
import models.ScheduledTransaction;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
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
   private Label dateError;

   @FXML
   private DatePicker datePicker;

   @FXML
   private TextField descriptonField;

   private ObservableList<BankAccount> accounts;

   private SwedenBankDatasource swedenBankDatasource;

   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();

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
      accounts = FXCollections.observableArrayList(state.getAccounts().filtered(a -> a.getSavingAccount().equals("N")));
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

   public boolean validateScheduledTransaction() {
      accountError.setVisible(false);
      moneyError.setVisible(false);
      dateError.setVisible(false);

      if (datePicker.getValue() == null) {
         dateError.setVisible(true);
      }

      if (amountField.getText().equals("")) {
         return false;
      }

      double amount = Double.parseDouble(amountField.getText());
      String receiver = accountNumberField.getText();

      try {
         double balance = swedenBankDatasource.queryAccountBalance(receiver);
         if (amount > balance) {
            Platform.runLater(() -> moneyError.setVisible(true));
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

   public ScheduledTransaction processResults() {
      Date date = Date.valueOf(datePicker.getValue());
      String description = descriptonField.getText();
      String sender = ((BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem()).getAccountNumber();
      String receiver = accountNumberField.getText();
      double amount = Double.parseDouble(amountField.getText());

      ScheduledTransaction transaction = new ScheduledTransaction();
      transaction.setDate(date)
              .setDescription(description)
              .setSenderAccountNumber(sender)
              .setReceiverAccountNumber(receiver)
              .setAmount(amount);

      return transaction;
   }

   public void processScheduledTransaction(Optional<ButtonType> result) {
      if (result.isPresent() && result.get() == ButtonType.OK) {
         ScheduledTransaction transaction = processResults();
         swedenBankDatasource.insertIntoTable(transaction);
         state.getScheduledTransactionsTabController().renderTransactions();
      }
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

   public DatePicker getDatePicker() {
      return datePicker;
   }
}
