package javaFX.controllers;

import datasource.SwedenBankDatasource;
import javaFX.Main;
import javaFX.State;
import javaFX.controllers.dialogs.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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

      controller.processNewTransaction(result);
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

      controller.processNewAccount(result);
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
         if (!controller.validateDeleteAccount()) {
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      controller.processDeleteAccount(result);
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

      controller.fillUpEditAccountDialog();

      btnOK.addEventFilter(ActionEvent.ACTION, event -> {
         if (!controller.validateAccount()) {
            event.consume();
         }
      });

      Optional<ButtonType> result = dialog.showAndWait();

      controller.processEditAccount(result);
   }


   @FXML
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

      controller.processScheduledTransaction(result);
   }
}
