package util;

import datasource.SwedenBankDatasource;

import java.util.Random;

public class AccountNumberGenerator {

   public static String generateUniqueNumber(SwedenBankDatasource datasource) {
      String number = generateNumber();
      while (true) {
         try {
            SwedenBankDatasource.getInstance().queryAccountBalance(number);
            number = generateNumber();
         } catch (Exception e) {
            return number;
         }
      }
   }

   public static String generateNumber() {
      Random rand = new Random();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 14; i++) {
         int n = rand.nextInt(10);
         sb.append(n);
      }
      return sb.toString();
   }
}
