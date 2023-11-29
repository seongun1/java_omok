import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

class Board extends Canvas{ // 오목판 구현 클래스
   public static final int BLACK =1, WHITE = -1; // 흑은 1, 백은 -1
   
   private int [][] map; // 오목판 배열
   
   private int size; // 격자의 가로/세로 갯수 , 15로 정함.
   
   private int cell; // 격자의 크기
   
   private String info ="게임 중지"; // 게임의 진행 상황
   
   private int color =BLACK;
   private Image dbuff; // 더블 버퍼링을 위한 변수
   
   private boolean enable = false;
   // true -> 돌을 놓을 수 있음, false -> 상대방 차례
   
   private boolean running =false; // 게임이 진행 중인가를 나타내는 변수
   
   private PrintWriter write;
   
   private Graphics gboard,gbuff; // 캔버스와 버퍼를 위한 그래픽스 객체
   
   Board(int size,int cell){ // 오목판의 생성자 (size=15,c=30)
      this.size = size; this.cell = cell;
      
      map = new int[size+2][];
      
      for (int i=0;i<map.length;i++)
         map[i] = new int[size+2];
      
      
      setBackground (new Color(174,147,90)); // 오목판의 배경색을 정함.
      
      setSize(size*(cell+1)+size,size*(cell+1)+size); // 오목판의 크기를 계산

      
      
      
      // 오목판이 마우스로 돌을 놓을 때
      
      addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            if (!enable) return; // 사용자가 누를 수 없는 상황이면 작동안함.
            
            // 마우스의 좌표를 map좌표로 계산. (비슷한 곳을 눌러도 돌이 놓아지게끔 계산)
            
            int x = (int)Math.round(e.getX()/(double) cell);
            int y = (int)Math.round(e.getY()/(double) cell);
            
            //돌을 놓을 수 있는 좌표가 아니면 빠져나온다.
            
            if (x==0 || y ==0 || x == size+1 || y== size+1) return;
            
            //해당 좌표에 돌이 놓아져 있으면 놓기 X.
            
            if (map[x][y] == BLACK || map[x][y] == WHITE) return;
            
            write.println("[STONE]"+x+" "+y);
            map[x][y] = color;
            
            // 돌을 놓은 뒤, 이겼는지 검사
            
            if (check (new Point(x,y),color)) {
               info ="이겼습니다.";
               
               write.println("[WIN]");
            }
            
            else info="상대가 돌을 놓는 중입니다 ...";
            
            repaint();
            
            enable =false;
         }
      });
   }
   
   public boolean isRunning() { // 게임의 진행 상태 반환
      return running;   
}
   public void startGame(String col) { // 게임 시작 시 -> running 이 참값이 되도록 
      running =true;
      
      if (col.equals("BLACK")) { // 흑일 경우
         
         enable = true; color = BLACK;
         
         info ="게임을 시작합니다.당신이 선공입니다.";
      }
      else {
         enable = false; color =WHITE;
         info ="상대방이 돌을 두고 있습니다 ...";
      }
         
      }
   
   public void stopGame() {  // 게임이 끝났을 경우
      reset(); // 오목판 초기화
      
      write.print("[STOPGAME]");
      
      enable = false;
      running = false;
   }
   
   public void putOpponent (int x,int y) { //상대방이 오목판 x,y자리에 놓았을 경우
      
      map[x][y] =-color;
      
      info ="당신의 차례입니다.";
      
      repaint();
      
   }
   
   public void setEnable (boolean enable) {
      // 턴을 넘기는 함수
      this.enable = enable;
   }
   
   public void setWriter (PrintWriter writer) {
      this.write = writer;
   }
   
   public void update (Graphics g) {
      paint (g);
   }
   
   public void paint (Graphics g) {
      if (gbuff ==null) {
         dbuff=createImage(getWidth(),getHeight());
         
         gbuff =dbuff.getGraphics();
      }
      drawBoard(g);
   }
   
   public void reset() { // 오목판 초기화 함수
      for (int i=0;i<map.length;i++)
         for (int j=0;j<map[i].length;j++) {
            map[i][j] =0;
         }
      info ="게임 중지";
      
      repaint();
   }
   
   
   public void drawLine() { // 오목판에 선 긋기
      gbuff.setColor(Color.black);
      
      for (int i=1;i<=size;i++) {
         gbuff.drawLine (cell,i*cell,cell*size,i*cell);
         
         gbuff.drawLine(i*cell,cell,i*cell,cell*size);
      }
   }
   private void drawBlack(int x,int y) { // 흑돌을 (x,y)에 그리기.
      Graphics gbuff = (Graphics)this.gbuff;
      
      gbuff.setColor(Color.black);
      
      gbuff.fillOval(x*cell-cell/2, y*cell-cell/2, cell, cell);
      
      gbuff.setColor(Color.white);
      
      gbuff.drawOval(x*cell-cell/2, y*cell -cell/2, cell, cell);
   }
   
   private void drawWhite(int x,int y) {
      gbuff.setColor(Color.white);
      
      gbuff.fillOval(x*cell-cell/2, y*cell-cell/2, cell, cell);
      
      gbuff.setColor(Color.black);
      
      gbuff.drawOval(x*cell-cell/2, y*cell -cell/2, cell, cell);
   }
   
   private void drawStones() { // map에 놓인 돌들을 모두 그림.
      for (int x=1;x<=size;x++)
         for (int y=1;y<=size;y++) {
            if (map[x][y] == BLACK)
               drawBlack(x,y);
            else if (map[x][y] == WHITE)
               drawWhite(x,y);   
         }
      
   }
   
   private void drawBoard(Graphics g) {
      // 오목판을 그림.
      
      gbuff.clearRect(0, 0, getWidth(), getHeight());
      
      drawLine();
      drawStones();
      //바둑돌을 그리고 안내 메세지를 출력
      gbuff.setColor(Color.blue);
      gbuff.drawString(info, 20, 15);
      g.drawImage(dbuff, 0, 0, this);
   }
   
   private boolean check(Point p, int col){

       if(count(p, 1, 0, col)+count(p, -1, 0, col)==4) return true;

       if(count(p, 0, 1, col)+count(p, 0, -1, col)==4) return true;

       if(count(p, -1, -1, col)+count(p, 1, 1, col)==4) return true;

       if(count(p, 1, -1, col)+count(p, -1, 1, col)==4) return true;

       return false;

     }

     private int count(Point p, int dx, int dy, int col){

       int i=0;

       for(; map[p.x+(i+1)*dx][p.y+(i+1)*dy]==col ;i++);

       return i;

     }
}

