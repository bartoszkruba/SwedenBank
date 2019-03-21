package datasource;

import models.Address;
import models.BankAccount;
import models.Transaction;
import models.User;

public class InsertData {

   public static void main(String[] args) {

      SwedenBankDatasource swedenBankDatasource = SwedenBankDatasource.getInstance();

      swedenBankDatasource.openConnection(DBNames.CONNECTION_ADDRESS, DBNames.LOGIN, DBNames.PASSWORD);

      swedenBankDatasource.dropProcedureTransfer_money();

      swedenBankDatasource.dropTable(Transaction.class);
      swedenBankDatasource.dropTable(BankAccount.class);
      swedenBankDatasource.dropTable(User.class);
      swedenBankDatasource.dropTable(Address.class);

      swedenBankDatasource.createProcedureTransfer_money();

      swedenBankDatasource.createTable(Address.class);
      swedenBankDatasource.createTable(User.class);
      swedenBankDatasource.createTable(BankAccount.class);
      swedenBankDatasource.createTable(Transaction.class);

      System.out.println();

      Address address = new Address();
      address.setStreetName("Storgatan")
              .setStreetNumber("12")
              .setPostCode("123 31")
              .setCity("Malmö")
              .setCountry("Sweden");

      User user = new User();
      user.setPersonNr("111122223333")
              .setFirstName("John")
              .setLastName("Doe")
              .setPassword("password1234")
              .setAddressId(1);

      swedenBankDatasource.insertIntoTable(address);
      swedenBankDatasource.insertIntoTable(user);


      BankAccount account = new BankAccount();

      account.setAccountNumber("11112222333344")
              .setName("My Account")
              .setPersonNumber("111122223333")
              .setSalaryAccount("Y");

      swedenBankDatasource.insertIntoTable(account);

      account = new BankAccount();
      account.setAccountNumber("99997777888866")
              .setName("Saving Account")
              .setPersonNumber("111122223333")
              .setSavingAccount("Y");
      swedenBankDatasource.insertIntoTable(account);

      System.out.println();
//
//      transaction.setSenderAccountNumber("55554444333322")
//              .setReceiverAccountNumber("11112222333344")
//              .setDescription("Lön")
//              .setAmount(25000.0);
//      swedenBankDatasource.insertIntoTable(transaction);

      swedenBankDatasource.callProcedureTransfer_money("55554444333322", "11112222333344",
              25000.0, "Lön");

      try {
         Thread.sleep(5000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

//      transaction.setSenderAccountNumber("11112222333344")
//              .setReceiverAccountNumber("12346789098765")
//              .setDescription("ICA Maxi")
//              .setAmount(143.50);
//      swedenBankDatasource.insertIntoTable(transaction);

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              143.50, "ICA Maxi");

      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

//      transaction.setSenderAccountNumber("11112222333344")
//              .setReceiverAccountNumber("99997777888866")
//              .setDescription("Transferred from \"My Account\" to \"Saving Account\"")
//              .setAmount(2000.0);
//      swedenBankDatasource.insertIntoTable(transaction);

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "99997777888866",
              2000.0, "Transferred from \"My Account\" to \"Saving Account\"");

      swedenBankDatasource.closeConnection();

   }

}
