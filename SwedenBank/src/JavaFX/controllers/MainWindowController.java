package JavaFX.controllers;

import JavaFX.State;
import datasource.SwedenBankDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.BankAccount;


import java.util.List;

public class MainWindowController {

   @FXML
   ListView accountListView;

   private SwedenBankDatasource swedenBankDatasource;
   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
      setUpAccountListView();
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
                     String text = item.getName() + "(" + item.getBalance() + " SEK)";
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
      accountListView.setItems(state.getAccounts());
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
}
