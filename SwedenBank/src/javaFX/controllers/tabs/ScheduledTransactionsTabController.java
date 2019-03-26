package javaFX.controllers.tabs;

import datasource.SwedenBankDatasource;
import javaFX.State;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.TransactionView;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduledTransactionsTabController {

   @FXML
   TableView transactionTableView;

   @FXML
   TableColumn columnDescription;

   @FXML
   TableColumn columnDate;

   @FXML
   TableColumn columnAmount;

   @FXML
   TableColumn columnAccountName;

   @FXML
   TableColumn columnReceiver;

   @FXML
   Button showMore_showLessBtn;

   @FXML
   ContextMenu transactionContextMenu;

   private State state;

   @FXML
   private void initialize() {
      state = State.getInstance();
      state.setScheduledTransactionsTabController(this);
      setupTransactionTableView();
      renderTransactions();
   }

   private void setupTransactionTableView() {
      setupColumnDescription();
      setupColumnDate();
      setupColumnAmount();
      setupColumnAccountName();
      setupColumnReceiver();

      transactionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      setupContextMenu();
   }

   private void setupContextMenu() {
      transactionContextMenu = new ContextMenu();
      addCancelToContextMenu();

      transactionTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
         if (t.getButton() == MouseButton.SECONDARY) {
            transactionContextMenu.show(transactionTableView, t.getScreenX(), t.getScreenY());
         }
      });
   }

   private void addCancelToContextMenu() {
      MenuItem cancelMenuItem = new MenuItem("Cancel");
      cancelMenuItem.setOnAction(event -> {
         long id =
                 ((TransactionView) transactionTableView.getSelectionModel().getSelectedItem()).getId();
         new Thread(() -> {
            SwedenBankDatasource.getInstance().deleteScheduledTransaction(id);
            renderTransactions();
         }).start();
      });

      transactionContextMenu.getItems().add(cancelMenuItem);
   }

   private void setupColumnDescription() {
      columnDescription.setCellValueFactory(new PropertyValueFactory<TransactionView, String>("description"));

      columnDescription.setCellFactory(column -> {
         TableCell cell = new TableCell<TransactionView, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
               super.updateItem(item, empty);

               if (item == null || empty) {
                  setText(null);
                  setGraphic(null);
               } else {
                  Text text = new Text(item);
//                  text.setWrappingWidth(300);
                  setGraphic(text);
               }
            }
         };
         return cell;
      });
   }

   private void setupColumnDate() {
      columnDate.setCellValueFactory(new PropertyValueFactory<TransactionView, Date>("date"));


      columnDate.setCellFactory(column -> {
         TableCell cell = new TableCell<TransactionView, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
               super.updateItem(item, empty);

               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

               if (item == null || empty) {
                  setText(null);
               } else {
                  setText(formatter.format(item.toLocalDate()));
               }
            }
         };
         return cell;
      });
   }

   private void setupColumnAmount() {
      columnAmount.setCellValueFactory(new PropertyValueFactory<TransactionView, Double>("amount"));

      columnAmount.setCellFactory(column -> {
         TableCell cell = new TableCell<TransactionView, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
               super.updateItem(item, empty);

               String format = "%.2f";

               if (item == null || empty) {
                  setText(null);
               } else {
                  StringBuilder sb = new StringBuilder();
                  sb.append(String.format(format, item) + " (SEK)");
                  setText(sb.toString());
               }
            }
         };
         return cell;
      });
   }

   private void setupColumnAccountName() {
      columnAccountName.setCellValueFactory(new PropertyValueFactory<TransactionView, String>("accountName"));

   }

   private void setupColumnReceiver() {
      columnReceiver.setCellValueFactory(new PropertyValueFactory<TransactionView, String>("receiver"));
   }

   @FXML
   private void showMore_Show_LessBtnClicked() {
      showMore_showLessBtn.setDisable(true);
      String newText = showMore_showLessBtn.getText().equals("Show More") ? "Show Less" : "Show More";
      showMore_showLessBtn.setText(newText);
      renderTransactions();
      showMore_showLessBtn.setDisable(false);
   }

   public void renderTransactions() {
      ShowTransactions task = new ShowTransactions();
      transactionTableView.itemsProperty().bind(task.valueProperty());
      new Thread(task).start();
   }

   private class ShowTransactions extends Task {
      @Override
      protected Object call() throws Exception {
         if (showMore_showLessBtn.getText().equals("Show Less")) {
            List<TransactionView> transactions =
                    SwedenBankDatasource.getInstance()
                            .queryAllScheduledTransactions(state.getInstance()
                                    .getUser()
                                    .getPersonNr());
            return FXCollections.observableArrayList(transactions);
         } else {
            List<TransactionView> transactions =
                    SwedenBankDatasource.getInstance()
                            .queryTenScheduledTransactions(state.getInstance()
                                    .getUser()
                                    .getPersonNr());
            return FXCollections.observableArrayList(transactions);
         }
      }
   }

}
