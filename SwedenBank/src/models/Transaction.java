package models;

import datasource.DBNames;
import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

import java.sql.Timestamp;

public class Transaction {

   @Table(DBNames.TABLE_TRANSACTIONS)
   public Transaction() {
   }

   @Column(DBNames.COLUMN_TRANSACTIONS_ID)
   @KeyDescription("INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY")
   private Long id;

   @Column(DBNames.COLUMN_TRANSACTIONS_SENDER)
   @KeyDescription("VARCHAR(30) NOT NULL")
   private String senderAccountNumber;

   @Column(DBNames.COLUMN_TRANSACTIONS_RECEIVER)
   @KeyDescription("VARCHAR(30) NOT NULL")
   private String receiverAccountNumber;

   @Column(DBNames.COLUMN_TRANSACTIONS_DESC)
   @KeyDescription("VARCHAR(250) NOT NULL DEFAULT 'No Description'")
   private String description;

   @Column(DBNames.COLUMN_TRANSACTIONS_TIMESTAMP)
   @KeyDescription("TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
   private Timestamp timestamp;

   @Column(DBNames.COLUMN_TRANSACTIONS_AMOUNT)
   @KeyDescription("DOUBLE(10,2) NOT NULL")
   private Double amount;

   private Double saldo;


   public String getSenderAccountNumber() {
      return senderAccountNumber;
   }

   public Transaction setSenderAccountNumber(String senderAccountNumber) {
      this.senderAccountNumber = senderAccountNumber;
      return this;
   }

   public String getReceiverAccountNumber() {
      return receiverAccountNumber;
   }

   public Transaction setReceiverAccountNumber(String receiverAccountNumber) {
      this.receiverAccountNumber = receiverAccountNumber;
      return this;
   }

   public String getDescription() {
      return description;
   }

   public Transaction setDescription(String description) {
      this.description = description;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public Transaction setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Double getSaldo() {
      return saldo;
   }

   public Transaction setSaldo(Double saldo) {
      this.saldo = saldo;
      return this;
   }

   public Long getId() {
      return id;
   }

   public Timestamp getTimestamp() {
      return timestamp;
   }
}
