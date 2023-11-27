
import java.io.*;
import java.net.*;
import java.util.*;
public class Server {

    private static int black =1;
      private static int white=2;
      
      public static void main(String[] args) {
         Player playerOne= new Player("Player1","Player1�Դϴ�.");
         Board board = new Board();
         
         playerOne.setPlayerColor(black);
         Scanner scanner= new Scanner(System.in);
         int x,y;
         
        
         ServerSocket listener =null;
         Socket socket=null;
         boolean first = true;
         try {
            listener=new ServerSocket(12345);
            System.out.println("������");
            socket=listener.accept();
            System.out.println("����");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             
         
            while(true) {
            
            int[][] clientData = (int[][]) in.readObject();
            board.get(clientData);
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
               System.out.println("----------------------");
               if(board.win(x,y,playerOne))
                  break;
               
               out.writeObject(board.boardMapReturn());
               out.flush();
               
         }
            System.out.println("�¸��ڴ�"+ board.getWinner().getPlayerName()+"�Դϴ�.");
            scanner.close();
            socket.close();
        }
        
         catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
     }
     
}