//보드 정의 끝.....
///////////////////////////




public class Client extends Frame implements Runnable, ActionListener{

        private TextArea msgView=new TextArea("", 1,1,1);   // 메시지를 보여주는 영역

        private TextField sendBox=new TextField("");         // 보낼 메시지

        private TextField nameBox=new TextField();          // 사용자 이름 

        private TextField roomBox=new TextField("0");        // 방 번호


        // 방에 접속한 인원의 수를 보여주는 레이블

        private Label pInfo=new Label("대기실:  명");

        private java.awt.List pList=new java.awt.List();  // 사용자 명단 리스트

        private Button startButton=new Button("시작");    // 시작 버튼

        private Button stopButton=new Button("기권");         // 기권 버튼

        private Button enterButton=new Button("입장하기");    // 입장하기 버튼

        private Button exitButton=new Button("대기실로");      // 대기실로 버튼


        // 각종 정보를 보여주는 레이블
        private Label infoView=new Label("오목 게임 과제", 1);

        private Board board=new Board(15,30);      // 오목판 객체

        private BufferedReader reader;                         // 입력 스트림

        private PrintWriter writer;                               // 출력 스트림

        private Socket socket;                                 // 소켓

        private int roomNumber=-1;                           // 방 번호

        private String userName=null;                          // 사용자 이름

