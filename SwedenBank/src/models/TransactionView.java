package models;

import datasource.annotations.Column;
import datasource.sql.DBNames;

import java.sql.Date;

public class TransactionView {

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_DESC)
   private String description;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_DATE)
   private Date date;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_AMOUNT)
   private Double amount;

   @Column(DBNames.COLUMN_ACCOUNTS_NAME)
   private String accountName;

   @Column(DBNames.COLUMN_SCHEDULED_TRANS_RECEIVER)
   private String receiver;


   public String getDescription() {
      return description;
   }

   public TransactionView setDescription(String description) {
      this.description = description;
      return this;
   }

   public Date getDate() {
      return date;
   }

   public TransactionView setDate(Date date) {
      this.date = date;
      return this;
   }

   public double getAmount() {
      return amount;
   }

   public TransactionView setAmount(double amount) {
      this.amount = amount;
      return this;
   }

   public String getAccountName() {
      return accountName;
   }

   public TransactionView setAccountName(String accountName) {
      this.accountName = accountName;
      return this;
   }

   public String getReceiver() {
      return receiver;
   }

   public TransactionView setReceiver(String receiver) {
      this.receiver = receiver;
      return this;
   }
}
