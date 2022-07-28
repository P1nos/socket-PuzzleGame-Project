package Socket_pkg;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

import Login_pkg.*;
import Puzzle_pkg.*;
import Ranking_pkg.*;

class PuzzleBoard extends Container {

	private boolean running = false; // 게임이 진행 중인가를 나타내는 변수

	private PrintWriter writer; // 상대편에게 메시지를 전달하기 위한 스트림
	private JButton changebtn; // 빈칸과 바꿀 칸 변경
	private JButton[][] numbtn = new JButton[4][4]; // 15 까지의 버튼
	private int[][] numcount = new int[4][4]; // 15까지의 숫자
	private int row = 0, col = 0;
	private int getrownum = 0, getcolnum = 0;
	protected static String timerBuffer; // 경과 시간 문자열이 저장될 버퍼 정의

	public PuzzleBoard() {
		
		// 배치
		setSize(480, 480);
		setLayout(new GridLayout(4, 4));

		int k = 1;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				numbtn[i][j] = new JButton(String.valueOf(k));
				numbtn[i][j].setFont(new Font("굴림체", Font.BOLD, 30));
				add(numbtn[i][j]);
				numbtn[i][j].addKeyListener(new MyKeyListener());
				k++;
			}
		}

		setVisible(false);
		getNum();
		display();
	}

	// 0~16 난수발생
	public void getNum() {
		int[] num = new int[16];
		int n = 0;
		boolean Check = false;
		for (int i = 0; i < 16; i++) {
			Check = true;
			while (Check) {
				n = (int) (Math.random() * 16);
				Check = false;
				for (int j = 0; j < i; j++) {
					if (n == num[j]) // 같은 수 저장 방지
					{
						Check = true;
						break;
					}
				}
			}
			num[i] = n;
			numcount[i / 4][i % 4] = n;
			if (n == 15) { // 랜덤 칸 생성
				row = i / 4;
				col = i % 4;
			}

		}
	}

	// 디스플레이
	public void display() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == row && j == col) {
					numbtn[i][j].setText(String.valueOf(""));

					numbtn[i][j].setEnabled(false);
				} else {
					System.out.println("numcount[" + i + "]" + "[" + j + "] " + numcount[i][j] + " ");
					numbtn[i][j].setText(String.valueOf(numcount[i][j] + 1));
					numbtn[i][j].setEnabled(true);
				}
			}
		}
	}

	// 종료 여부 확인 numbtn 과 k 가 같으면 종료
	public boolean isEnd() {

		int k = 1;
		try {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (Integer.parseInt(numbtn[i][j].getText()) != k)
						return false;
					System.out.println("k :" + k);
					k++;
				}
			}
		} catch (NumberFormatException e) {

		}

		if (k == 15)
			return true;

		return false;

	}

	private class MyKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			boolean isEnd = false;
			char keyCode = e.getKeyChar();
			switch (keyCode) {
			case 'w':
				if (row == 0) {
					break;
				} else {
					changebtn = numbtn[row - 1][col]; // 변경할 위 버튼
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // 변경할 버튼 입력
					numbtn[row][col].setEnabled(true);

					setrow(row);
					setcol(col);
					row = row - 1; // 위에를 다시 가리킴

					changebtn = numbtn[row][col]; // 빈칸 버튼 지정
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					if (isEnd()) {

						writer.println("[WIN]");

					}
					break;
				}

			case 's':
				if (row == 3) {
					break;
				} else {
					changebtn = numbtn[row + 1][col];
					numbtn[row][col].setText(String.valueOf(changebtn.getText()));
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					setrow(row);
					setcol(col);
					row = row + 1;

					changebtn = numbtn[row][col];
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					if (isEnd()) {

						writer.println("[WIN]");

					}

					break;
				}
			case 'd':
				if (col == 3) {
					break;
				} else {
					changebtn = numbtn[row][col + 1];
					numbtn[row][col].setText(String.valueOf(changebtn.getText()));
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					setrow(row);
					setcol(col);
					col = col + 1;

					changebtn = numbtn[row][col];
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					if (isEnd()) {

						writer.println("[WIN]");

					}
				}
				break;
			case 'a':
				if (col == 0) {
					break;
				} else {
					changebtn = numbtn[row][col - 1];
					numbtn[row][col].setText(String.valueOf(changebtn.getText()));
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					setrow(row);
					setcol(col);
					col = col - 1;

					changebtn = numbtn[row][col];
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					if (isEnd()) {

						writer.println("[WIN]");

					}

					break;
				}
			}
		}

	}

	public int setrow(int row) {
		getrownum = row;
		return getrownum;
	}

	public int getrow() {
		return getrownum;
	}

	public int setcol(int col) {
		getcolnum = col;
		return getcolnum;
	}

	public int getcol() {
		return getcolnum;
	}



	public static String getTimes() {
		return timerBuffer;
	}

	public boolean isRunning() { // 게임의 진행 상태를 반환한다.

		return running;

	}

	public void startGame(String col) { // 게임을 시작한다.

		running = true;

	}

	public void stopGame() { // 게임을 멈춘다.

		writer.println("[STOPGAME]"); // 상대편에게 메시지를 보낸다.

		running = false;

	}

	public void setWriter(PrintWriter writer) {

		this.writer = writer;

	}

}

