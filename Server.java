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
    		System.out.println("���� ����. Ŭ���̾�Ʈ�� ��ٸ��ϴ�...");
    		
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
    	
    	private boolean ready =false; // ���� �غ� ����,���۵Ǹ� true
    	
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
    		
    		String msg; // Ŭ���̾�Ʈ�� ���� (����,����,�׺�,...)
    		while ((msg=read.readLine()) !=null) {
    			//msg�� start���->����
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
    			//���� ���� -> �й�
    			else if (msg.startsWith("STOP")) {
    				ready = false;
    			}
    			// �¸� �޼���
    			else if (msg.startsWith("WIN")) {
    				ready =false;
    				write.println("�̰���ϴ�!");
    				Gman.sendToOther(this,"�й��Ͽ����ϴ�.");
    			}
    			else if (msg.startsWith("Room")) {
    				int rn = Integer.parseInt(msg.substring(6));
    				if (!Gman.isFull(rn)) {
    					// ���� ���� �ٸ� ������� ������ �˸�.
    				
    					if (roomNumber != -1) {
    						Gman.sendToOther(this,"[EXIT]" + userName);
    						
    					}
    					roomNumber = rn;
    					
    					write.println(msg); // ����ڿ��� �޼����� �״�� ������ ������ �� ������ �˸�
    					write.println(Gman.getNamesInRoom(roomNumber)); // ����ڿ��� �� �濡 �ִ� ����� �̸� ����Ʈ�� ����
    					
    					Gman.sendToOther(this,"[ENTER]" +userName);
    				}
    					else 
    						write.println("[FULL]"); // ����ڿ��� ���� á���� �˸�
    			}
    			else if (roomNumber >=1 && msg.startsWith("[STONE]"))
    				Gman.sendToOther(this,msg);
    		
    		// start �޼����� ���
    		else if (msg.startsWith("[START]")) {
    			ready = true; // ������ ������ �غ� �Ǿ����� �˸�.
    		
    			if (Gman.isReady(roomNumber)) {
    				int a = 0; //���� ���ϴ� ���� ����Ʈ
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
    		 // ����ڰ� ������ �����ϴ� �޼���
    		else if (msg.startsWith("[STOPGAME]"))
    			ready = false;
    		
    		
    		else if (msg.startsWith("[WIN]")) {
    			ready =false;
    			
    			// ����ڿ��� �̰����� �˸�.
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
    				
    				System.out.println(userName+"���� ������ �������ϴ�.");
    				
    			}catch (Exception e) {}
    		}
    	}
    }
    // �޼��� ���� Ŭ����
    class GManager extends Vector{
    	GManager(){}
    	
    	void add (gomoku_Thread gt) { // ������ (�����) �߰�
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
    		
    	// i��° Ŭ���̾�Ʈ���� �޼����� ����
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
    	
    	boolean isFull(int roomNum) { // ���� á���� �˾ƺ��� �Լ�
    		if (roomNum==0) return false; // ������ ���� ����.
    		
    		int count=0;

    	      for(int i=0;i<size();i++)

    	        if(roomNum==getRoomNumber(i))count++;

    	      if(count>=2)return true; // 2�� �̻� á�ٸ� Ǯ��.

    	      return false;
    	}
    	
    	//�濡 �޼����� �����ϴ� �Լ�.
    	void sendToRoom(int roomNum , String msg) {
    		for (int i=0;i<size();i++)
    			if (roomNum == getRoomNumber(i))
    				sendTo(i,msg);
    	}
    	
    	
    	// GT�� ���� �濡 �ִ� �ٸ� ����ڿ��� �޼��� ���� �Լ�.
    	
    	void sendToOther(gomoku_Thread gt, String msg) {
    		for (int i=0;i<size();i++) {
    			if (getRoomNumber(i) == gt.getRoomNumber() && getGT(i) != gt)
    				sendTo(i,msg);
    		}
    	}
    	
    	
    	
    	// ������ ������ �غ� �Ǿ����� Ȯ��
    	
    	boolean isReady (int roomNum) {
    		int count =0;
    		for (int i=0;i<size();i++)
    			if (roomNum == getRoomNumber (i) && getGT(i).isReady())
    				count ++;
    		if (count ==2) return true; // �濡 �θ� ���ִ� ����� ���� ���, true�� ��ȯ
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