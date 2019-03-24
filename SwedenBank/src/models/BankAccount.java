package models;

import datasource.sql.DBNames;
import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

import java.sql.Timestamp;

public class BankAccount {

   @Table(DBNames.TABLE_ACCOUNTS)
   public BankAccount() {
   }

   @Column(DBNames.COLUMN_ACCOUNTS_NUMBER)
   @KeyDescription("CHAR(14) UNIQUE NOT NULL PRIMARY KEY")
   private String accountNumber;

   @Column(DBNames.COLUMN_ACCOUNTS_NAME)
   @KeyDescription("VARCHAR(50) NOT NULL")
   private String name;

   @Column(DBNames.COLUMN_ACCOUNTS_PERS_NR)
   @KeyDescription("CHAR(12) NOT NULL," +
           "FOREIGN KEY (" + DBNames.COLUMN_ACCOUNTS_PERS_NR +
           ") REFERENCES " + DBNames.TABLE_USERS + "(" + DBNames.COLUMN_USERS_PERSON_NR + ") " +
           " ON UPDATE CASCADE ON DELETE CASCADE")
   private String personNumber;

   @Column(DBNames.COLUMN_ACCOUNTS_BALANCE)
   @KeyDescription("DOUBLE(8,2) NOT NULL DEFAULT 0")
   private Double balance;

   @Column(DBNames.COLUMN_ACCOUNT_SAVING_ACC)
   @KeyDescription("ENUM('Y', 'N') NOT NULL DEFAULT 'N'")
   private String isSavingAccount;

   @Column(DBNames.COLUMN_ACCOUNT_SALARY_ACC)
   @KeyDescription("ENUM('Y', 'N') NOT NULL DEFAULT 'N'")
   private String isSalaryAccount;

   @Column(DBNames.COLUMN_ACCOUNT_CARD_ACC)
   @KeyDescription("ENUM('Y', 'N') NOT NULL DEFAULT 'N'")
   private String isCardAccount;

   @Column(DBNames.COLUMN_ACCOUNT_OPENED_ON)
   @KeyDescription("TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
   private Timestamp openedOn;


   public String getAccountNumber() {
      return accountNumber;
   }

   public BankAccount setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
      return this;
   }

   public String getName() {
      return name;
   }

   public BankAccount setName(String name) {
      this.name = name;
      return this;
   }

   public String getPersonNumber() {
      return personNumber;
   }

   public BankAccount setPersonNumber(String personNumber) {
      this.personNumber = personNumber;
      return this;
   }

   public double getBalance() {
      return balance;
   }

   public BankAccount setBalance(double balance) {
      this.balance = balance;
      return this;
   }

   public String getSavingAccount() {
      return isSavingAccount;
   }

   public BankAccount setSavingAccount(String savingAccount) {
      isSavingAccount = savingAccount;
      return this;
   }

   public String getSalaryAccount() {
      return isSalaryAccount;
   }

   public BankAccount setSalaryAccount(String salaryAccount) {
      isSalaryAccount = salaryAccount;
      return this;
   }

   public Timestamp getOpenedOn() {
      return openedOn;
   }

   public String getCardAccount() {
      return isCardAccount;
   }

   public BankAccount setCardAccount(String isCardAccount) {
      this.isCardAccount = isCardAccount;
      return this;
   }
}
