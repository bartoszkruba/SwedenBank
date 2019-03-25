package datasource.sql;

public class SQLCode {

   public static final String QUERY_USER = "SELECT * FROM " + DBNames.TABLE_USERS +
           " WHERE " + DBNames.COLUMN_USERS_PERSON_NR + "=? AND " +
           DBNames.COLUMN_USERS_PASSWORD + " = ?";

   public static final String QUERY_ACCOUNTS_FOR_USER = "SELECT * FROM " + DBNames.TABLE_ACCOUNTS +
           " WHERE " + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = ?";

   public static final String QUERY_TEN_TRANSACTIONS = "SELECT * FROM " + DBNames.TABLE_TRANSACTIONS +
           " WHERE " + DBNames.COLUMN_TRANSACTIONS_SENDER + " = ? OR " +
           DBNames.COLUMN_TRANSACTIONS_RECEIVER + " = ? " +
           "ORDER BY " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + " DESC LIMIT 10";

   public static final String QUERY_ALL_TRANSACTIONS = "SELECT * FROM " + DBNames.TABLE_TRANSACTIONS +
           " WHERE " + DBNames.COLUMN_TRANSACTIONS_SENDER + " = ? OR " +
           DBNames.COLUMN_TRANSACTIONS_RECEIVER + " = ? " +
           "ORDER BY " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + " DESC";

   public static final String QUERY_ACCOUNT_BALANCE = "SELECT " + DBNames.COLUMN_ACCOUNTS_BALANCE +
           " FROM " + DBNames.TABLE_ACCOUNTS +
           " WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = ?";

   public static final String CALL_PROCEDURE_TRANSFER_MONEY = "CALL " +
           DBNames.PROCEDURE_TRANSFER_MONEY + "(?, ?, ?, ?)";

   public static final String QUERY_ACCOUNT_ON_NAME = "SELECT * FROM " + DBNames.TABLE_ACCOUNTS + " " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = ? AND " +
           DBNames.COLUMN_ACCOUNTS_NAME + " = ?";

   public static final String DELETE_ACCOUNT = "DELETE FROM " + DBNames.TABLE_ACCOUNTS + " " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = ?";

   public static final String UPDATE_ACCOUNT = "UPDATE " + DBNames.TABLE_ACCOUNTS + " " +
           "SET " + DBNames.COLUMN_ACCOUNTS_NAME + " = ?, " + DBNames.COLUMN_ACCOUNTS_SAVING_ACC + " = ?, " +
           DBNames.COLUMN_ACCOUNTS_CARD_ACC + " = ?, " + DBNames.COLUMN_ACCOUNTS_SALARY_ACC + " = ?, " +
           DBNames.COLUMN_ACCOUNTS_LIMIT + " = ? " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = ?";

   public static final String QUERY_ADDRESS = "SELECT * FROM " + DBNames.TABLE_ADDRESSES + " " +
           "WHERE " + DBNames.COLUMN_ADRS_ID + " = ?";

   public static final String QUERY_TEN_SCHEDULED_TRANS = "SELECT " + DBNames.COLUMN_SCHEDULED_TRANS_DESC + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_ID + ", " + DBNames.COLUMN_SCHEDULED_TRANS_DATE + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_AMOUNT + ", " + DBNames.COLUMN_ACCOUNTS_NAME + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_RECEIVER + " " +
           "FROM " + DBNames.TABLE_SCHEDULED_TRANSACTIONS + " INNER JOIN " + DBNames.TABLE_ACCOUNTS + " " +
           "ON " + DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_NUMBER + " = " +
           DBNames.TABLE_SCHEDULED_TRANSACTIONS + "." + DBNames.COLUMN_SCHEDULED_TRANS_SENDER + " " +
           "INNER JOIN " + DBNames.TABLE_USERS + " " +
           "ON " + DBNames.TABLE_USERS + "." + DBNames.COLUMN_USERS_PERSON_NR + " = " +
           DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_PERS_NR + " " +
           "WHERE " + DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = ? " +
           "ORDER BY " + DBNames.COLUMN_SCHEDULED_TRANS_DATE + " ASC LIMIT 10";

   public static final String QUERY_ALL_SCHEDULED_TRANS = "SELECT " + DBNames.COLUMN_SCHEDULED_TRANS_DESC + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_DATE + ", " + DBNames.COLUMN_SCHEDULED_TRANS_ID + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_AMOUNT + ", " + DBNames.COLUMN_ACCOUNTS_NAME + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_RECEIVER + " " +
           "FROM " + DBNames.TABLE_SCHEDULED_TRANSACTIONS + " INNER JOIN " + DBNames.TABLE_ACCOUNTS + " " +
           "ON " + DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_NUMBER + " = " +
           DBNames.TABLE_SCHEDULED_TRANSACTIONS + "." + DBNames.COLUMN_SCHEDULED_TRANS_SENDER + " " +
           "INNER JOIN " + DBNames.TABLE_USERS + " " +
           "ON " + DBNames.TABLE_USERS + "." + DBNames.COLUMN_USERS_PERSON_NR + " = " +
           DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_PERS_NR + " " +
           "WHERE " + DBNames.TABLE_ACCOUNTS + "." + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = ? " +
           "ORDER BY " + DBNames.COLUMN_SCHEDULED_TRANS_DATE + " ASC";