public class PuzzleClient extends Frame implements Runnable, ActionListener {

	private TextArea msgView = new TextArea("", 1, 1, 1); // 메시지를 보여주는 영역

	private TextField sendBox = new TextField(""); // 보낼 메시지를 적는 상자

	private TextField nameBox = new TextField(); // 사용자 이름 상자

	private TextField roomBox = new TextField(""); // 방 번호 상자

	// 방에 접속한 인원의 수를 보여주는 레이블

	private Label pInfo = new Label("대기실:  명");

	private java.awt.List pList = new java.awt.List(); // 사용자 명단을 보여주는 리스트

	private Button startButton = new Button("게임 시작"); // 시작 버튼

	private Button stopButton = new Button("기권"); // 기권 버튼

	private Button enterButton = new Button("입장하기"); // 입장하기 버튼

	private Button exitButton = new Button("대기실로"); // 대기실로 버튼

	private Button SignUpButton = new Button("회원가입");
	
	private Button RankButton = new Button("랭킹");

	private PuzzleBoard puzzleBoard = new PuzzleBoard(); // Puzzle 화면

	private Ranking_WindowBuilder ranking = new Ranking_WindowBuilder();
	private PuzzleDB Pdb = new PuzzleDB();

	// 각종 정보를 보여주는 레이블

	private Label infoView = new Label("퍼즐 게임", 1);

	private BufferedReader reader; // 입력 스트림

	private PrintWriter writer; // 출력 스트림

	private Socket socket; // 소켓

	private int roomNumber = -1; // 방 번호

	private String userName = null; // 사용자 이름
	
	private static int oldTime; // 타이머 시작 시각을 기억하고 있는 변수
	protected static String timerBuffer; // 경과 시간 문자열이 저장될 버퍼 정의

