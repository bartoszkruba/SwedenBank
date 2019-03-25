package datasource;

import datasource.sql.DBNames;
import models.*;

public class InsertData {

   public static void main(String[] args) {

      SwedenBankDatasource swedenBankDatasource = SwedenBankDatasource.getInstance();

      if (!(swedenBankDatasource.dropDatabase(DBNames.LOGIN, DBNames.PASSWORD, DBNames.DB_NAME)
              && swedenBankDatasource.createDatabase(DBNames.LOGIN, DBNames.PASSWORD, DBNames.DB_NAME))) {
         return;
      }

      swedenBankDatasource.openConnection(DBNames.CONNECTION_ADDRESS, DBNames.LOGIN, DBNames.PASSWORD);

      swedenBankDatasource.dropProcedureTransfer_money();
      swedenBankDatasource.dropScheduledTransactionsEvent();

      swedenBankDatasource.dropTable(ScheduledTransaction.class);
      swedenBankDatasource.dropTable(Transaction.class);
      swedenBankDatasource.dropTable(BankAccount.class);
      swedenBankDatasource.dropTable(User.class);
      swedenBankDatasource.dropTable(Address.class);

      swedenBankDatasource.turnOnScheduledEvents();

      swedenBankDatasource.dropScheduledTransactionsEvent();

      swedenBankDatasource.createScheduledTransactionsEvent();

      swedenBankDatasource.createProcedureTransfer_money();

      swedenBankDatasource.createTable(Address.class);
      swedenBankDatasource.createTable(User.class);
      swedenBankDatasource.createTable(BankAccount.class);
      swedenBankDatasource.createTable(Transaction.class);
      swedenBankDatasource.createTable(ScheduledTransaction.class);

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
              .setSalaryAccount("Y")
              .setCardAccount("Y");

      swedenBankDatasource.insertIntoTable(account);

      account = new BankAccount();
      account.setAccountNumber("99997777888866")
              .setName("Saving Account")
              .setPersonNumber("111122223333")
              .setSavingAccount("Y");
      swedenBankDatasource.insertIntoTable(account);

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("55554444333322", "11112222333344",
              25000.0, "Lön");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              143.50, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              15, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              243, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              87, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              192, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              13, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              432, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346789098765",
              52, "ICA Maxi");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346542398765",
              130.00, "Frisör");

      sleep();

      System.out.println();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "12346765438765",
              10000.00, "Godis");

      System.out.println();

      sleep();

      swedenBankDatasource.callProcedureTransfer_money("11112222333344", "99997777888866",
              2000.0, "Transferred from \"My Account\" to \"Saving Account\"");

      swedenBankDatasource.closeConnection();
   }

   private static void sleep() {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
