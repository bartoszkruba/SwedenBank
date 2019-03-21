package JavaFX.controllers;

import JavaFX.State;
import datasource.SwedenBankDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.BankAccount;
import models.Transaction;

import java.sql.Timestamp;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainWindowController {

   @FXML
   ListView accountListView;

   @FXML
   TableView transactionTableView;

   @FXML
   TableColumn columnDate;

   @FXML
   TableColumn columnDescription;

   private SwedenBankDatasource swedenBankDatasource;
   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
      setUpAccountListView();
      setupTransactionTableView();
      loadAccounts();
   }

   private void setUpAccountListView() {
      accountListView.setCellFactory(new Callback<ListView, ListCell>() {
         @Override
         public ListCell call(ListView param) {
            ListCell<BankAccount> cell = new ListCell<>() {
               @Override
               protected void updateItem(BankAccount item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty) {
                     setText(null);
                  } else {
                     String text = item.getName() + " (" + item.getBalance() + " SEK)";
                     if (item.getSavingAccount().equals("Y")) {
                        text = text + " \uD83C\uDFE6";
                     }
                     if (item.getSalaryAccount().equals("Y")) {
                        text = text + " \uD83D\uDCB8";
                     }
                     setText(text);
                  }
               }
            };
            return cell;
         }
      });

      accountListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null) {
            loadTransactions((BankAccount) newValue);
         } else {
            state.getTransactions().clear();
         }
      });

      accountListView.setItems(state.getAccounts());
   }

   private void setupTransactionTableView() {

      columnDescription.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));

      columnDescription.setCellFactory(column -> {
         TableCell cell = new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
               super.updateItem(item, empty);

               if (item == null || empty) {
                  setText(null);
                  setGraphic(null);
               } else {
                  Text text = new Text(item);
                  text.setWrappingWidth(300);
                  setGraphic(text);
               }
            }
         };

         return cell;
      });

      columnDate.setCellValueFactory(new PropertyValueFactory<Transaction, Timestamp>("timestamp"));


      columnDate.setCellFactory(column -> {
         TableCell cell = new TableCell<Transaction, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
               super.updateItem(item, empty);

               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm");

               if (item == null || empty) {
                  setText(null);
               } else {
                  // Format date.
                  setText(formatter.format(item.toLocalDateTime()));
               }
            }
         };
         return cell;
      });

      transactionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      transactionTableView.setItems(state.getTransactions());
   }


   private void loadAccounts() {
      new Thread(() -> {
         List<BankAccount> accounts;
         accounts = swedenBankDatasource.queryAccountsForUser(state.getUser().getPersonNr());
         state.setAccounts(accounts);
         if (state.getAccounts().size() != 0) {
            accountListView.getSelectionModel().selectFirst();
         }
      }).start();
   }

   private void loadTransactions(BankAccount account) {
      String currentAccountNumber = account.getAccountNumber();

      List<Transaction> transactions =
              swedenBankDatasource.queryTenTransactions(currentAccountNumber);

      System.out.println(transactions.size());

      double accountSaldo = account.getBalance();

      for (Transaction t : transactions) {
         t.setSaldo(accountSaldo);
         if (t.getSenderAccountNumber().equals(currentAccountNumber)) {
            t.setAmount(t.getAmount() * -1);
            accountSaldo += t.getAmount() * -1;
         }
      }
      state.setTransactions(transactions);
   }
}
