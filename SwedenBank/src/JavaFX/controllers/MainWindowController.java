package JavaFX.controllers;

import JavaFX.State;
import datasource.SwedenBankDatasource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.BankAccount;
import models.Transaction;

import java.io.IOException;
import java.sql.Timestamp;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MainWindowController {

   @FXML
   ListView accountListView;

   @FXML
   TableView transactionTableView;

   @FXML
   TableColumn columnDate;

   @FXML
   TableColumn columnDescription;

   @FXML
   Button showMore_showLessBtn;

   @FXML
   BorderPane mainBorderPane;

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
      accountListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
            showMore_showLessBtn.setVisible(true);
            showMore_showLessBtn.setText("Show More");
            loadTransactions((BankAccount) newValue);
         } else {
            state.getTransactions().clear();
            showMore_showLessBtn.setVisible(false);
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
                  setText(formatter.format(item.toLocalDateTime()));
               }
            }
         };
         return cell;
      });

      transactionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

//      Task<ObservableList<Transaction>> task = new Task<ObservableList<Transaction>>() {
//         @Override
//         protected ObservableList<Transaction> call() throws Exception {
//            return FXCollections.observableArrayList(state.getTransactions());
//         }
//      };

      ShowTransactions task = new ShowTransactions();

      transactionTableView.itemsProperty().bind(task.valueProperty());

      new Thread(task).start();
   }

   @FXML
   private void showMore_Show_LessBtnClicked() {
      showMore_showLessBtn.setDisable(true);
      String newText = showMore_showLessBtn.getText().equals("Show More") ? "Show Less" : "Show More";
      loadTransactions((BankAccount) accountListView.getSelectionModel().getSelectedItem());
      showMore_showLessBtn.setText(newText);
      showMore_showLessBtn.setDisable(false);
   }

   @FXML
   private void newTransactionSelected() {
      showNewTransactionDialog();
   }

   private void showNewTransactionDialog() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("New Transaction");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/NewTransactionDialog.fxml"));
      try {
         dialog.getDialogPane().setContent(fxmlLoader.load());
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Couldn't load the dialog: " + e.getMessage());
         return;
      }

      dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
      dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

      Button btnOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

      NewTransactionController controller = fxmlLoader.getController();

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         if (validateNewTransaction(controller)) {
         } else {
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         String receiver = controller.getAccountNumberField().getText();
         String sender =
                 ((BankAccount) controller.getAccountChoiceBox().getSelectionModel().getSelectedItem()).getAccountNumber();
         double amount = Double.parseDouble(controller.getAmountField().getText());
         String description = controller.getDescriptonField().getText();

         new Thread(() -> {
            swedenBankDatasource.callProcedureTransfer_money(sender, receiver, amount, description);
            Platform.runLater(() -> loadAccounts());
         }).start();
      }
   }

   private boolean validateNewTransaction(NewTransactionController controller) {
      controller.getAccountError().setVisible(false);
      controller.getMoneyError().setVisible(false);

      var returnValue = new Object() {
         boolean returnValue;
      };

      returnValue.returnValue = true;

      double amount = Double.parseDouble(controller.getAmountField().getText());
      String receiver = controller.getAccountNumberField().getText();

      new Thread(() -> {
         try {
            double balance = swedenBankDatasource.queryAccountBalance(receiver);
            if (amount > balance) {
               Platform.runLater(() -> controller.getMoneyError().setVisible(true));
               System.out.println("not enough money");
               returnValue.returnValue = false;
            }
         } catch (IllegalStateException e) {
            Platform.runLater(() -> controller.getAccountError().setVisible(true));
            returnValue.returnValue = false;
         } catch (Exception e) {
            e.printStackTrace();
         }
      }).start();

      return returnValue.returnValue;
   }

   @FXML
   private void newAccountSelected() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("New Account");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/NewAccountDialog.fxml"));
      try {
         dialog.getDialogPane().setContent(fxmlLoader.load());
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Couldn't load the dialog: " + e.getMessage());
         return;
      }

      dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
      dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

      Button btnOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

//      NewTransactionController controller = fxmlLoader.getController();

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {

      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         System.out.println("Ok button pressed");
      }

   }

   class ShowTransactions extends Task {
      @Override
      protected Object call() throws Exception {
         return FXCollections.observableArrayList(state.getTransactions());
      }
   }
}
