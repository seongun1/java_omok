import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    private static final int PORT = 12345;

    private GManager Gman = new GManager();
    public Server() {}
    void startServer() {
    	
    	try { 
    		ServerSocket serverSocket = new ServerSocket(PORT);
    		System.out.println("서버 시작. 클라이언트를 기다립니다...");
    		
    		while (true) {
    			Socket socket = serverSocket.accept();
    			gomoku_Thread omok = new gomoku_Thread(socket);
    			omok.start();
    			
    		}
    }
    	catch(Exception e) {
    		System.out.println(e);
    	}
    }
    public static void main(String[] args) {
        
        	Server omokserver = new Server();
        	omokserver.startServer();
    }
    class gomoku_Thread extends Thread{
    	public int roomNumber = -1;
    	private String userName = null;
    	private Socket socket;
    	
    	private boolean ready =false; // 게임 준비 여부,시작되면 true
    	
    	private BufferedReader read;
    	private PrintWriter write;
    	
    	gomoku_Thread(Socket socket){
    		this.socket=socket;
    	}
    		Socket getSocket() {
    			return socket;
    		}
    	boolean isReady() {
    		return ready;
    	}
    	int getRoomNumber() {
    		return roomNumber;
    	}
    	String getUserName() {
    		return userName;
    	}
    	public void run() {
    		try {
    			read = new BufferedReader(new InputStreamReader
    					(socket.getInputStream()));
    			write = new PrintWriter(socket.getOutputStream());			
    		
    		String msg; // 클라이언트의 방향 (시작,종료,항복,...)
    		while ((msg=read.readLine()) !=null) {
    			//msg가 start라면->시작
    			if(msg.startsWith("start")) {
    				ready = true;
    				if(Gman.isReady(roomNumber)) {
    					int a = 1;
    					if (a==0) {
    						write.println("[COLOR]BLACK");
    						Gman.sendToOther(this,"[COLOR}white");
    					}
    					else {
    						write.println("[COLOR]WHITE");
    						Gman.sendToOther(this,"[COLOR]BLACK");
    					}
    				}
    			}
    			//게임 중지 -> 패배
    			else if (msg.startsWith("STOP")) {
    				ready = false;
    			}
    			// 승리 메세지
    			else if (msg.startsWith("WIN")) {
    				ready =false;
    				write.println("이겼습니다!");
    				Gman.sendToOther(this,"패배하였습니다.");
    			}
    			else if (msg.startsWith("Room")) {
    				int rn = Integer.parseInt(msg.substring(6));
    				if (!Gman.isFull(rn)) {
    					// 현재 방의 다른 사람에게 퇴장을 알림.
    				
    					if (roomNumber != -1) {
    						Gman.sendToOther(this,"[EXIT]" + userName);
    						
    					}
    					roomNumber = rn;
    					
    					write.println(msg); // 사용자에게 메세지를 그대로 전송해 입장할 수 있음을 알림
    					write.println(Gman.getNamesInRoom(roomNumber)); // 사용자에게 새 방에 있는 사용자 이름 리스트를 전송
    					
    					Gman.sendToOther(this,"[ENTER]" +userName);
    				}
    					else 
    						write.println("[FULL]"); // 사용자에게 방이 찼음을 알림
    			}
    			else if (roomNumber >=1 && msg.startsWith("[STONE]"))
    				Gman.sendToOther(this,msg);
    		
    		// start 메세지일 경우
    		else if (msg.startsWith("[START]")) {
    			ready = true; // 게임을 시작할 준비가 되었음을 알림.
    		
    			if (Gman.isReady(roomNumber)) {
    				int a = 0; //흑을 정하는 것이 디폴트
    				if (a==0) {
    					write.println("[COLOR]BLACK");
    					
    					Gman.sendToOther(this,"[COLOR]WHITE");
    				}
    				
    				else {
    					write.println("[COLOR]WHITE");
    					
    					Gman.sendToOther(this,"[COLOR]BLACK");
    				}
    				
    			}
    		}
    		 // 사용자가 게임을 중지하는 메세지
    		else if (msg.startsWith("[STOPGAME]"))
    			ready = false;
    		
    		
    		else if (msg.startsWith("[WIN]")) {
    			ready =false;
    			
    			// 사용자에게 이겼음을 알림.
    			write.println("WIN");
    			
    			
    			Gman.sendToOther(this,"LOSE");
    		}
    		
    		}
    		
    		}catch (Exception e) {
    			
    		}finally {
    			try {
    				Gman.remove(this);
    				
    				if(read != null) read.close();
    				
    				if(write != null) write.close();
    				
    				if (socket != null) socket.close();
    				
    				read = null; write = null; socket = null;
    				
    				System.out.println(userName+"님이 접속을 끊었습니다.");
    				
    			}catch (Exception e) {}
    		}
    	}
    }
    // 메세지 전달 클래스
    class GManager extends Vector{
    	GManager(){}
    	
    	void add (gomoku_Thread gt) { // 스레드 (사용자) 추가
    		super.add(gt);
    	}
    	void remove (gomoku_Thread gt) {
    		super.remove(gt);
    	}
    	
    	gomoku_Thread getGT(int i) {
    		return (gomoku_Thread)elementAt(i);
    	}
    	Socket getSocket (int i) {
    		return getGT(i).getSocket();
    	}
    		
    	// i번째 클라이언트에게 메세지를 전송
    	void sendTo(int i, String msg) {
    		try {
    			PrintWriter pw = new PrintWriter 
    					(getSocket(i).getOutputStream(),true);
    			pw.println(msg);
    		}catch (Exception e) {}
    	}
    	
    	
    	int getRoomNumber(int i) {
    		return getGT(i).getRoomNumber();
    	}
    	
    	boolean isFull(int roomNum) { // 방이 찼는지 알아보는 함수
    		if (roomNum==0) return false; // 대기실은 차지 않음.
    		
    		int count=0;

    	      for(int i=0;i<size();i++)

    	        if(roomNum==getRoomNumber(i))count++;

    	      if(count>=2)return true; // 2명 이상 찼다면 풀방.

    	      return false;
    	}
    	
    	//방에 메세지를 전송하는 함수.
    	void sendToRoom(int roomNum , String msg) {
    		for (int i=0;i<size();i++)
    			if (roomNum == getRoomNumber(i))
    				sendTo(i,msg);
    	}
    	
    	
    	// GT와 같은 방에 있는 다른 사용자에게 메세지 전달 함수.
    	
    	void sendToOther(gomoku_Thread gt, String msg) {
    		for (int i=0;i<size();i++) {
    			if (getRoomNumber(i) == gt.getRoomNumber() && getGT(i) != gt)
    				sendTo(i,msg);
    		}
    	}
    	
    	
    	
    	// 게임을 시작할 준비가 되었는지 확인
    	
    	boolean isReady (int roomNum) {
    		int count =0;
    		for (int i=0;i<size();i++)
    			if (roomNum == getRoomNumber (i) && getGT(i).isReady())
    				count ++;
    		if (count ==2) return true; // 방에 두명 차있는 사람이 있을 경우, true값 반환
    		return false;
    	}
    	
    	String getNamesInRoom (int roomNum) {
    		StringBuffer bb = new StringBuffer("[PLAYERS]");
    		
    		for (int i=0;i<size();i++)
    			if (roomNum == getRoomNumber(i))
    				bb.append(getGT(i).getUserName() + "\t");
    		
    		return bb.toString();
    	}
    }    
}