   public static final String QUERY_TEN_TRANSACTIONS_FOR_USER = "SELECT " + DBNames.COLUMN_TRANSACTIONS_SENDER + ", " +
           DBNames.COLUMN_TRANSACTIONS_RECEIVER + ", " + DBNames.COLUMN_TRANSACTIONS_DESC + ", " +
           DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + ", " + DBNames.COLUMN_TRANSACTIONS_AMOUNT + " " +
           "FROM " + DBNames.TABLE_USERS + " AS u " + " " +
           "INNER JOIN " + DBNames.TABLE_ACCOUNTS + " AS a " + " ON a." + DBNames.COLUMN_ACCOUNTS_PERS_NR + " = u." +
           DBNames.COLUMN_USERS_PERSON_NR + " " +
           "INNER JOIN " + DBNames.TABLE_TRANSACTIONS + " AS t " + " ON t." +
           DBNames.COLUMN_TRANSACTIONS_SENDER + " = a." +
           DBNames.COLUMN_ACCOUNTS_NUMBER + " OR t." + DBNames.COLUMN_TRANSACTIONS_RECEIVER + " = a." +
           DBNames.COLUMN_ACCOUNTS_NUMBER + " " +
           "WHERE " + DBNames.COLUMN_USERS_PERSON_NR + " = ? " +
           "GROUP BY " + DBNames.COLUMN_TRANSACTIONS_RECEIVER + ", " + DBNames.COLUMN_TRANSACTIONS_SENDER + ", " +
           DBNames.COLUMN_TRANSACTIONS_DESC + ", " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + ", " +
           DBNames.COLUMN_TRANSACTIONS_AMOUNT + " " +
           "HAVING COUNT(*) = 1 ORDER BY " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + " DESC LIMIT 10";

   public static final String DELETE_SCHEDULED_TRANSACTIONS = "DELETE FROM " +
           DBNames.TABLE_SCHEDULED_TRANSACTIONS + " " +
           "WHERE " + DBNames.COLUMN_SCHEDULED_TRANS_ID + " = ?";

   public static final String REMOVE_FUTURE_SCHEDULED_TRANSACTIONS = "DELETE FROM " +
           DBNames.TABLE_SCHEDULED_TRANSACTIONS + " " +
           "WHERE " + DBNames.COLUMN_SCHEDULED_TRANS_SENDER + " = ? " + " " +
           "AND " + DBNames.COLUMN_SCHEDULED_TRANS_DATE + " > CURDATE()";

   public static final String CREATE_TRANSFER_MONEY_PROCEDURE = "" +
           "CREATE PROCEDURE " + DBNames.PROCEDURE_TRANSFER_MONEY +
           "(sender VARCHAR(50), receiver VARCHAR(50), amount DECIMAL, description VARCHAR(250))\n" +
           "BEGIN\n" +
           "\tIF EXISTS(SELECT 1 FROM " + DBNames.TABLE_ACCOUNTS +
           " WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = receiver OR " +
           DBNames.COLUMN_ACCOUNTS_NUMBER + " = sender) THEN\n" +
           "\t\tUPDATE accounts SET " + DBNames.COLUMN_ACCOUNTS_BALANCE + " = " +
           DBNames.COLUMN_ACCOUNTS_BALANCE + " + amount " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = receiver;\n" +
           "\t\tUPDATE accounts SET " + DBNames.COLUMN_ACCOUNTS_BALANCE + " = " +
           DBNames.COLUMN_ACCOUNTS_BALANCE + " - amount " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = sender;\n" +
           "\t\tINSERT INTO " + DBNames.TABLE_TRANSACTIONS + "(" + DBNames.COLUMN_TRANSACTIONS_SENDER + ", "
           + DBNames.COLUMN_TRANSACTIONS_RECEIVER +
           ", " + DBNames.COLUMN_TRANSACTIONS_AMOUNT + ", " + DBNames.COLUMN_TRANSACTIONS_DESC + ")\n" +
           "VALUES(sender, receiver, amount, description); \n" +
           "\tEND IF;\n" +
           "END;\n" +
           "";