        public Client(String title){                        // 생성자

          super(title);

          setLayout(null);                             

          // 각종 컴포넌트를 생성,배치

          msgView.setEditable(false);

          infoView.setBounds(10,30,480,30);

          infoView.setBackground(new Color(255,217,236));

          board.setLocation(10,70);

          add(infoView);

          add(board);

          Panel p=new Panel();   // Panel 1 (이름, 방 번호)

          p.setLayout(new FlowLayout()); 
          
          nameBox.setColumns(17);
          roomBox.setColumns(17);
          
          
          Label nameLabel = new Label("Name:");
          Label roomLabel = new Label("RoomNumber:");
          nameLabel.setBackground(new Color(178, 235, 244));
          roomLabel.setBackground(new Color(178, 235, 244));
          
          nameLabel.setPreferredSize(new Dimension(80, 20));
          roomLabel.setPreferredSize(new Dimension(80,20));
          
          p.add(nameLabel);
          p.add(nameBox);
          p.add(roomLabel);
          p.add(roomBox);
          
          enterButton.setPreferredSize(new Dimension(120, 20)); 
          exitButton.setPreferredSize(new Dimension(120, 20)); 
          
          p.add(enterButton);
          p.add(exitButton);
          
         //exitButton.setEnabled(false);

          p.setBounds(500, 30, 250, 80);
          
          Panel p2=new Panel();   //Panel 2 (사용자 명단, 시작, 기권)

          p2.setLayout(new BorderLayout());

          Panel p2_1=new Panel();

          startButton.setPreferredSize(new Dimension(120, 20)); 
          stopButton.setPreferredSize(new Dimension(120, 20)); 
          
          p2_1.add(startButton); p2_1.add(stopButton);

          p2.add(pInfo,"North");
          p2.add(pList,"Center"); p2.add(p2_1,"South");

          startButton.setEnabled(false); stopButton.setEnabled(false);

          p2.setBounds(500,110,250,180);

          //Panel p3=new Panel();   //Panel 3(채팅)

          //p3.setLayout(new BorderLayout());

          //p3.add(msgView,"Center");

          //p3.add(sendBox, "South");

          //p3.setBounds(500, 300, 250,250);

          add(p); add(p2); //add(p3);
          // 이벤트 리스너를 등록

          sendBox.addActionListener(this);

          enterButton.addActionListener(this);

          exitButton.addActionListener(this);

          startButton.addActionListener(this);

          stopButton.addActionListener(this);

            // 윈도우 닫기 처리

          addWindowListener(new WindowAdapter(){

             public void windowClosing(WindowEvent we){

               System.exit(0);

             }

          });

        }
        // 컴포넌트들의 액션 이벤트 처리

