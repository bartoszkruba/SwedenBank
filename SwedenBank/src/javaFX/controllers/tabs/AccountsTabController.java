package javaFX.controllers.tabs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javaFX.controllers.MainWindowController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
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

public class AccountsTabController {
   @FXML
   ListView accountListView;

   @FXML
   TableView transactionTableView;

   @FXML
   TableColumn columnDate;

   @FXML
   TableColumn columnDescription;

   @FXML
   TableColumn columnAmount;

   @FXML
   TableColumn columnSaldo;

   @FXML
   Button showMore_showLessBtn;

   @FXML
   ContextMenu accountContextMenu;

   @FXML
   Label accountNumber;

   @FXML
   Label accountBalance;

   @FXML
   Label savingsAccount;

   @FXML
   Label cardAccount;

   @FXML
   Label salaryAccount;
   private SwedenBankDatasource swedenBankDatasource;
   private State state;

   MainWindowController mainWindowController;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();

      accountContextMenu = new ContextMenu();
      mainWindowController = state.getMainWindowController();
      state.setAccountsTabController(this);

      setUpAccountListView();
      setupTransactionTableView();
      loadAccounts();
   }

   private void setUpAccountListView() {
      accountListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

      addDeleteToContextMenu();
      addEditContextMenu();

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
                     String format = "%.2f";
                     String text = item.getName() + " ("
                             + String.format(format, item.getBalance()) +
                             " SEK)";
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

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
               if (isNowEmpty) {
                  cell.setContextMenu(null);
               } else {
                  cell.setContextMenu(accountContextMenu);
               }
            });
            return cell;
         }
      });

      accountListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null) {
            showMore_showLessBtn.setVisible(true);
            showMore_showLessBtn.setText("Show More");
            loadTransactions((BankAccount) newValue);
            state.setCurrentAccount((BankAccount) newValue);
            renderAccountDetails();
         } else {
            state.getTransactions().clear();
            state.setCurrentAccount(null);
            showMore_showLessBtn.setVisible(false);
         }
      });

      accountListView.setItems(state.getAccounts());
   }


   private void addDeleteToContextMenu() {
      MenuItem deleteMenuItem = new MenuItem("Delete Account");
      deleteMenuItem.setOnAction(event -> {
         BankAccount account = ((BankAccount) accountListView.getSelectionModel().getSelectedItem());
         deleteAccount(account);
      });

      accountContextMenu.getItems().add(deleteMenuItem);
   }

   private void addEditContextMenu() {
      MenuItem editMenuItem = new MenuItem("Edit Account");
      editMenuItem.setOnAction(event -> {
         editAccount();
      });

      accountContextMenu.getItems().add(editMenuItem);
   }

   private void renderAccountDetails() {
      BankAccount currentAcc = state.getCurrentAccount();

      String format = "%.2f";
      String balance = String.format(format, currentAcc.getBalance()) + " (SEK)";

      accountNumber.setText(currentAcc.getAccountNumber());
      accountBalance.setText(balance);
      savingsAccount.setText(currentAcc.getSavingAccount().equals("Y") ? "Yes" : "No");
      cardAccount.setText(currentAcc.getCardAccount().equals("Y") ? "Yes" : "No");
      salaryAccount.setText(currentAcc.getSalaryAccount().equals("Y") ? "Yes" : "No");
   }

   private void deleteAccount(BankAccount account) {
      if (accountListView.getItems().size() == 1) {
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setTitle("There's a problem");
         alert.setHeaderText("Unable to delete account");
         alert.setContentText("\"" + account.getName() + "\" is your only account");
         alert.showAndWait();
      } else {
         mainWindowController.showDeleteAccountDialog();
      }
   }

   private void editAccount() {
      mainWindowController.showEditAccountDialog();
   }

   private void setupTransactionTableView() {

      setupColumnDescription();
      setupColumnDate();
      setupColumnAmount();
      setupColumnSaldo();

      transactionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
   }

   private void setupColumnDescription() {
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
   }

   private void setupColumnDate() {
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
                  setText(formatter.format(item.toLocalDateTime()));
               }
            }
         };
         return cell;
      });
   }

   private void setupColumnAmount() {
      columnAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

      columnAmount.setCellFactory(column -> {
         TableCell cell = new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
               super.updateItem(item, empty);

               String format = "%.2f";

               if (item == null || empty) {
                  setText(null);
               } else {
                  StringBuilder sb = new StringBuilder();
                  if (item > 0) {
                     sb.append("+");
                  }
                  sb.append(String.format(format, item) + " (SEK)");
                  setText(sb.toString());
               }
            }
         };
         return cell;
      });
   }

   private void setupColumnSaldo() {
      columnSaldo.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("saldo"));

      columnSaldo.setCellFactory(column -> {
         TableCell cell = new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
               super.updateItem(item, empty);

               String format = "%.2f";

               if (item == null || empty) {
                  setText(null);
               } else {
                  setText(String.format(format, item) + " (SEK)");
               }
            }
         };
         return cell;
      });
   }

   public void loadAccounts() {
      new Thread(() -> {
         List<BankAccount> accounts;
         accounts = swedenBankDatasource.queryAccountsForUser(state.getUser().getPersonNr());
         Platform.runLater(() -> {
            state.setAccounts(accounts);
            if (state.getAccounts().size() != 0) {
               accountListView.getSelectionModel().selectFirst();
            }
            state.getOverViewTabController().renderTransactions();
         });
      }).start();
   }

   public void loadTransactions(BankAccount account) {

      String currentAccountNumber = account.getAccountNumber();

      List<Transaction> transactions;

      if (showMore_showLessBtn.getText().equals("Show Less")) {
         transactions = swedenBankDatasource.queryAllTransactions(currentAccountNumber);
      } else {
         transactions = swedenBankDatasource.queryTenTransactions(currentAccountNumber);
      }

      double accountSaldo = account.getBalance();

      if (transactions != null) {
         for (Transaction t : transactions) {
            t.setSaldo(accountSaldo);
            if (t.getSenderAccountNumber().equals(currentAccountNumber)) {
               t.setAmount(t.getAmount() * -1);
            } else {
               t.setAmount(t.getAmount());
            }
            accountSaldo += t.getAmount() * -1;
         }
      }

      state.setTransactions(transactions);

      ShowTransactions task = new ShowTransactions();

      transactionTableView.itemsProperty().bind(task.valueProperty());

      new Thread(task).start();
   }

   @FXML
   private void showMore_Show_LessBtnClicked() {
      showMore_showLessBtn.setDisable(true);
      String newText = showMore_showLessBtn.getText().equals("Show More") ? "Show Less" : "Show More";
      showMore_showLessBtn.setText(newText);
      loadTransactions((BankAccount) accountListView.getSelectionModel().getSelectedItem());
      showMore_showLessBtn.setDisable(false);
   }


   class ShowTransactions extends Task {
      @Override
      protected Object call() throws Exception {
         return FXCollections.observableArrayList(state.getTransactions());
      }
   }
}
