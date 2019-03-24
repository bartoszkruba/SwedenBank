package models;

import datasource.DBNames;
import datasource.annotations.Column;
import datasource.annotations.KeyDescription;
import datasource.annotations.Table;

import java.sql.Date;

public class ScheduledTransaction {

   @Table(DBNames.TABLE_SCHEDULED_TRANSACTIONS)
   public ScheduledTransaction() {
   }

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_ID)
   @KeyDescription("INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY")
   private Long id;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_SENDER)
   @KeyDescription("VARCHAR(30) NOT NULL")
   private String senderAccountNumber;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_RECEIVER)
   @KeyDescription("VARCHAR(30) NOT NULL")
   private String receiverAccountNumber;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_DESC)
   @KeyDescription("VARCHAR(250) NOT NULL DEFAULT 'No Description'")
   private String description;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_DATE)
   @KeyDescription("DATE NOT NULL")
   private Date date;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_AMOUNT)
   @KeyDescription("DOUBLE(10,2) NOT NULL")
   private Double amount;

   private Double saldo;


   public String getSenderAccountNumber() {
      return senderAccountNumber;
   }

   public ScheduledTransaction setSenderAccountNumber(String senderAccountNumber) {
      this.senderAccountNumber = senderAccountNumber;
      return this;
   }

   public String getReceiverAccountNumber() {
      return receiverAccountNumber;
   }

   public ScheduledTransaction setReceiverAccountNumber(String receiverAccountNumber) {
      this.receiverAccountNumber = receiverAccountNumber;
      return this;
   }

   public String getDescription() {
      return description;
   }

   public ScheduledTransaction setDescription(String description) {
      this.description = description;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public ScheduledTransaction setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Double getSaldo() {
      return saldo;
   }

   public ScheduledTransaction setSaldo(Double saldo) {
      this.saldo = saldo;
      return this;
   }

   public Long getId() {
      return id;
   }

   public Date getDate() {
      return date;
   }

   public ScheduledTransaction setDate(Date date) {
      this.date = date;
      return this;
   }
}
