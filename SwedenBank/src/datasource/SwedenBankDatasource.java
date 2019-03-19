package datasource;

public class SwedenBankDatasource extends Datasource {

   private static SwedenBankDatasource instance;

   public static SwedenBankDatasource getInstance() {
      if (instance == null) {
         instance = new SwedenBankDatasource();
      }
      return instance;
   }

   private SwedenBankDatasource() {

   }

}
