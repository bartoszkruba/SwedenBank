package javaFX.controllers.tabs;

import datasource.SwedenBankDatasource;
import javafx.application.Platform;
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

//      setupTransactionTableView();
      renderTransactions();
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
               street.setText(address.getStreetName() + " " + address.getPostCode());
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

   private void renderTransactions() {
      List<Transaction> transactions = swedenBankDatasource.queryTenTransactionsForUser(state.getUser().getPersonNr());
      transactions.forEach(t -> System.out.println(t.getDescription()));
   }

}
