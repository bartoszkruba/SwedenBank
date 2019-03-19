package datasource;

import models.Address;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SwedenBankDatasource extends Datasource {

   private static SwedenBankDatasource instance;

   private final String QUERY_USER = "SELECT * FROM " + DBNames.TABLE_USERS +
           " WHERE " + DBNames.COLUMN_USERS_PERSON_NR + "=? AND " +
           DBNames.COLUMN_USERS_PASSWORD + "=?";

   private PreparedStatement queryUser;
   private ObjectMapper<User> userObjectMapper;
   private ObjectMapper<Address> addressObjectMapper;

   public static SwedenBankDatasource getInstance() {
      if (instance == null) {
         instance = new SwedenBankDatasource();
      }
      return instance;
   }

   private SwedenBankDatasource() {
      userObjectMapper = new ObjectMapper<>(User.class);
      addressObjectMapper = new ObjectMapper<>(Address.class);
   }

   @Override
   public boolean openConnection(String connectionString, String login, String password) {
      super.openConnection(connectionString, login, password);
      try {
         queryUser = conn.prepareStatement(QUERY_USER);
         return true;
      } catch (SQLException e) {
         e.printStackTrace();
         return false;
      }
   }


   public User QueryUser(String personnummer, String password) {
      if (queryUser == null) {
         System.out.println("Connection is not open");
         return null;
      }

      try {
         ResultSet results = queryUser.executeQuery();
         results.next();
         return userObjectMapper.mapOne(results);
      } catch (SQLException e) {
         System.out.println("Couldn't query user: " + e.getMessage());
         return null;
      }
   }
}