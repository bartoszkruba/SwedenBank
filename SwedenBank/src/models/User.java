package models;

import datasource.sql.DBNames;
import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

public class User {

   @Table(DBNames.TABLE_USERS)
   public User() {

   }

   @Column(DBNames.COLUMN_USERS_PERSON_NR)
   @KeyDescription("CHAR(12) NOT NULL PRIMARY KEY")
   private String personNr;

   @Column(DBNames.COLUMN_USERS_FIRST_NAME)
   @KeyDescription("VARCHAR(50) NOT NULL")
   private String firstName;

   @Column(DBNames.COLUMN_USERS_LAST_NAME)
   @KeyDescription("VARCHAR(50) NOT NULL")
   private String lastName;

   @Column(DBNames.COLUMN_USERS_PASSWORD)
   @KeyDescription("VARCHAR(50) NOT NULL")
   private String password;

   @Column(DBNames.COLUMN_USERS_ADRS_ID)
   @KeyDescription("INT UNSIGNED," +
           "FOREIGN KEY (" + DBNames.COLUMN_USERS_ADRS_ID +
           ") REFERENCES " + DBNames.TABLE_ADDRESSES + "(" + DBNames.COLUMN_ADRS_ID + ") " +
           " ON UPDATE CASCADE ON DELETE SET NULL")
   private Long addressId;

   public String getPersonNr() {
      return personNr;
   }

   public User setPersonNr(String personNr) {
      this.personNr = personNr;
      return this;
   }

   public String getFirstName() {
      return firstName;
   }

   public User setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
   }

   public String getLastName() {
      return lastName;
   }

   public User setLastName(String lastName) {
      this.lastName = lastName;
      return this;
   }

   public String getPassword() {
      return password;
   }

   public User setPassword(String password) {
      this.password = password;
      return this;
   }

   public Long getAddressId() {
      return addressId;
   }

   public User setAddressId(long addressId) {
      this.addressId = addressId;
      return this;
   }
}



