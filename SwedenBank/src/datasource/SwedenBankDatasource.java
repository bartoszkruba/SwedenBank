package datasource;

import datasource.sql.DBNames;
import datasource.sql.SQLCode;
import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SwedenBankDatasource extends Datasource {

   private static SwedenBankDatasource instance;

   private PreparedStatement queryUser;
   private PreparedStatement queryAccountsForUser;
   private PreparedStatement queryTenTransactions;
   private PreparedStatement queryAllTransactions;
   private PreparedStatement queryAccountBalance;
   private PreparedStatement queryAccountOnName;
   private PreparedStatement queryAddress;
   private PreparedStatement queryTenTransactionsForUser;
   private PreparedStatement queryTenScheduledTransactions;
   private PreparedStatement queryAllScheduledTransactions;

   private PreparedStatement deleteAccount;
   private PreparedStatement removeFuruteScheduledTransactions;

   private PreparedStatement updateAccount;

   private PreparedStatement callProcedureTransfer_money;

   private ObjectMapper<User> userObjectMapper;
   private ObjectMapper<Address> addressObjectMapper;
   private ObjectMapper<BankAccount> accountObjectMapper;
   private ObjectMapper<Transaction> transactionObjectMapper;
   private ObjectMapper<TransactionView> transactionViewObjectMapper;

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
      transactionViewObjectMapper = new ObjectMapper<>(TransactionView.class);
   }

   @Override
   public boolean openConnection(String connectionString, String login, String password) {
      super.openConnection(connectionString, login, password);
      try {
         queryUser = conn.prepareStatement(SQLCode.QUERY_USER);
         queryAccountsForUser = conn.prepareStatement(SQLCode.QUERY_ACCOUNTS_FOR_USER);
         queryTenTransactions = conn.prepareStatement(SQLCode.QUERY_TEN_TRANSACTIONS);
         queryAccountBalance = conn.prepareStatement(SQLCode.QUERY_ACCOUNT_BALANCE);
         queryAllTransactions = conn.prepareStatement(SQLCode.QUERY_ALL_TRANSACTIONS);
         queryAccountOnName = conn.prepareStatement(SQLCode.QUERY_ACCOUNT_ON_NAME);
         queryAddress = conn.prepareStatement(SQLCode.QUERY_ADDRESS);
         queryTenTransactionsForUser = conn.prepareStatement(SQLCode.QUERY_TEN_TRANSACTIONS_FOR_USER);
         queryTenScheduledTransactions = conn.prepareStatement(SQLCode.QUERY_TEN_SCHEDULED_TRANS);
         queryAllScheduledTransactions = conn.prepareStatement(SQLCode.QUERY_ALL_SCHEDULED_TRANS);


         deleteAccount = conn.prepareStatement(SQLCode.DELETE_ACCOUNT);
         removeFuruteScheduledTransactions = conn.prepareStatement(SQLCode.REMOVE_FUTURE_SCHEDULED_TRANSACTIONS);

         updateAccount = conn.prepareStatement(SQLCode.UPDATE_ACCOUNT);

         callProcedureTransfer_money = conn.prepareStatement(SQLCode.CALL_PROCEDURE_TRANSFER_MONEY);

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
         closeStatement(queryAllTransactions);
         closeStatement(queryAccountOnName);
         closeStatement(deleteAccount);
         closeStatement(updateAccount);
         closeStatement(queryAddress);
         closeStatement(queryTenTransactionsForUser);
         closeStatement(removeFuruteScheduledTransactions);
         closeStatement(queryTenScheduledTransactions);
         closeStatement(queryAllScheduledTransactions);

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
      try {
         return queryTransactions(queryTenTransactions, accountNumber);
      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }

   public List<Transaction> queryAllTransactions(String accountNumber) {
      try {
         return queryTransactions(queryAllTransactions, accountNumber);
      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }

   private synchronized List<Transaction> queryTransactions
           (PreparedStatement statement, String accountNumber) throws SQLException {
      if (statement == null) {
         System.out.println("Connections is not open");
         return null;
      }
      statement.setString(1, accountNumber);
      statement.setString(2, accountNumber);

      ResultSet results = statement.executeQuery();

      return transactionObjectMapper.map(results);
   }

   public double queryAccountBalance(String accountNumber) throws Exception {
      queryAccountBalance.setString(1, accountNumber);
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
         System.out.println(SQLCode.CREATE_TRANSFER_MONEY_PROCEDURE);
         Statement statement = conn.createStatement();
         statement.executeUpdate(SQLCode.CREATE_TRANSFER_MONEY_PROCEDURE);
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

   public void createScheduledTransactionsEvent() {
      try {
         System.out.println(SQLCode.CREATE_SCHEDULED_TRANSACTIONS_EVENT);
         Statement statement = conn.createStatement();
         statement.executeUpdate(SQLCode.CREATE_SCHEDULED_TRANSACTIONS_EVENT);
      } catch (SQLException e) {
         System.out.println("Couldn't create event: " + e.getMessage());
      }
   }

   public void dropScheduledTransactionsEvent() {
      try {
         Statement statement = conn.createStatement();
         statement.executeUpdate("DROP EVENT IF EXISTS " + DBNames.SCHEDULED_TRANSACTIONS_EVENT);
      } catch (SQLException e) {
         System.out.println("Couldn't drop event: " + e.getMessage());
      }
   }

   public void callProcedureTransfer_money(String sender, String receiver, double amount, String description) {

      try {
         if (!checkAccountBalance(sender, amount)) {
            return;
         }
         if (amount == 0) {
            return;
         }
      } catch (SQLException e) {
         System.out.println("couldn't query account balance: " + e.getMessage());
         return;
      } catch (Exception e) {
         System.out.println("Sender doesn't exist in database");
      }

      try {
         queryAccountBalance(receiver);
      } catch (SQLException e) {
         System.out.println("couldn't query account balance: " + e.getMessage());
         return;
      } catch (Exception e) {
         System.out.println("Receiver doesn't exist in database");
      }

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

   private boolean checkAccountBalance(String accountNumber, double amount) throws Exception {
      double balance = queryAccountBalance(accountNumber);
      if (balance < amount) {
         System.out.println("Not enough money on account");
         return false;
      }
      return true;
   }

   public BankAccount queryAccountOnName(String personNr, String name) throws Exception {
      try {
         queryAccountOnName.setString(1, personNr);
         queryAccountOnName.setString(2, name);

         ResultSet result = queryAccountOnName.executeQuery();

         if (result.isBeforeFirst()) {
            result.next();
            return accountObjectMapper.mapOne(result);
         } else {
            return null;
         }

      } catch (SQLException e) {
         System.out.println("Couldn't query account: " + e.getMessage());
         throw new Exception("Couldn't query account");
      }
   }

   public boolean transferMoneyAndDeleteAccount(String accountNr, String receiverNr, String description) {

      try {
         conn.setAutoCommit(false);
         queryAccountBalance.setString(1, accountNr);
         ResultSet result = queryAccountBalance.executeQuery();

         result.next();
         double balance = accountObjectMapper.mapOne(result).getBalance();

         int affectedRows;

         if (balance != 0) {
            callProcedureTransfer_money.setString(1, accountNr);
            callProcedureTransfer_money.setString(2, receiverNr);
            callProcedureTransfer_money.setDouble(3, balance);
            callProcedureTransfer_money.setString(4, description);

            affectedRows = callProcedureTransfer_money.executeUpdate();

            if (affectedRows != 1) {
               throw new SQLException("Update failed. Affected rows: " + affectedRows);
            }
         }

         deleteAccount.setString(1, accountNr);

         affectedRows = deleteAccount.executeUpdate();

         if (affectedRows != 1) {
            System.out.println("Uptade failed. Deleted rows: " + affectedRows);
         }

         removeFuruteScheduledTransactions.setString(1, accountNr);
         removeFuruteScheduledTransactions.executeUpdate();

         try {
            conn.setAutoCommit(true);
         } catch (SQLException e) {
            System.out.println("Could not set autocommit");
         }

         return true;

      } catch (SQLException e) {
         try {
            System.out.println("Something went wrong " + e.getMessage());
            System.out.println("Performing rollback");
            conn.rollback();
            try {
               conn.setAutoCommit(true);
            } catch (SQLException e2) {
               System.out.println("Could not set autocommit");
            }
         } catch (SQLException e1) {
            System.out.println("Could not rollback: " + e.getMessage());
         }
      }
      return false;
   }

   public void updateAccount(String accountNumber, String accountName, boolean savingAccount,
                             boolean cardAccount, boolean salaryAccount) {
      try {
         conn.setAutoCommit(false);
         updateAccount.setString(1, accountName);
         if (savingAccount) {
            updateAccount.setString(2, "Y");
            removeFuruteScheduledTransactions.setString(1, accountNumber);
            removeFuruteScheduledTransactions.executeUpdate();
         } else {
            updateAccount.setString(2, "N");
         }

         if (cardAccount) {
            updateAccount.setString(3, "Y");
         } else {
            updateAccount.setString(3, "N");
         }

         if (salaryAccount) {
            updateAccount.setString(4, "Y");
         } else {
            updateAccount.setString(4, "N");
         }

         updateAccount.setString(5, accountNumber);

         updateAccount.executeUpdate();
         conn.commit();
      } catch (SQLException e) {
         System.out.println("Couldn't update account: " + e.getMessage());
         try {
            conn.rollback();
         } catch (SQLException e1) {
            System.out.println("Couldn't rollback: " + e1.getMessage());
            e1.printStackTrace();
         }
      }

      try {
         conn.setAutoCommit(true);
      } catch (SQLException e) {
         System.out.println("Couldn't set autocommit to true: " + e.getMessage());
      }
   }

   public Address queryAddress(Long addressId) {
      try {
         queryAddress.setLong(1, addressId);
         ResultSet result = queryAddress.executeQuery();
         if (result.isBeforeFirst()) {
            result.next();
            return addressObjectMapper.mapOne(result);
         } else {
            return null;
         }

      } catch (SQLException e) {
         System.out.println("Couldn't query address: " + e.getMessage());
         return null;
      }
   }

   public List<Transaction> queryTenTransactionsForUser(String personNr) {
      try {
         queryTenTransactionsForUser.setString(1, personNr);
         ResultSet results = queryTenTransactionsForUser.executeQuery();

         return transactionObjectMapper.map(results);
      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }

   public List<TransactionView> queryTenScheduledTransactions(String personNr) {
      try {
         queryTenScheduledTransactions.setString(1, personNr);
         return transactionViewObjectMapper.map(queryTenScheduledTransactions.executeQuery());
      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }

   public List<TransactionView> queryAllScheduledTransactions(String personNr) {
      try {
         queryAllScheduledTransactions.setString(1, personNr);
         return transactionViewObjectMapper.map(queryAllScheduledTransactions.executeQuery());
      } catch (SQLException e) {
         System.out.println("Couldn't query transactions: " + e.getMessage());
         return null;
      }
   }
}