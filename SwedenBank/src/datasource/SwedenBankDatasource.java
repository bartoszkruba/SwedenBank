package datasource;

import models.Address;
import models.BankAccount;
import models.Transaction;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SwedenBankDatasource extends Datasource {

   private static SwedenBankDatasource instance;

   private final String QUERY_USER = "SELECT * FROM " + DBNames.TABLE_USERS +
           " WHERE " + DBNames.COLUMN_USERS_PERSON_NR + "=? AND " +
           DBNames.COLUMN_USERS_PASSWORD + " = ?";

   private final String QUERY_ACCOUNTS_FOR_USER = "SELECT * FROM " + DBNames.TABLE_ACCOUNTS +
           " WHERE " + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = ?";

   private final String QUERY_TEN_TRANSACTIONS = "SELECT * FROM " + DBNames.TABLE_TRANSACTIONS +
           " WHERE " + DBNames.COLUMN_TRANSACTIONS_SENDER + " = ? OR " +
           DBNames.COLUMN_TRANSACTIONS_RECEIVER + " = ? " +
           "ORDER BY " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + " DESC LIMIT 10";

   private PreparedStatement queryUser;
   private PreparedStatement queryAccountsForUser;
   private PreparedStatement queryTenTransactions;

   private ObjectMapper<User> userObjectMapper;
   private ObjectMapper<Address> addressObjectMapper;
   private ObjectMapper<BankAccount> accountObjectMapper;
   private ObjectMapper<Transaction> transactionObjectMapper;

   public static SwedenBankDatasource getInstance() {
      if (instance == null) {
         instance = new SwedenBankDatasource();
      }
      return instance;
   }

   private SwedenBankDatasource() {
      userObjectMapper = new ObjectMapper<>(User.class);
      addressObjectMapper = new ObjectMapper<>(Address.class);
      accountObjectMapper = new ObjectMapper<>(BankAccount.class);
      transactionObjectMapper = new ObjectMapper<>(Transaction.class);
   }

   @Override
   public boolean openConnection(String connectionString, String login, String password) {
      super.openConnection(connectionString, login, password);
      try {
         queryUser = conn.prepareStatement(QUERY_USER);
         queryAccountsForUser = conn.prepareStatement(QUERY_ACCOUNTS_FOR_USER);
         queryTenTransactions = conn.prepareStatement(QUERY_TEN_TRANSACTIONS);
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't open connection: " + e.getMessage());
         return false;
      }
   }

   @Override
   public boolean closeConnection() {
      try {
         closeStatement(queryUser);
         closeStatement(queryAccountsForUser);
         closeStatement(queryTenTransactions);
         super.closeConnection();
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't close connection");
         return false;
      }
   }

   private void closeStatement(PreparedStatement statement) throws SQLException {
      if (statement != null) {
         statement.close();
      }
   }

   public User QueryUser(String personnummer, String password) {
      if (queryUser == null) {
         System.out.println("Connection is not open");
         return null;
      }

      try {
         queryUser.setString(1, personnummer);
         queryUser.setString(2, password);
         ResultSet results = queryUser.executeQuery();
         if (results.isBeforeFirst()) {
            results.next();
            return userObjectMapper.mapOne(results);
         } else {
            return null;
         }
      } catch (SQLException e) {
         System.out.println("Couldn't query user: " + e.getMessage());
         return null;
      }
   }

   public List<BankAccount> queryAccountsForUser(String personNummer) {
      try {
         queryAccountsForUser.setString(1, personNummer);
         ResultSet results = queryAccountsForUser.executeQuery();
         List<BankAccount> accounts = new ArrayList<>();
         accounts = accountObjectMapper.map(results);
         return accounts;
      } catch (SQLException e) {
         System.out.println("Couldn't query accounts: " + e.getMessage());
         return null;
      }
   }

   public List<Transaction> queryTenTransactions(String accountNumber) {
      if (queryTenTransactions == null) {
         System.out.println("Connections is not open");
         return null;
      }

      try {
         queryTenTransactions.setString(1, accountNumber);
         queryTenTransactions.setString(2, accountNumber);

         ResultSet results = queryTenTransactions.executeQuery();

         return transactionObjectMapper.map(results);

      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }
}