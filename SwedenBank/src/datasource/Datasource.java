package datasource;

import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Datasource {

   protected Connection conn;

   public Datasource() {

   }

   public boolean createDatabase(String login, String password, String databaseName) {
      try {
         Connection conn = DriverManager.getConnection("jdbc:mysql://localhost", login, password);

         Statement statement = conn.createStatement();
         statement.executeUpdate("CREATE DATABASE " + databaseName);

         conn.close();
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't create database: " + e.getMessage());
         return false;
      }
   }

   public boolean dropDatabase(String login, String password, String databaseName) {
      try {
         Connection conn = DriverManager.getConnection("jdbc:mysql://localhost", login, password);

         Statement statement = conn.createStatement();
         statement.executeUpdate("DROP DATABASE IF EXISTS " + databaseName);

         conn.close();
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't drop database: " + e.getMessage());
         return false;
      }
   }

   public boolean openConnection(String connectionString, String login, String password) {
      try {
         conn = DriverManager.getConnection(connectionString, login, password);
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't open connection " + e.getMessage());
         return false;
      }
   }

   public boolean closeConnection() {
      try {

         if (conn != null) {
            conn.close();
         }
         return true;
      } catch (SQLException e) {
         System.out.println("Couldn't close connection " + e.getMessage());
         return false;
      }
   }

   public boolean insertIntoTable(Object ob) {
      try {
         String table = ob.getClass().getConstructor().getAnnotation(Table.class).value();

         StringBuilder queryBegining = new StringBuilder("INSERT INTO ");
         queryBegining.append(table);
         queryBegining.append(" (");

         StringBuilder values = new StringBuilder();
         values.append("VALUES (");


         Field[] fieldArr = ob.getClass().getDeclaredFields();

         ArrayList<Object> valuesToInsert = new ArrayList<>();

         for (Field f : fieldArr) {
            f.setAccessible(true);

            Column column = f.getAnnotation(Column.class);

            if (column != null) {
               if (f.get(ob) != null) {
                  String name = column.value();
                  queryBegining.append(name + ", ");
                  values.append("?, ");
                  valuesToInsert.add(f.get(ob).toString());
               }
            }
         }

         queryBegining.setLength(queryBegining.length() - 2);
         queryBegining.append(") ");
         values.setLength(values.length() - 2);
         values.append(")");

         String sql = queryBegining.toString() + values.toString();

         PreparedStatement statement = conn.prepareStatement(sql);

         for (int i = 1; i <= valuesToInsert.size(); i++) {


            statement.setObject(i, valuesToInsert.get(i - 1));

         }

         System.out.println(statement.toString().replaceAll("^.*: ", ""));

         statement.executeUpdate();

         return true;
      } catch (IllegalAccessException | NoSuchMethodException | SQLException e) {
         e.printStackTrace();
         return false;
      }
   }

   public boolean createTable(Class clazz) {
      try {
         String table = ((Table) clazz.getConstructor().getAnnotation(Table.class)).value();

         StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + " (");

         Field[] fieldArr = clazz.getDeclaredFields();

         for (Field f : fieldArr) {
            Column column = f.getAnnotation(Column.class);
            KeyDescription keyDescription = f.getAnnotation(KeyDescription.class);

            if (column != null && keyDescription != null) {
               f.setAccessible(true);
               String name = column.value();
               String description = keyDescription.value();
               sb.append(name + " " + description + ", ");
            }
         }

         sb.setLength(sb.length() - 2);
         sb.append(")");

         String sql = sb.toString();
         System.out.println(sql);
         Statement statement = conn.createStatement();

         statement.executeUpdate(sql);

         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   public boolean dropTable(Class clazz) {
      try {
         String table = ((Table) clazz.getConstructor().getAnnotation(Table.class)).value();

         String sql = "DROP TABLE IF EXISTS " + table;

         Statement statement = conn.createStatement();

         statement.executeUpdate(sql);

         return true;
      } catch (NoSuchMethodException | SQLException e) {
         e.printStackTrace();
         return false;
      }
   }

   public List<?> getAllItemsFromTable(Class clazz) {
      try {

         ObjectMapper objectMapper = new ObjectMapper(clazz);

         String table = ((Table) clazz.getConstructor().getAnnotation(Table.class)).value();
         String sql = "Select * FROM " + table;

         Statement statement = conn.createStatement();

         return objectMapper.map(statement.executeQuery(sql));

      } catch (NoSuchMethodException | SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   public void turnOnScheduledEvents() {
      if (conn == null) {
         System.out.println("Connection is not open");
         return;
      }

      try {
         Statement statement = conn.createStatement();

         System.out.println(statement);

         statement.executeUpdate("SET GLOBAL event_scheduler = ON;");
      } catch (SQLException e) {
         System.out.println("Couldn't turn on scheduled events: " + e.getMessage());
      }
   }

   public void turnOffScheduledEvents() {
      if (conn == null) {
         System.out.println("Connection is not open");
         return;
      }


      try {
         Statement statement = conn.createStatement();

         System.out.println(statement);

         statement.executeUpdate("SET GLOBAL event_scheduler = OFF;");
      } catch (SQLException e) {
         System.out.println("Couldn't turn off scheduled events: " + e.getMessage());
      }
   }

}

