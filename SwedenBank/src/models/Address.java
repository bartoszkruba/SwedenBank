package models;

import datasource.sql.DBNames;
import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

public class Address {

   @Table(DBNames.TABLE_ADDRESSES)
   public Address() {
   }

   @Column(DBNames.COLUMN_ADRS_ID)
   @KeyDescription("INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY")
   private Long id;

   @Column(DBNames.COLUMN_ADRS_STREET)
   @KeyDescription("VARCHAR(250) NOT NULL")
   private String streetName;

   @Column(DBNames.COLUMN_ADRS_STREET_NR)
   @KeyDescription("VARCHAR(20) NOT NULL")
   private String StreetNumber;

   @Column(DBNames.COLUMN_ADRS_POST_CODE)
   @KeyDescription("VARCHAR(50) NOT NULL")
   private String postCode;

   @Column(DBNames.COLUMN_ADRS_CITY)
   @KeyDescription("VARCHAR(250) NOT NULL")
   private String city;

   @Column(DBNames.COLUMN_ADRS_COUNTRY)
   @KeyDescription("VARCHAR(250) NOT NULL")
   private String Country;

   public Long getId() {
      return id;
   }

   public Address setId(long id) {
      this.id = id;
      return this;
   }

   public String getStreetName() {
      return streetName;
   }

   public Address setStreetName(String streetName) {
      this.streetName = streetName;
      return this;
   }

   public String getStreetNumber() {
      return StreetNumber;
   }

   public Address setStreetNumber(String streetNumber) {
      StreetNumber = streetNumber;
      return this;
   }

   public String getPostCode() {
      return postCode;
   }

   public Address setPostCode(String postCode) {
      this.postCode = postCode;
      return this;
   }

   public String getCity() {
      return city;
   }

   public Address setCity(String city) {
      this.city = city;
      return this;
   }

   public String getCountry() {
      return Country;
   }

   public Address setCountry(String country) {
      Country = country;
      return this;
   }
}