	public PuzzleClient(String title) { // 생성자

		super(title);

		setLayout(null); // 레이아웃을 사용하지 않는다.

		// 각종 컴포넌트를 생성하고 배치한다.

		msgView.setEditable(false);

		infoView.setBounds(10, 30, 480, 30);

		infoView.setBackground(new Color(200, 200, 255));

		puzzleBoard.setLocation(10, 70);

		add(infoView);

		add(puzzleBoard);

		Panel p = new Panel();

		p.setBackground(new Color(200, 255, 255));

		p.setLayout(new GridLayout(4, 4));

		p.add(new Label("이     름:", 2));
		p.add(nameBox);

		p.add(new Label("방 번호:", 2));
		p.add(roomBox);

		p.add(enterButton);
		p.add(exitButton);
		p.add(SignUpButton);
		p.add(RankButton);

		enterButton.setEnabled(false);

		p.setBounds(500, 30, 250, 70);

		Panel p2 = new Panel();

		p2.setBackground(new Color(255, 255, 100));

		p2.setLayout(new BorderLayout());

		Panel p2_1 = new Panel();

		p2_1.add(startButton);
		p2_1.add(stopButton);

		p2.add(pInfo, "North");
		p2.add(pList, "Center");
		p2.add(p2_1, "South");

		startButton.setEnabled(false);
		stopButton.setEnabled(false);

		p2.setBounds(500, 110, 250, 180);

		Panel p3 = new Panel();

		p3.setLayout(new BorderLayout());

		p3.add(msgView, "Center");

		p3.add(sendBox, "South");

		p3.setBounds(500, 300, 250, 250);

		add(p);
		add(p2);
		add(p3);

		// 이벤트 리스너를 등록한다.

		sendBox.addActionListener(this);

		enterButton.addActionListener(this);

		exitButton.addActionListener(this);

		SignUpButton.addActionListener(this);
		
		RankButton.addActionListener(this);

		startButton.addActionListener(this);

		stopButton.addActionListener(this);

		// 윈도우 닫기 처리

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {

				System.exit(0);

			}

		});

	}

	// 컴포넌트들의 액션 이벤트 처리

	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == sendBox) { // 메시지 입력 상자이면

			String msg = sendBox.getText();

			if (msg.length() == 0)
				return;

			if (msg.length() >= 30)
				msg = msg.substring(0, 30);

			try {

				writer.println("[MSG]" + msg);

				sendBox.setText("");

			} catch (Exception ie) {
			}

		}

		else if (ae.getSource() == enterButton) { // 입장하기 버튼이면

			try {

				if (Integer.parseInt(roomBox.getText()) < 1) {

					infoView.setText("방번호가 잘못되었습니다. 1이상");

					return;

				}

				writer.println("[ROOM]" + Integer.parseInt(roomBox.getText()));

				msgView.setText("");

			} catch (Exception ie) {

				infoView.setText("입력하신 사항에 오류가 았습니다.");

			}

		}

		else if (ae.getSource() == exitButton) { // 대기실로 버튼이면

			try {

				goToWaitRoom();

				startButton.setEnabled(false);

				stopButton.setEnabled(false);
				
				SignUpButton.setEnabled(false);

			} catch (Exception e) {
			}

		}
		
		else if (ae.getSource() == SignUpButton) { // 회원가입 버튼이면
			
			Login_WindowBuilder login = new Login_WindowBuilder();
			login.frame.setVisible(true); login.frame.setResizable(false);
			
		}
		
		else if (ae.getSource() == RankButton) { // 랭킹 버튼이면
			
			ranking.setVisible(true);
			
		}

		else if (ae.getSource() == startButton) { // 게임 시작 버튼이면
		
			
			
			try {
				
				writer.println("[START]");

				infoView.setText("상대의 결정을 기다립니다.");

				startButton.setEnabled(false);
				
			} catch (Exception e) {
			}
			
		}

		else if (ae.getSource() == stopButton) { // 기권 버튼이면

			try {

				writer.println("[DROPGAME]");

				endGame("기권하였습니다.");

				puzzleBoard.setVisible(false);

			} catch (Exception e) {
			}

		}

	}

	void goToWaitRoom() { // 대기실로 버튼을 누르면 호출된다.

		String name = nameBox.getText().trim();

		LoginDB ldb = new LoginDB();

		System.out.println(ldb.LoginOX(new LoginData(name)));
		if (ldb.LoginOX(new LoginData(name)) == 1) {

			userName = name;

			writer.println("[NAME]" + userName);

			nameBox.setText(userName);

			nameBox.setEditable(false);

			msgView.setText("");

			writer.println("[ROOM]0");

			infoView.setText("대기실에 입장하셨습니다.");

			roomBox.setText("0");

			enterButton.setEnabled(true);

			exitButton.setEnabled(false);

		}

		else {
			JOptionPane.showMessageDialog(null, "등록되지 않은 ID입니다.");
		}

	}

	public void run() {

		String msg; // 서버로부터의 메시지

		try {

			while ((msg = reader.readLine()) != null) {

				if (msg.startsWith("[STONE]")) { // 상대편이 움직인 위치의 좌표

					String temp = msg.substring(7);

					int x = Integer.parseInt(temp.substring(0, puzzleBoard.getrow()));

					int y = Integer.parseInt(temp.substring(0, puzzleBoard.getcol()));

				}

				else if (msg.startsWith("[ROOM]")) { // 방에 입장

					if (!msg.equals("[ROOM]0")) { // 대기실이 아닌 방이면

						enterButton.setEnabled(false);

						exitButton.setEnabled(true);

						infoView.setText(msg.substring(6) + "번 방에 입장하셨습니다.");

					}

					else
						infoView.setText("대기실에 입장하셨습니다.");

					roomNumber = Integer.parseInt(msg.substring(6)); // 방 번호 지정

					if (puzzleBoard.isRunning()) { // 게임이 진행중인 상태이면

						puzzleBoard.stopGame(); // 게임을 중지시킨다.

					}

				}

				else if (msg.startsWith("[FULL]")) { // 방이 찬 상태이면

					infoView.setText("방이 차서 입장할 수 없습니다.");

				}

				else if (msg.startsWith("[PLAYERS]")) { // 방에 있는 사용자 명단

					nameList(msg.substring(9));

				}

				else if (msg.startsWith("[ENTER]")) { // 손님 입장

					pList.add(msg.substring(7));

					playersInfo();

					msgView.append("[" + msg.substring(7) + "]님이 입장하였습니다.\n");

				}

				else if (msg.startsWith("[EXIT]")) { // 손님 퇴장

					pList.remove(msg.substring(6)); // 리스트에서 제거

					playersInfo(); // 인원수를 다시 계산하여 보여준다.

					msgView.append("[" + msg.substring(6) +

							"]님이 다른 방으로 입장하였습니다.\n");

					if (roomNumber != 0)

						endGame("상대가 나갔습니다.");

				}

				else if (msg.startsWith("[DISCONNECT]")) { // 손님 접속 종료

					pList.remove(msg.substring(12));

					playersInfo();

					msgView.append("[" + msg.substring(12) + "]님이 접속을 끊었습니다.\n");

					if (roomNumber != 0)

						endGame("상대가 나갔습니다.");

				}

				else if (msg.startsWith("[READY]")) {
					String str = msg.substring(7);
					puzzleBoard.startGame(str);
					
					if (str.equals("사용자1")) {
						infoView.setText("게임시작");
						puzzleBoard.requestFocus();
						puzzleBoard.setVisible(true);
					} else {
						infoView.setText("게임시작");
						puzzleBoard.requestFocus();
						puzzleBoard.setVisible(true);
					}
					stopwatch(1);
					stopButton.setEnabled(true);
				}

				else if (msg.startsWith("[DROPGAME]")) { // 상대가 기권하면
					
					endGame("상대가 기권하였습니다.");
					puzzleBoard.setVisible(false);
				}

				else if (msg.startsWith("[WIN]")) { // 이겼으면
					stopwatch(0);
					endGame("이겼습니다.");
					puzzleBoard.setVisible(false);
					Pdb.InsertPuzzleDB(new PuzzleData(nameBox.getText().trim(), getTimes()));
					ranking.select(); // 중첩으로 나옴 변경해야됨
				}

				else if (msg.startsWith("[LOSE]")) { // 졌으면

					endGame("졌습니다.");
					puzzleBoard.setVisible(false);
				}

				// 약속된 메시지가 아니면 메시지 영역에 보여준다.

				else
					msgView.append(msg + "\n");

			}

		} catch (IOException ie) {

			msgView.append(ie + "\n");

		}

		msgView.append("접속이 끊겼습니다.");
		

	}

	private void endGame(String msg) { // 게임의 종료시키는 메소드

		infoView.setText(msg);

		startButton.setEnabled(false);

		stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		} // 2초간 대기

		if (puzzleBoard.isRunning())
			puzzleBoard.stopGame();

		if (pList.getItemCount() == 2)
			startButton.setEnabled(true);

	}

	private void playersInfo() { // 방에 있는 접속자의 수를 보여준다.

		int count = pList.getItemCount();

		if (roomNumber == 0)

			pInfo.setText("대기실: " + count + "명");

		else
			pInfo.setText(roomNumber + " 번 방: " + count + "명");

		// 대국 시작 버튼의 활성화 상태를 점검한다.

		if (count == 2 && roomNumber != 0)

			startButton.setEnabled(true);

		else
			startButton.setEnabled(false);

	}

	// 사용자 리스트에서 사용자들을 추출하여 pList에 추가한다.

	private void nameList(String msg) {

		pList.removeAll();

		StringTokenizer st = new StringTokenizer(msg, "\t");

		while (st.hasMoreElements())

			pList.add(st.nextToken());

		playersInfo();

	}

	private void connect() { // 연결

		try {

			msgView.append("서버에 연결을 요청합니다.\n");

			socket = new Socket("127.0.0.1", 7777);

			msgView.append("---연결 성공--.\n");

			msgView.append("이름을 입력하고 대기실로 입장하세요.\n");

			reader = new BufferedReader(

					new InputStreamReader(socket.getInputStream()));

			writer = new PrintWriter(socket.getOutputStream(), true);

			new Thread(this).start();

			puzzleBoard.setWriter(writer);

		} catch (Exception e) {

			msgView.append(e + "\n\n연결 실패..\n");

		}
		
		

	}
	
	
	public static void stopwatch(int onOff) {
		if (onOff == 1) // 타이머 on
			oldTime = (int) System.currentTimeMillis() / 1000;

		if (onOff == 0) // 타이머 off, 시분초 timerBuffer 에 저장
			secToHHMMSS(((int) System.currentTimeMillis() / 1000) - oldTime);

	}

	// 정수로 된 시간을 초단위(sec)로 입력 받고, 문자열로 시분초를 저장
	public static void secToHHMMSS(int secs) {
		int hour, min, sec;

		sec = secs % 60;
		min = secs / 60 % 60;
		hour = secs / 3600;

		timerBuffer = String.format("%02d%02d%02d", hour, min, sec);
		System.out.println(timerBuffer);
	}
	
	public static String getTimes() {
		return timerBuffer;
	}

	public static void main(String[] args) {

		PuzzleClient client = new PuzzleClient("네트워크 퍼즐게임");

		client.setSize(760,560);

		client.setVisible(true);

		client.connect();

	}

}