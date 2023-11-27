
import java.io.*;
import java.net.*;
import java.util.*;
public class Server {

    private static int black =1;
      private static int white=2;
      
      public static void main(String[] args) {
         Player playerOne= new Player("Player1","Player1입니다.");
         Board board = new Board();
         
         playerOne.setPlayerColor(black);
         Scanner scanner= new Scanner(System.in);
         int x,y;
         
        
         ServerSocket listener =null;
         Socket socket=null;
         boolean first = true;
         try {
            listener=new ServerSocket(12345);
            System.out.println("연결대기");
            socket=listener.accept();
            System.out.println("연결");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             
         
            while(true) {
            
            int[][] clientData = (int[][]) in.readObject();
            board.get(clientData);
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
               System.out.println("----------------------");
               if(board.win(x,y,playerOne))
                  break;
               
               out.writeObject(board.boardMapReturn());
               out.flush();
               
         }
            System.out.println("승리자는"+ board.getWinner().getPlayerName()+"입니다.");
            scanner.close();
            socket.close();
        }
        
         catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
     }
     
}
