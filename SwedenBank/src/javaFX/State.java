package javaFX;

import javaFX.controllers.MainWindowController;
import javaFX.controllers.tabs.AccountsTabController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import models.BankAccount;
import models.Transaction;
import models.User;

import java.util.List;

public class State {
   private static State ourInstance = new State();

   public static State getInstance() {
      return ourInstance;
   }

   private User user;
   private ObservableList<BankAccount> accounts;
   private ObservableList<Transaction> transactions;
   private BankAccount currentAccount;

   private MainWindowController mainWindowController;
   private AccountsTabController accountsTabController;

   private State() {
      user = new User();
      accounts = FXCollections.observableArrayList();
      transactions = FXCollections.observableArrayList();
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public ObservableList<BankAccount> getAccounts() {
      return accounts;
   }

   public void setAccounts(List<BankAccount> accounts) {
      this.accounts.clear();
      this.accounts.addAll(accounts);
   }

   public ObservableList<Transaction> getTransactions() {
      return transactions;
   }

   public synchronized void setTransactions(List<Transaction> transactions) {
      this.transactions.clear();
      this.transactions.addAll(transactions);
   }

   public BankAccount getCurrentAccount() {
      return currentAccount;
   }

   public void setCurrentAccount(BankAccount currentAccount) {
      this.currentAccount = currentAccount;
   }

   public MainWindowController getMainWindowController() {
      return mainWindowController;
   }

   public void setMainWindowController(MainWindowController mainWindowController) {
      this.mainWindowController = mainWindowController;
   }

   public AccountsTabController getAccountsTabController() {
      return accountsTabController;
   }

   public void setAccountsTabController(AccountsTabController accountsTabController) {
      this.accountsTabController = accountsTabController;
   }
}
