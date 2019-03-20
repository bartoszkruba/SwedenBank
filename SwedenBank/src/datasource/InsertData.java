package datasource;

import models.Address;
import models.BankAccount;
import models.Transaction;
import models.User;

public class InsertData {

   public static void main(String[] args) {

      SwedenBankDatasource swedenBankDatasource = SwedenBankDatasource.getInstance();

      swedenBankDatasource.openConnection(DBNames.CONNECTION_ADDRESS, DBNames.LOGIN, DBNames.PASSWORD);

      swedenBankDatasource.dropTable(Transaction.class);
      swedenBankDatasource.dropTable(BankAccount.class);
      swedenBankDatasource.dropTable(User.class);
      swedenBankDatasource.dropTable(Address.class);

      swedenBankDatasource.createTable(Address.class);
      swedenBankDatasource.createTable(User.class);
      swedenBankDatasource.createTable(BankAccount.class);
      swedenBankDatasource.createTable(Transaction.class);

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

      BankAccount account = new BankAccount();

      account.setAccountNumber("11112222333344")
              .setName("My Account")
              .setPersonNumber("111122223333")
              .setSalaryAccount("Y");

      BankAccount anotherAccount = new BankAccount();
      anotherAccount.setAccountNumber("99997777888866")
              .setName("Saving Account")
              .setPersonNumber("111122223333")
              .setSavingAccount("Y")
              .setBalance(24390);

      swedenBankDatasource.insertIntoTable(address);
      swedenBankDatasource.insertIntoTable(user);
      swedenBankDatasource.insertIntoTable(account);
      swedenBankDatasource.insertIntoTable(anotherAccount);

      swedenBankDatasource.closeConnection();

   }

}
