package NewTest;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScoreServerFrame extends JFrame {
	private ScoreManager scoreManager = null;
	private JTextArea log = new JTextArea();
	public ScoreServerFrame() {
		super("점수 조회 서버");
		setSize(250, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //프레임 종료 버튼(X)을 클릭하면 프로그램 종료
		Container c = getContentPane();
		c.add(new JLabel("점수 조회 서버입니다"));
		c.add(new JScrollPane(log), BorderLayout.CENTER);
		setVisible(true);
		
		scoreManager = new ScoreManager("images/score.txt");
		if(scoreManager.isFileRead()) { // 단어 파일이 읽혀졌을 경우 서비스 시작
			log.setText("score.txt 읽기 완료\n");
			new ServerThread().start(); // 서비스 시작
		}
	}
	
	class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket listener = null;
			Socket socket = null;
			try {
				listener = new ServerSocket(9997);
				while(true) {
					socket = listener.accept();
					log.append("클라이언트 연결됨\n");
					new ServiceThread(socket).start();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(listener != null)
					listener.close();
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ScoreManager {
		private HashMap<String, String> map = new HashMap<String, String>();
		private boolean fileOn = false;
		
		public ScoreManager(String fileName) {
			try {
				Scanner reader = new Scanner(new FileReader(fileName));
				while(reader.hasNext()) {
					String name = reader.next();
					String score = reader.next();
					map.put(name, score);
				}
				reader.close();
				fileOn = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fileOn = false;
			}
		}
		
		public boolean isFileRead() {
			return fileOn;
		}
		
		public String get(String name) { // map에서 name의 score를 검색하여 리턴
			return map.get(name);
			
		}
	}
	
	class ServiceThread extends Thread {
		private Socket socket = null;
		private BufferedReader in = null;
		private BufferedWriter out = null;
		
		public ServiceThread(Socket socket) { // 클라이언트와 통신할 소켓을 전달받음
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					String name = in.readLine(); // 클라이언트로부터 이름 받음
					String score = scoreManager.get(name);
					if(score == null) {
						out.write("없는 이름\n");
						log.append(name + " 없음\n");
					}
					else { 
						out.write(score + "\n");
						log.append(name + ":" + score + "\n");				
					}
					out.flush();
				} catch (IOException e) {
					log.append("연결 종료\n");
					System.out.println("연결 종료");
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return; // 스레드 종료
					//e.printStackTrace();
				}

			}
		}
	}
	public static void main(String[] args) {
		new ScoreServerFrame();
	}

}
