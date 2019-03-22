package javaFX;

import datasource.DBNames;
import datasource.SwedenBankDatasource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
   public static Stage primaryStage;

   @Override
   public void start(Stage primaryStage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("views/LoginView.fxml"));
      Main.primaryStage = primaryStage;
      primaryStage.setTitle("SwedenBank");
      primaryStage.setScene(new Scene(root, 450, 280));
      primaryStage.show();
   }

   public static void main(String[] args) {
      if (SwedenBankDatasource.getInstance()
              .openConnection(DBNames.CONNECTION_ADDRESS, DBNames.LOGIN, DBNames.PASSWORD)) {
         launch(args);
      }
   }
}
