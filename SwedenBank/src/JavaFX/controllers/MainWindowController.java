package JavaFX.controllers;

import datasource.SwedenBankDatasource;
import javafx.fxml.FXML;
import models.BankAccount;
import JavaFX.State;

import java.util.List;

public class MainWindowController {

   private SwedenBankDatasource swedenBankDatasource;
   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();
      loadAccounts();
   }

   private void loadAccounts() {
      List<BankAccount> accounts;

      // create thread
      accounts = swedenBankDatasource.queryAccountsForUser(state.getUser().getPersonNr());

      System.out.println(accounts.size());
   }
}
