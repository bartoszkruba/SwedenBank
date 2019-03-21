package datasource;

public class DBNames {

   public static final String DB_NAME = "sweden_bank";
   public static final String CONNECTION_ADDRESS = "jdbc:mysql://localhost/" + DB_NAME;
   public static final String LOGIN = "root";
   public static final String PASSWORD = "password";

   public static final String TABLE_USERS = "users";

   public static final String COLUMN_USERS_PERSON_NR = "person_nr";
   public static final String COLUMN_USERS_FIRST_NAME = "first_name";
   public static final String COLUMN_USERS_LAST_NAME = "last_name";
   public static final String COLUMN_USERS_PASSWORD = "password";
   public static final String COLUMN_USERS_ADRS_ID = "address_id";

   public static final String TABLE_ADDRESSES = "addresses";

   public static final String COLUMN_ADRS_ID = "_id";
   public static final String COLUMN_ADRS_STREET = "street_name";
   public static final String COLUMN_ADRS_STREET_NR = "street_nr";
   public static final String COLUMN_ADRS_POST_CODE = "post_code";
   public static final String COLUMN_ADRS_CITY = "city";
   public static final String COLUMN_ADRS_COUNTRY = "country";

   public static final String TABLE_ACCOUNTS = "accounts";

   public static final String COLUMN_ACCOUNTS_NUMBER = "account_number";
   public static final String COLUMN_ACCOUNTS_NAME = "name";
   public static final String COLUMN_ACCOUNTS_PERS_NR = "user_person_nr";
   public static final String COLUMN_ACCOUNTS_BALANCE = "balance";
   public static final String COLUMN_ACCOUNT_SAVING_ACC = "is_saving_account";
   public static final String COLUMN_ACCOUNT_SALARY_ACC = "is_salary_account";
   public static final String COLUMN_ACCOUNT_OPENED_ON = "opened_on";

   public static final String TABLE_TRANSACTIONS = "transactions";

   public static final String COLUMN_TRANSACTIONS_ID = "_id";
   public static final String COLUMN_TRANSACTIONS_SENDER = "sender_account_nr";
   public static final String COLUMN_TRANSACTIONS_RECEIVER = "receiver_account_nr";
   public static final String COLUMN_TRANSACTIONS_DESC = "description";
   public static final String COLUMN_TRANSACTIONS_TIMESTAMP = "happened_on";
   public static final String COLUMN_TRANSACTIONS_AMOUNT = "amount";

   public static final String PROCEDURE_TRANSFER_MONEY = "transfer_money";

   public static final String CREATE_TRANSFER_MONEY_PROCEDURE = "" +
           "CREATE PROCEDURE " + PROCEDURE_TRANSFER_MONEY +
           "(sender VARCHAR(50), receiver VARCHAR(50), amount DECIMAL, description VARCHAR(250))\n" +
           "BEGIN\n" +
           "\tIF EXISTS(SELECT 1 FROM " + TABLE_ACCOUNTS +
           " WHERE " + COLUMN_ACCOUNTS_NUMBER + " = receiver OR " + COLUMN_ACCOUNTS_NUMBER + " = sender) THEN\n" +
           "\t\tUPDATE accounts SET " + COLUMN_ACCOUNTS_BALANCE + " = " + COLUMN_ACCOUNTS_BALANCE + " + amount " +
           "WHERE " + COLUMN_ACCOUNTS_NUMBER + " = receiver;\n" +
           "\t\tUPDATE accounts SET " + COLUMN_ACCOUNTS_BALANCE + " = " + COLUMN_ACCOUNTS_BALANCE + " - amount " +
           "WHERE " + COLUMN_ACCOUNTS_NUMBER + " = sender;\n" +
           "\t\tINSERT INTO " + TABLE_TRANSACTIONS + "(" + COLUMN_TRANSACTIONS_SENDER + ", " + COLUMN_TRANSACTIONS_RECEIVER +
           ", " + COLUMN_TRANSACTIONS_AMOUNT + ", " + COLUMN_TRANSACTIONS_DESC + ")\n" +
           "VALUES(sender, receiver, amount, description); \n" +
           "\tEND IF;\n" +
           "END;\n" +
           "";
}
