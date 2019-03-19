package JavaFX;

import models.User;

public class State {
   private static State ourInstance = new State();

   public static State getInstance() {
      return ourInstance;
   }

   private User user;

   private State() {
      user = new User();
   }

   public static State getOurInstance() {
      return ourInstance;
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
}
