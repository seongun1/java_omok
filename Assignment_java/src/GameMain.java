import java.util.Random;
import java.util.Scanner;
public class GameMain {
   private static int black =1;
   private static int white=2;
   
   public static void main(String[] args) {
      Player playerOne= new Player("홍길동","홍길동입니다.");
      Player playerTwo= new Player("고말숙","고말숙입니다.");
      Board board = new Board();
      playerSetting(playerOne, playerTwo);
      Scanner scanner= new Scanner(System.in);
      int x,y;
      
      
      while(true) {
         if(playerOne.getPlayerColor()==black)
         {
            board.show();
            System.out.println(playerOne.getPlayerName()+"이 돌 차례입니다.");
            System.out.print("돌 위치를 입력하세요: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerOne)) 
            {
               System.out.println("돌 위치를 다시 선택하세요: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            board.show();
            if(board.win(x,y,playerOne))
               break;
            System.out.println(playerTwo.getPlayerName()+"이 둘 차례입니다.");
            System.out.print("둘 위치를 입력하세요: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerTwo))
            {
               System.out.print("둘 위치를 다시 선택하세요: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            if(board.win(x,y,playerTwo))
               break;
         }
         else
         {
            board.show();
            System.out.println(playerTwo.getPlayerName()+"이 돌 차례입니다.");
            System.out.print("돌 위치를 입력하세요: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerTwo)) 
            {
               System.out.print("돌 위치를 다시 선택하세요: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            board.show();
            if(board.win(x,y,playerTwo))
               break;
            System.out.println(playerOne.getPlayerName()+"이 돌 차례입니다.");
            System.out.print("돌 위치를 입력하세요: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerOne)) {
               System.out.print("돌 위치를 다시 선택하세요: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            if(board.win(x,y,playerOne))
               break;
         }
      }
         System.out.println("승리자는"+ board.getWinner().getPlayerName()+"입니다.");
         scanner.close();
   }
   public static void playerSetting(Player playerOne,Player playerTwo) {
      Random random=new Random();
      if(random.nextBoolean()==true) {
         playerOne.setPlayerColor(black);
         playerTwo.setPlayerColor(white);
         
         }
      else
      {
         playerOne.setPlayerColor(white);
         playerTwo.setPlayerColor(black);
      }  
   }
}