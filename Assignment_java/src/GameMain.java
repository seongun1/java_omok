import java.util.Random;
import java.util.Scanner;
public class GameMain {
   private static int black =1;
   private static int white=2;
   
   public static void main(String[] args) {
      Player playerOne= new Player("ȫ�浿","ȫ�浿�Դϴ�.");
      Player playerTwo= new Player("����","�����Դϴ�.");
      Board board = new Board();
      playerSetting(playerOne, playerTwo);
      Scanner scanner= new Scanner(System.in);
      int x,y;
      
      
      while(true) {
         if(playerOne.getPlayerColor()==black)
         {
            board.show();
            System.out.println(playerOne.getPlayerName()+"�� �� �����Դϴ�.");
            System.out.print("�� ��ġ�� �Է��ϼ���: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerOne)) 
            {
               System.out.println("�� ��ġ�� �ٽ� �����ϼ���: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            board.show();
            if(board.win(x,y,playerOne))
               break;
            System.out.println(playerTwo.getPlayerName()+"�� �� �����Դϴ�.");
            System.out.print("�� ��ġ�� �Է��ϼ���: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerTwo))
            {
               System.out.print("�� ��ġ�� �ٽ� �����ϼ���: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            if(board.win(x,y,playerTwo))
               break;
         }
         else
         {
            board.show();
            System.out.println(playerTwo.getPlayerName()+"�� �� �����Դϴ�.");
            System.out.print("�� ��ġ�� �Է��ϼ���: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerTwo)) 
            {
               System.out.print("�� ��ġ�� �ٽ� �����ϼ���: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            board.show();
            if(board.win(x,y,playerTwo))
               break;
            System.out.println(playerOne.getPlayerName()+"�� �� �����Դϴ�.");
            System.out.print("�� ��ġ�� �Է��ϼ���: ");
            x=scanner.nextInt()-1;
            y=scanner.nextInt()-1;
            while(!board.put(x,y,playerOne)) {
               System.out.print("�� ��ġ�� �ٽ� �����ϼ���: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
            }
            if(board.win(x,y,playerOne))
               break;
         }
      }
         System.out.println("�¸��ڴ�"+ board.getWinner().getPlayerName()+"�Դϴ�.");
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