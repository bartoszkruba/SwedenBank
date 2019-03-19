package JavaFX;

import models.BankAccount;
import models.User;

import java.util.List;

public class State {
   private static State ourInstance = new State();

   public static State getInstance() {
      return ourInstance;
   }

   private User user;
   private List<BankAccount> accounts;

   private State() {
      user = new User();
   }

   public static void setOurInstance(State ourInstance) {
      State.ourInstance = ourInstance;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public List<BankAccount> getAccounts() {
      return accounts;
   }

   public void setAccounts(List<BankAccount> accounts) {
      this.accounts = accounts;
   }
}
