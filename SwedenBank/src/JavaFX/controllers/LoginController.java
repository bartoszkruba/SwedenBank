package JavaFX.controllers;

import datasource.SwedenBankDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;

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
               System.out.println("Process to login");
            }
         }
         loginButton.setDisable(false);
      }).start();
   }
}
