import java.io.Serializable;

public class Player implements Serializable {
   private String playerName;
   private String playerInfo;
   private int playerColor;
   
   public String toString() {
	   return "Player{" +
               "playerName='" + playerName + '\'' +
               ", description='" + playerInfo + '\'' +
               ", playerColor=" + playerColor +
               '}';
   }
   
   public Player(String playerName, String playerInfo) {
      this.playerName=playerName;
      this.playerInfo=playerInfo;
   }
   
   public int getPlayerColor() {
      return playerColor;
   }
   void setPlayerColor(int playerColor)
   {
      this.playerColor=playerColor;
      
   }
   public String getPlayerName() {
      return playerName;
      
   }
   public void setPlayerName(String playerName) {
      this.playerName=playerName;
   }
   public String getPlayerInfo() {
      return playerInfo;
      
   }
   public void setPlayerInfo(String playerInfo) {
      this.playerInfo=playerInfo;
   }
}