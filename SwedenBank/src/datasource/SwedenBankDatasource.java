package datasource;

import models.Address;
import models.BankAccount;
import models.Transaction;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

   private final String QUERY_ACCOUNT_BALANCE = "SELECT " + DBNames.COLUMN_ACCOUNTS_BALANCE +
           " FROM " + DBNames.TABLE_ACCOUNTS;

   private final String CALL_PROCEDURE_TRANSFER_MONEY = "CALL " + DBNames.PROCEDURE_TRANSFER_MONEY + "(?, ?, ?, ?)";

   private PreparedStatement queryUser;
   private PreparedStatement queryAccountsForUser;
   private PreparedStatement queryTenTransactions;
   private PreparedStatement queryAccountBalance;

   private PreparedStatement callProcedureTransfer_money;

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
         queryAccountBalance = conn.prepareStatement(QUERY_ACCOUNT_BALANCE);

         callProcedureTransfer_money = conn.prepareStatement(CALL_PROCEDURE_TRANSFER_MONEY);
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
         closeStatement(queryAccountBalance);
         closeStatement(callProcedureTransfer_money);
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

   public double queryAccountBalance() throws Exception {
      ResultSet result = queryAccountBalance.executeQuery();

      if (result.isBeforeFirst()) {
         result.next();
         return accountObjectMapper.mapOne(result).getBalance();
      } else {
         throw new IllegalStateException("Account doesn't exist");
      }
   }

   public void createProcedureTransfer_money() {
      try {
         System.out.println(DBNames.CREATE_TRANSFER_MONEY_PROCEDURE);
         Statement statement = conn.createStatement();
         statement.executeUpdate(DBNames.CREATE_TRANSFER_MONEY_PROCEDURE);
      } catch (SQLException e) {
         System.out.println("Couldn't create procedure: " + e.getMessage());
      }
   }

   public void dropProcedureTransfer_money() {
      try {
         Statement statement = conn.createStatement();
         String sql = "DROP PROCEDURE IF EXISTS " + DBNames.PROCEDURE_TRANSFER_MONEY;
         statement.executeUpdate(sql);
      } catch (SQLException e) {
         System.out.println("Couldn't drop procedure: " + e.getMessage());
      }
   }

   public void callProcedureTransfer_money(String sender, String receiver, double amount, String description) {
      try {
         callProcedureTransfer_money.setString(1, sender);
         callProcedureTransfer_money.setString(2, receiver);
         callProcedureTransfer_money.setDouble(3, amount);
         callProcedureTransfer_money.setString(4, description);

         System.out.println(callProcedureTransfer_money.toString().replaceAll("^.*: ", ""));

         int affectedRows = callProcedureTransfer_money.executeUpdate();

         System.out.println("Affected rows: " + affectedRows);
      } catch (SQLException e) {
         System.out.println("Couldn't call procedure: " + e.getMessage());
      }
   }
}