        public void actionPerformed(ActionEvent ae){

          if(ae.getSource()==sendBox){             // 메시지 입력 상자 클릭 시
            String msg=sendBox.getText();

            if(msg.length()==0)return;

            if(msg.length()>=30)msg=msg.substring(0,30);

            try{  

              writer.println("[MSG]"+msg);

              sendBox.setText("");

            }catch(Exception ie){}

          }
          else if(ae.getSource()==enterButton){         // 입장하기 버튼 클릭 시

            try{
               
              if(Integer.parseInt(roomBox.getText())<1){

                infoView.setText("1이상의 방 번호를 입력해주세요.");

                return;

              }

                writer.println("[ROOM]"+Integer.parseInt(roomBox.getText()));

                msgView.setText("");

              }catch(Exception ie){

                infoView.setText("입력하신 사항에 오류가 았습니다.");

              }
          }
          else if(ae.getSource()==exitButton){           // 대기실로 버튼 클릭 시

            try{

              goToWaitRoom();

              startButton.setEnabled(false);

              stopButton.setEnabled(false);

            }catch(Exception e){}

          }

       

          else if(ae.getSource()==startButton){          // 시작 버튼 클릭 시

            try{

              writer.println("[START]");

              infoView.setText("상대의 결정을 기다립니다.");

              startButton.setEnabled(false);

            }catch(Exception e){}

          }

          else if(ae.getSource()==stopButton){          // 기권 버튼 클릭 시

            try{

              writer.println("[DROPGAME]");

              endGame("기권하였습니다.");

            }catch(Exception e){}

          }

        }
        void goToWaitRoom(){                   // 대기실로 버튼을 누르면 호출된다.

          if(userName==null){

            String name=nameBox.getText().trim();

            if(name.length()<=1 || name.length()>10){

              infoView.setText("2글자 와 10글자 사이의 이름을 입력해주세요");
              nameBox.requestFocus();
              return;

            }
            userName=name;

            writer.println("[NAME]"+userName);    

            nameBox.setText(userName);

            nameBox.setEditable(false);

          }  

          msgView.setText("");

          writer.println("[ROOM]0");

          infoView.setText("대기실에 입장하셨습니다.");

          roomBox.setText("0");

          enterButton.setEnabled(true);

          exitButton.setEnabled(false);

        }
        public void run(){

          String msg;                             // 서버로부터의 메시지
          try{

          while((msg=reader.readLine())!=null){

              if(msg.startsWith("[STONE]")){     // 상대편이 놓은 돌의 좌표

                String temp=msg.substring(7);

                int x=Integer.parseInt(temp.substring(0,temp.indexOf(" ")));

                int y=Integer.parseInt(temp.substring(temp.indexOf(" ")+1));

                board.putOpponent(x, y);     // 상대편의 돌을 그림

                board.setEnable(true);        // 사용자가 돌을 놓을 수 있도록 한다.

              }
              else if(msg.startsWith("[ROOM]")){    // 방에 입장

                if(!msg.equals("[ROOM]0")){          // 대기실이 아닌 방이면

                  enterButton.setEnabled(false);

                  exitButton.setEnabled(true);

                  infoView.setText(msg.substring(6)+"번 방에 입장하셨습니다.");

                }
                else infoView.setText("대기실에 입장하셨습니다.");

       

                roomNumber=Integer.parseInt(msg.substring(6));     // 방 번호 지정

       

                if(board.isRunning()){                    // 게임이 진행중인 상태이면

                  board.stopGame();                    // 게임을 중지

                }
              }
              else if(msg.startsWith("[FULL]")){       // 방이 찬 상태이면

                infoView.setText("방이 차서 입장할 수 없습니다.");

              }
              else if(msg.startsWith("[PLAYERS]")){      // 방에 있는 사용자 명단
                nameList(msg.substring(9));
              }
              else if(msg.startsWith("[ENTER]")){        // 사용자 입장
                pList.add(msg.substring(7));

                playersInfo();

                msgView.append("["+ msg.substring(7)+"]님이 입장하였습니다.\n");

              }

              else if(msg.startsWith("[EXIT]")){          // 사용자 퇴장
                pList.remove(msg.substring(6));   
                
                playersInfo();                        // 인원수를 다시 계산하고 출력

                msgView.append("["+msg.substring(6)+ "]님이 다른 방으로 입장하였습니다.\n");

                if(roomNumber!=0)
                  endGame("상대가 나갔습니다.");

              }

       

              else if(msg.startsWith("[DISCONNECT]")){     // 사용자 접속 종료
                pList.remove(msg.substring(12));

                playersInfo();

                msgView.append("["+msg.substring(12)+"]님이 접속을 끊었습니다.\n");

                if(roomNumber!=0)
                  endGame("상대가 나갔습니다.");

              }

              else if(msg.startsWith("[COLOR]")){          // 돌의 색을 부여
                String color=msg.substring(7);
                
                board.startGame(color);                      // 게임을 시작

                if(color.equals("BLACK"))
                  infoView.setText("흑돌을 잡았습니다.");

                else
                  infoView.setText("백돌을 잡았습니다.");

                stopButton.setEnabled(true);                 // 기권 버튼 활성화
              }
              else if(msg.startsWith("[DROPGAME]"))      // 상대가 기권힐 경우
                endGame("상대가 기권하였습니다.");

       
              else if(msg.startsWith("[WIN]"))              // 이겼을 경우
                endGame("이겼습니다.");
              
              else if(msg.startsWith("[LOSE]"))            // 졌을 경우
                endGame("졌습니다.");

              // 약속된 메시지가 아니면 메시지 영역에 보여준다.

              else msgView.append(msg+"\n");
          }

          }catch(IOException ie){

            msgView.append(ie+"\n");

          }

          msgView.append("접속이 끊겼습니다.");

        }
        private void endGame(String msg){                // 게임의 종료시키는 메소드

          infoView.setText(msg);

          startButton.setEnabled(false);

          stopButton.setEnabled(false);

          
          try{ Thread.sleep(2000); }catch(Exception e){}    // 2초간 대기

          if(board.isRunning())board.stopGame();

          if(pList.getItemCount()==2)startButton.setEnabled(true);

        }
        private void playersInfo(){                 // 방에 있는 접속자의 수를 보여준다.

          int count=pList.getItemCount();

          if(roomNumber==0)
            pInfo.setText("대기실: "+count+"명");

          else pInfo.setText(roomNumber+" 번 방: "+count+"명");

          // 대국 시작 버튼의 활성화 상태를 점검한다.
          if(count==2 && roomNumber!=0)
            startButton.setEnabled(true);
          else startButton.setEnabled(false);
        }
        // 사용자 리스트에서 사용자들을 추출하여 pList에 추가한다.

        private void nameList(String msg){

          pList.removeAll();

          StringTokenizer st=new StringTokenizer(msg, "\t");

          while(st.hasMoreElements())

            pList.add(st.nextToken());

          playersInfo();

        }

        private void connect(){                    // 연결되었을 경우 실행

          try{

            msgView.append("서버에 연결을 요청합니다.\n");

            socket=new Socket("localhost", 7777);

            msgView.append("연결성공\n");

            msgView.append("이름을 입력하고 대기실로 입장하세요.\n");

            reader=new BufferedReader(

                                 new InputStreamReader(socket.getInputStream()));

            writer=new PrintWriter(socket.getOutputStream(), true);

            new Thread(this).start();

            board.setWriter(writer);

          }catch(Exception e){

            msgView.append(e+"\n\n연결 실패..\n");  

          }

        }
        public static void main(String[] args){
          Client client=new Client("오목 게임");
          client.setSize(760,560);
          client.setVisible(true);
          client.connect();

        }

      }