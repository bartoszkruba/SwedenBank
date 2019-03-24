package javaFX.controllers;

import javaFX.Main;
import javaFX.State;
import datasource.SwedenBankDatasource;
import javaFX.controllers.dialogs.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.BankAccount;
import models.ScheduledTransaction;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController {

   @FXML
   BorderPane mainBorderPane;

   @FXML
   TabPane tabPane;

   @FXML
   Tab accountsTab;

   @FXML
   Tab overviewTab;

   @FXML
   Tab scheduledTransationsTab;

   private SwedenBankDatasource swedenBankDatasource;
   private State state;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
      state = State.getInstance();

      state.setMainWindowController(this);

      setupTabPane();
   }

   private void setupTabPane() {
      tabPane.prefWidthProperty().bind(Main.primaryStage.widthProperty());
      try {
         BorderPane overview = FXMLLoader.load(getClass().getResource("../views/tabs/OverviewTab.fxml"));
         overviewTab.setContent(overview);

         BorderPane accounts = FXMLLoader.load(getClass().getResource("../views/tabs/AccountsTab.fxml"));
         accountsTab.setContent(accounts);

         BorderPane transactions = FXMLLoader.load(getClass().getResource("../views/tabs/ScheduledTransactionsTab.fxml"));
         scheduledTransationsTab.setContent(transactions);
      } catch (IOException e) {
         System.out.println("Couldn't load view: " + e.getMessage());
      }
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
      fxmlLoader.setLocation(getClass().getResource("../views/dialogs/NewTransactionDialog.fxml"));
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
         if (!controller.validateNewTransaction()) {
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

            Platform.runLater(() -> state.getAccountsTabController().loadAccounts());
         }).start();
      }
   }

   @FXML
   private void newAccountSelected() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("New Account");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/dialogs/NewAccountDialog.fxml"));
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

      NewAccountController controller = fxmlLoader.getController();

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         if (!controller.validateNewAccount()) {
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         new Thread(() -> {
            BankAccount account = controller.processResults();
            swedenBankDatasource.insertIntoTable(account);
            Platform.runLater(() -> {
               state.getAccountsTabController().loadAccounts();
            });
         }).start();
      }

   }

   public void showDeleteAccountDialog() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("Delete Account");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/dialogs/DeleteAccountDialog.fxml"));
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

      DeleteAccountController controller = fxmlLoader.getController();


      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         BankAccount transferAccount = (BankAccount) controller.getAccountChoiceBox().getSelectionModel().getSelectedItem();
         if (transferAccount == null) {
            controller.getAccountError().setVisible(true);
            event.consume();
         }

      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         String description = controller.getDescriptionField().getText();
         boolean sqlResult =
                 swedenBankDatasource.transferMoneyAndDeleteAccount(
                         state.getCurrentAccount().getAccountNumber(),
                         ((BankAccount) controller.getAccountChoiceBox().getSelectionModel().getSelectedItem()).getAccountNumber(),
                         description);
         Alert alert;
         if (sqlResult) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Account Deleted");
            alert.setHeaderText("Account was deleted");
            alert.setContentText("Money transferred to " + ((BankAccount) controller.getAccountChoiceBox().getSelectionModel().getSelectedItem()).getAccountNumber());
         } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Account could not be deleted");
            alert.setContentText("Please try again");
         }
         alert.showAndWait();
         state.getAccountsTabController().loadAccounts();
      }
   }

   public void showEditAccountDialog() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("Edit Account");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/dialogs/EditAccountDialog.fxml"));
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

      EditAccountController controller = fxmlLoader.getController();

      controller.getAccountNameField().setText(state.getCurrentAccount().getName());

      String savingAccount = state.getCurrentAccount().getSavingAccount();

      String cardAccount = state.getCurrentAccount().getCardAccount();

      String salaryAccount = state.getCurrentAccount().getSalaryAccount();

      if (savingAccount.equals("Y")) {
         controller.getSavingAccountCheckBox().setSelected(true);
      } else {
         controller.getSavingAccountCheckBox().setSelected(false);
      }

      if (cardAccount.equals("Y")) {
         controller.getCardAccountCheckBox().setSelected(true);
      } else {
         controller.getCardAccountCheckBox().setSelected(false);
      }

      if (salaryAccount.equals("Y")) {
         controller.getSalaryAccountCheckBox().setSelected(true);
      } else {
         controller.getSalaryAccountCheckBox().setSelected(false);
      }

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         controller.getNameError().setVisible(false);
         controller.getConnectionError().setVisible(false);
         String personNr = state.getUser().getPersonNr();
         String name = controller.getAccountNameField().getText();
         try {
            BankAccount account = swedenBankDatasource.queryAccountOnName(personNr, name);
            if (account != null && !name.equals(state.getCurrentAccount().getName())) {
               controller.getNameError().setVisible(true);
               event.consume();
            }
         } catch (Exception e) {
            controller.getConnectionError().setVisible(true);
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         String newName = controller.getAccountNameField().getText();

         boolean isSavingAccount = controller.getSavingAccountCheckBox().isSelected();
         boolean isCardAccount = controller.getCardAccountCheckBox().isSelected();
         boolean isSalaryAccount = controller.getSalaryAccountCheckBox().isSelected();

         String accountNumber = state.getCurrentAccount().getAccountNumber();

         swedenBankDatasource.updateAccount(accountNumber, newName, isSavingAccount, isCardAccount, isSalaryAccount);

         state.getAccountsTabController().loadAccounts();
      }
   }

   @FXML
   private void newScheduledTransactionSelected() {
      showScheduledTransactionDialog();
   }

   private void showScheduledTransactionDialog() {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.initOwner(mainBorderPane.getScene().getWindow());
      dialog.setTitle("New Transaction");

      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("../views/dialogs/ScheduledTransactionDialog.fxml"));
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

      ScheduledTransactionController controller = fxmlLoader.getController();

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         if (!controller.validateScheduledTransaction()) {
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
         ScheduledTransaction transaction = controller.processResults();
         swedenBankDatasource.insertIntoTable(transaction);
      }
   }

}