   public static final String CREATE_SCHEDULED_TRANSACTIONS_EVENT = "CREATE EVENT " +
           DBNames.SCHEDULED_TRANSACTIONS_EVENT + "\n" +
           "ON SCHEDULE EVERY 1 DAY\n" +
           "ON COMPLETION PRESERVE\n" +
           "DO\n" +
           "BEGIN\n" +
           "DECLARE sender_var VARCHAR(30);\n" +
           "DECLARE receiver_var VARCHAR(30);\n" +
           "DECLARE description_var VARCHAR(250);\n" +
           "DECLARE amount_var DOUBLE(10,2);\n" +
           "\nDECLARE finished INT DEFAULT 0;\n" +
           "\nDECLARE cur CURSOR FOR SELECT " + DBNames.COLUMN_SCHEDULED_TRANS_SENDER + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_RECEIVER + ", " +
           DBNames.COLUMN_SCHEDULED_TRANS_DESC + ", " + DBNames.COLUMN_SCHEDULED_TRANS_AMOUNT + " " +
           "\n\t\tFROM " + DBNames.TABLE_SCHEDULED_TRANSACTIONS + "\n" +
           "\t\tWHERE " + DBNames.COLUMN_SCHEDULED_TRANS_DATE + " = CURDATE();\n" +
           "\nDECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;\n" +
           "\nOPEN cur;\n" +
           "\ntransactionLoop :\n" +
           "\tLOOP\n" +
           "\t\tFETCH cur INTO sender_var, receiver_var, description_var, amount_var;\n" +
           "\n\t\tIF finished = 1 THEN\n" +
           "\t\t\tLEAVE transactionLoop;\n" +
           "\t\tEND IF;\n" +
           "\n\t\tIF EXISTS(SELECT 1 FROM " + DBNames.TABLE_ACCOUNTS + " " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = receiver_var AND balance - amount_var >= 0) THEN\n" +
           "\n\t\t\tIF EXISTS(SELECT 1 FROM " + DBNames.TABLE_ACCOUNTS + " " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = sender_var) THEN\n" +
           "\t\t\t\tCALL " + DBNames.PROCEDURE_TRANSFER_MONEY + "(sender_var, receiver_var, amount_var, description_var);\n" + "" +
           "\t\t\tEND IF;\n" +
           "\t\tEND IF;\n" +
           "\n\tEND LOOP;\n" +
           "\nEND;";

   public static final String CREATE_FUNCTION_CHECK_ACCOUNT_LIMIT = "CREATE FUNCTION " +
           DBNames.FUNCTION_CHECK_ACCOUNT_LIMIT + "(accountNum VARCHAR(14), transaction_amount DOUBLE(10,2))\n" +
           "\tRETURNS INT\n" +
           "BEGIN\n" +
           "\tDECLARE amountSum DOUBLE(10, 2);\n" +
           "\tDECLARE temp DOUBLE(10, 2);\n\n" +
           "\tDECLARE finished INT DEFAULT 0;\n\n" +
           "\tDECLARE cur CURSOR FOR SELECT " + DBNames.COLUMN_TRANSACTIONS_AMOUNT + "\n" +
           "\t\tFROM " + DBNames.TABLE_TRANSACTIONS + "\n" +
           "\t\tWHERE " + DBNames.COLUMN_TRANSACTIONS_TIMESTAMP + " > DATE_SUB(CURDATE(), INTERVAL 7 DAY)\n" +
           "\t\tAND " + DBNames.COLUMN_TRANSACTIONS_SENDER + " = accountNum;\n\n" +
           "DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;\n\n" +
           "OPEN cur;\n\n" +
           "SET amountSum = 0;\n\n" + "" +
           "amountLoop : \n" +
           "\tLOOP\n" +
           "\t\tFETCH cur INTO temp;\n\n" +
           "\t\tIF finished = 1 THEN\n" +
           "\t\t\tLEAVE amountLoop;\n" +
           "\t\tEND IF;\n\n" +
           "\t\tSET amountSum = amountSum + temp;\n\n" +
           "\t END LOOP;\n\n" +
           "IF EXISTS( SELECT 1 FROM " + DBNames.TABLE_ACCOUNTS + " " +
           "WHERE " + DBNames.COLUMN_ACCOUNTS_NUMBER + " = accountNum AND " +
           DBNames.COLUMN_ACCOUNTS_LIMIT + " >= (amountSum + transaction_amount)) THEN\n" +
           "\tRETURN 1;\n" +
           "ELSE\n" +
           "\tRETURN 0;\n" +
           "END IF;\n\n" +
           "END;";

   public static final String CHECK_ACCOUNT_LIMIT = "SELECT " + DBNames.FUNCTION_CHECK_ACCOUNT_LIMIT +
           "(?, ?)";
}
