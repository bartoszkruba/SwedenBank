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
}
