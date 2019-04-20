package datasource.sql;

public class DBNames {

    public static final String DB_NAME = "sweden_bank";
    public static final String CONNECTION_ADDRESS = "jdbc:mysql://localhost/" + DB_NAME + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
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
    public static final String COLUMN_ACCOUNTS_SAVING_ACC = "is_saving_account";
    public static final String COLUMN_ACCOUNTS_SALARY_ACC = "is_salary_account";
    public static final String COLUMN_ACCOUNTS_CARD_ACC = "is_card_account";
    public static final String COLUMN_ACCOUNTS_OPENED_ON = "opened_on";
    public static final String COLUMN_ACCOUNTS_LIMIT = "transaction_limit";

    public static final String TABLE_TRANSACTIONS = "transactions";

    public static final String COLUMN_TRANSACTIONS_ID = "_id";
    public static final String COLUMN_TRANSACTIONS_SENDER = "sender_account_nr";
    public static final String COLUMN_TRANSACTIONS_RECEIVER = "receiver_account_nr";
    public static final String COLUMN_TRANSACTIONS_DESC = "description";
    public static final String COLUMN_TRANSACTIONS_TIMESTAMP = "happened_on";
    public static final String COLUMN_TRANSACTIONS_AMOUNT = "amount";

    public static final String TABLE_SCHEDULED_TRANSACTIONS = "scheduled_transactions";

    public static final String COLUMN_SCHEDULED_TRANS_ID = "id";
    public static final String COLUMN_SCHEDULED_TRANS_SENDER = "sender_account_nr";
    public static final String COLUMN_SCHEDULED_TRANS_RECEIVER = "receiver_account_nr";
    public static final String COLUMN_SCHEDULED_TRANS_DESC = "description";
    public static final String COLUMN_SCHEDULED_TRANS_DATE = "transaction_date";
    public static final String COLUMN_SCHEDULED_TRANS_AMOUNT = "amount";

    public static final String PROCEDURE_TRANSFER_MONEY = "transfer_money";

    public static final String FUNCTION_CHECK_ACCOUNT_LIMIT = "check_account_limit";

    public static final String SCHEDULED_TRANSACTIONS_EVENT = "perform_scheduled_transactions";

}
