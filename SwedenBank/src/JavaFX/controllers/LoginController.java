package JavaFX.controllers;

import JavaFX.Main;
import JavaFX.State;
import datasource.SwedenBankDatasource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import models.User;

import java.io.IOException;

public class LoginController {

   private SwedenBankDatasource swedenBankDatasource;

   @FXML
   private void initialize() {
      swedenBankDatasource = SwedenBankDatasource.getInstance();
   }

   @FXML
   Button loginButton;

   @FXML
   TextField personNumberField;

   @FXML
   PasswordField passwordField;

   @FXML
   Label personnummerErrorLabel;

   @FXML
   Label passwordErrorLabel;

   @FXML
   private void loginButtonPressed() {
      new Thread(() -> {
         passwordErrorLabel.setVisible(false);
         personnummerErrorLabel.setVisible(false);

         loginButton.setDisable(true);
         String personNumber = personNumberField.getText();
         String password = passwordField.getText();

         personNumber = personNumber.replaceAll("\\D", "");

         if (personNumber.length() != 12) {
            personnummerErrorLabel.setVisible(true);
         } else {
            User user = swedenBankDatasource.QueryUser(personNumber, password);
            if (user == null) {
               passwordErrorLabel.setVisible(true);
            } else {
               State.getInstance().setUser(user);
               switchWindow();
            }
         }
         loginButton.setDisable(false);
         Platform.runLater(() -> loginButton.requestFocus());
      }).start();
   }

   @FXML
   private void keyPressedOnField(KeyEvent e) {
      if (e.getCode().toString().equals("ENTER")) {
         loginButtonPressed();
      }
   }

   private void switchWindow() {
      Platform.runLater(() -> {
         try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/MainWindowView.fxml"));

            Main.primaryStage.setScene(new Scene(root, 1100, 700));
         } catch (IOException e) {
            e.printStackTrace();
         }
      });
   }
}