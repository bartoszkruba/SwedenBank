package javaFX.controllers.tabs;

import datasource.SwedenBankDatasource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.Address;
import models.Transaction;
import models.User;
import javaFX.State;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OverViewTabController {

   @FXML
   private TableView transactionTableView;

   @FXML
   private TableColumn columnDate;

   @FXML
   private TableColumn columnDescription;

   @FXML
   private TableColumn columnAmount;


   @FXML
   private Label firstName;

   @FXML
   private Label lastName;

   @FXML
   private Label personnummer;

   @FXML
   private Label street;

   @FXML
   private Label city;

   @FXML
   private Label postCode;

   @FXML
   private Label country;

   private State state;
   private SwedenBankDatasource swedenBankDatasource;

   @FXML
   private void initialize() {
      state = State.getInstance();
      swedenBankDatasource = SwedenBankDatasource.getInstance();

      state.setOverViewTabController(this);

      setupTransactionTableView();
      renderUserData();
      renderAddress();
   }

   private void renderUserData() {
      User user = state.getUser();

      firstName.setText(user.getFirstName());
      lastName.setText(user.getLastName());
      personnummer.setText(user.getPersonNr());
   }

   private void renderAddress() {
      new Thread(() -> {
         Address address = swedenBankDatasource.queryAddress(state.getUser().getAddressId());
         if (address != null) {
            Platform.runLater(() -> {
               street.setText(address.getStreetName() + " " + address.getStreetNumber());
               city.setText(address.getCity());
               postCode.setText(address.getPostCode());
               country.setText(address.getCountry());
            });
         }
      }).start();
   }

   private void setupTransactionTableView() {

      setupColumnDescription();
      setupColumnDate();
      setupColumnAmount();

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

   public void renderTransactions() {
      ShowTransactions task = new ShowTransactions();

      transactionTableView.itemsProperty().bind(task.valueProperty());

      new Thread(task).start();

   }

   private class ShowTransactions extends Task {
      @Override
      protected Object call() throws Exception {
         List<Transaction> transactions = swedenBankDatasource.queryTenTransactionsForUser(state.getUser().getPersonNr());

         ArrayList<String> accountNumbers = new ArrayList<>();

         state.getAccounts().forEach(a -> {
            accountNumbers.add(a.getAccountNumber());
         });

         for (Transaction t : transactions) {
            for (String number : accountNumbers) {
               if (number.equals(t.getSenderAccountNumber())) {
                  t.setAmount(t.getAmount() * -1);
                  break;
               }
            }
         }

         return FXCollections.observableArrayList(transactions);
      }
   }

}
