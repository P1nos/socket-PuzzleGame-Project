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

	private boolean running = false; // ������ ���� ���ΰ��� ��Ÿ���� ����

	private PrintWriter writer; // ������� �޽����� �����ϱ� ���� ��Ʈ��
	private JButton changebtn; // ��ĭ�� �ٲ� ĭ ����
	private JButton[][] numbtn = new JButton[4][4]; // 15 ������ ��ư
	private int[][] numcount = new int[4][4]; // 15������ ����
	private int row = 0, col = 0;
	private int getrownum = 0, getcolnum = 0;
	protected static String timerBuffer; // ��� �ð� ���ڿ��� ����� ���� ����

	public PuzzleBoard() {
		
		// ��ġ
		setSize(480, 480);
		setLayout(new GridLayout(4, 4));

		int k = 1;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				numbtn[i][j] = new JButton(String.valueOf(k));
				numbtn[i][j].setFont(new Font("����ü", Font.BOLD, 30));
				add(numbtn[i][j]);
				numbtn[i][j].addKeyListener(new MyKeyListener());
				k++;
			}
		}

		setVisible(false);
		getNum();
		display();
	}

	// 0~16 �����߻�
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
					if (n == num[j]) // ���� �� ���� ����
					{
						Check = true;
						break;
					}
				}
			}
			num[i] = n;
			numcount[i / 4][i % 4] = n;
			if (n == 15) { // ���� ĭ ����
				row = i / 4;
				col = i % 4;
			}

		}
	}

	// ���÷���
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

	// ���� ���� Ȯ�� numbtn �� k �� ������ ����
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
					changebtn = numbtn[row - 1][col]; // ������ �� ��ư
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // ������ ��ư �Է�
					numbtn[row][col].setEnabled(true);

					setrow(row);
					setcol(col);
					row = row - 1; // ������ �ٽ� ����Ŵ

					changebtn = numbtn[row][col]; // ��ĭ ��ư ����
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

	public boolean isRunning() { // ������ ���� ���¸� ��ȯ�Ѵ�.

		return running;

	}

	public void startGame(String col) { // ������ �����Ѵ�.

		running = true;

	}

	public void stopGame() { // ������ �����.

		writer.println("[STOPGAME]"); // ������� �޽����� ������.

		running = false;

	}

	public void setWriter(PrintWriter writer) {

		this.writer = writer;

	}

}

public class PuzzleClient extends Frame implements Runnable, ActionListener {

	private TextArea msgView = new TextArea("", 1, 1, 1); // �޽����� �����ִ� ����

	private TextField sendBox = new TextField(""); // ���� �޽����� ���� ����

	private TextField nameBox = new TextField(); // ����� �̸� ����

	private TextField roomBox = new TextField(""); // �� ��ȣ ����

	// �濡 ������ �ο��� ���� �����ִ� ���̺�

	private Label pInfo = new Label("����:  ��");

	private java.awt.List pList = new java.awt.List(); // ����� ����� �����ִ� ����Ʈ

	private Button startButton = new Button("���� ����"); // ���� ��ư

	private Button stopButton = new Button("���"); // ��� ��ư

	private Button enterButton = new Button("�����ϱ�"); // �����ϱ� ��ư

	private Button exitButton = new Button("���Ƿ�"); // ���Ƿ� ��ư

	private Button SignUpButton = new Button("ȸ������");
	
	private Button RankButton = new Button("��ŷ");

	private PuzzleBoard puzzleBoard = new PuzzleBoard(); // Puzzle ȭ��

	private Ranking_WindowBuilder ranking = new Ranking_WindowBuilder();
	private PuzzleDB Pdb = new PuzzleDB();

	// ���� ������ �����ִ� ���̺�

	private Label infoView = new Label("���� ����", 1);

	private BufferedReader reader; // �Է� ��Ʈ��

	private PrintWriter writer; // ��� ��Ʈ��

	private Socket socket; // ����

	private int roomNumber = -1; // �� ��ȣ

	private String userName = null; // ����� �̸�
	
	private static int oldTime; // Ÿ�̸� ���� �ð��� ����ϰ� �ִ� ����
	protected static String timerBuffer; // ��� �ð� ���ڿ��� ����� ���� ����

	public PuzzleClient(String title) { // ������

		super(title);

		setLayout(null); // ���̾ƿ��� ������� �ʴ´�.

		// ���� ������Ʈ�� �����ϰ� ��ġ�Ѵ�.

		msgView.setEditable(false);

		infoView.setBounds(10, 30, 480, 30);

		infoView.setBackground(new Color(200, 200, 255));

		puzzleBoard.setLocation(10, 70);

		add(infoView);

		add(puzzleBoard);

		Panel p = new Panel();

		p.setBackground(new Color(200, 255, 255));

		p.setLayout(new GridLayout(4, 4));

		p.add(new Label("��     ��:", 2));
		p.add(nameBox);

		p.add(new Label("�� ��ȣ:", 2));
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

		// �̺�Ʈ �����ʸ� ����Ѵ�.

		sendBox.addActionListener(this);

		enterButton.addActionListener(this);

		exitButton.addActionListener(this);

		SignUpButton.addActionListener(this);
		
		RankButton.addActionListener(this);

		startButton.addActionListener(this);

		stopButton.addActionListener(this);

		// ������ �ݱ� ó��

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {

				System.exit(0);

			}

		});

	}

	// ������Ʈ���� �׼� �̺�Ʈ ó��

	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == sendBox) { // �޽��� �Է� �����̸�

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

		else if (ae.getSource() == enterButton) { // �����ϱ� ��ư�̸�

			try {

				if (Integer.parseInt(roomBox.getText()) < 1) {

					infoView.setText("���ȣ�� �߸��Ǿ����ϴ�. 1�̻�");

					return;

				}

				writer.println("[ROOM]" + Integer.parseInt(roomBox.getText()));

				msgView.setText("");

			} catch (Exception ie) {

				infoView.setText("�Է��Ͻ� ���׿� ������ �ҽ��ϴ�.");

			}

		}

		else if (ae.getSource() == exitButton) { // ���Ƿ� ��ư�̸�

			try {

				goToWaitRoom();

				startButton.setEnabled(false);

				stopButton.setEnabled(false);
				
				SignUpButton.setEnabled(false);

			} catch (Exception e) {
			}

		}
		
		else if (ae.getSource() == SignUpButton) { // ȸ������ ��ư�̸�
			
			Login_WindowBuilder login = new Login_WindowBuilder();
			login.frame.setVisible(true); login.frame.setResizable(false);
			
		}
		
		else if (ae.getSource() == RankButton) { // ��ŷ ��ư�̸�
			
			ranking.setVisible(true);
			
		}

		else if (ae.getSource() == startButton) { // ���� ���� ��ư�̸�
		
			
			
			try {
				
				writer.println("[START]");

				infoView.setText("����� ������ ��ٸ��ϴ�.");

				startButton.setEnabled(false);
				
			} catch (Exception e) {
			}
			
		}

		else if (ae.getSource() == stopButton) { // ��� ��ư�̸�

			try {

				writer.println("[DROPGAME]");

				endGame("����Ͽ����ϴ�.");

				puzzleBoard.setVisible(false);

			} catch (Exception e) {
			}

		}

	}

	void goToWaitRoom() { // ���Ƿ� ��ư�� ������ ȣ��ȴ�.

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

			infoView.setText("���ǿ� �����ϼ̽��ϴ�.");

			roomBox.setText("0");

			enterButton.setEnabled(true);

			exitButton.setEnabled(false);

		}

		else {
			JOptionPane.showMessageDialog(null, "��ϵ��� ���� ID�Դϴ�.");
		}

	}

	public void run() {

		String msg; // �����κ����� �޽���

		try {

			while ((msg = reader.readLine()) != null) {

				if (msg.startsWith("[STONE]")) { // ������� ������ ��ġ�� ��ǥ

					String temp = msg.substring(7);

					int x = Integer.parseInt(temp.substring(0, puzzleBoard.getrow()));

					int y = Integer.parseInt(temp.substring(0, puzzleBoard.getcol()));

				}

				else if (msg.startsWith("[ROOM]")) { // �濡 ����

					if (!msg.equals("[ROOM]0")) { // ������ �ƴ� ���̸�

						enterButton.setEnabled(false);

						exitButton.setEnabled(true);

						infoView.setText(msg.substring(6) + "�� �濡 �����ϼ̽��ϴ�.");

					}

					else
						infoView.setText("���ǿ� �����ϼ̽��ϴ�.");

					roomNumber = Integer.parseInt(msg.substring(6)); // �� ��ȣ ����

					if (puzzleBoard.isRunning()) { // ������ �������� �����̸�

						puzzleBoard.stopGame(); // ������ ������Ų��.

					}

				}

				else if (msg.startsWith("[FULL]")) { // ���� �� �����̸�

					infoView.setText("���� ���� ������ �� �����ϴ�.");

				}

				else if (msg.startsWith("[PLAYERS]")) { // �濡 �ִ� ����� ���

					nameList(msg.substring(9));

				}

				else if (msg.startsWith("[ENTER]")) { // �մ� ����

					pList.add(msg.substring(7));

					playersInfo();

					msgView.append("[" + msg.substring(7) + "]���� �����Ͽ����ϴ�.\n");

				}

				else if (msg.startsWith("[EXIT]")) { // �մ� ����

					pList.remove(msg.substring(6)); // ����Ʈ���� ����

					playersInfo(); // �ο����� �ٽ� ����Ͽ� �����ش�.

					msgView.append("[" + msg.substring(6) +

							"]���� �ٸ� ������ �����Ͽ����ϴ�.\n");

					if (roomNumber != 0)

						endGame("��밡 �������ϴ�.");

				}

				else if (msg.startsWith("[DISCONNECT]")) { // �մ� ���� ����

					pList.remove(msg.substring(12));

					playersInfo();

					msgView.append("[" + msg.substring(12) + "]���� ������ �������ϴ�.\n");

					if (roomNumber != 0)

						endGame("��밡 �������ϴ�.");

				}

				else if (msg.startsWith("[READY]")) {
					String str = msg.substring(7);
					puzzleBoard.startGame(str);
					
					if (str.equals("�����1")) {
						infoView.setText("���ӽ���");
						puzzleBoard.requestFocus();
						puzzleBoard.setVisible(true);
					} else {
						infoView.setText("���ӽ���");
						puzzleBoard.requestFocus();
						puzzleBoard.setVisible(true);
					}
					stopwatch(1);
					stopButton.setEnabled(true);
				}

				else if (msg.startsWith("[DROPGAME]")) { // ��밡 ����ϸ�
					
					endGame("��밡 ����Ͽ����ϴ�.");
					puzzleBoard.setVisible(false);
				}

				else if (msg.startsWith("[WIN]")) { // �̰�����
					stopwatch(0);
					endGame("�̰���ϴ�.");
					puzzleBoard.setVisible(false);
					Pdb.InsertPuzzleDB(new PuzzleData(nameBox.getText().trim(), getTimes()));
					ranking.select(); // ��ø���� ���� �����ؾߵ�
				}

				else if (msg.startsWith("[LOSE]")) { // ������

					endGame("�����ϴ�.");
					puzzleBoard.setVisible(false);
				}

				// ��ӵ� �޽����� �ƴϸ� �޽��� ������ �����ش�.

				else
					msgView.append(msg + "\n");

			}

		} catch (IOException ie) {

			msgView.append(ie + "\n");

		}

		msgView.append("������ ������ϴ�.");
		

	}

	private void endGame(String msg) { // ������ �����Ű�� �޼ҵ�

		infoView.setText(msg);

		startButton.setEnabled(false);

		stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		} // 2�ʰ� ���

		if (puzzleBoard.isRunning())
			puzzleBoard.stopGame();

		if (pList.getItemCount() == 2)
			startButton.setEnabled(true);

	}

	private void playersInfo() { // �濡 �ִ� �������� ���� �����ش�.

		int count = pList.getItemCount();

		if (roomNumber == 0)

			pInfo.setText("����: " + count + "��");

		else
			pInfo.setText(roomNumber + " �� ��: " + count + "��");

		// �뱹 ���� ��ư�� Ȱ��ȭ ���¸� �����Ѵ�.

		if (count == 2 && roomNumber != 0)

			startButton.setEnabled(true);

		else
			startButton.setEnabled(false);

	}

	// ����� ����Ʈ���� ����ڵ��� �����Ͽ� pList�� �߰��Ѵ�.

	private void nameList(String msg) {

		pList.removeAll();

		StringTokenizer st = new StringTokenizer(msg, "\t");

		while (st.hasMoreElements())

			pList.add(st.nextToken());

		playersInfo();

	}

	private void connect() { // ����

		try {

			msgView.append("������ ������ ��û�մϴ�.\n");

			socket = new Socket("127.0.0.1", 7777);

			msgView.append("---���� ����--.\n");

			msgView.append("�̸��� �Է��ϰ� ���Ƿ� �����ϼ���.\n");

			reader = new BufferedReader(

					new InputStreamReader(socket.getInputStream()));

			writer = new PrintWriter(socket.getOutputStream(), true);

			new Thread(this).start();

			puzzleBoard.setWriter(writer);

		} catch (Exception e) {

			msgView.append(e + "\n\n���� ����..\n");

		}
		
		

	}
	
	
	public static void stopwatch(int onOff) {
		if (onOff == 1) // Ÿ�̸� on
			oldTime = (int) System.currentTimeMillis() / 1000;

		if (onOff == 0) // Ÿ�̸� off, �ú��� timerBuffer �� ����
			secToHHMMSS(((int) System.currentTimeMillis() / 1000) - oldTime);

	}

	// ������ �� �ð��� �ʴ���(sec)�� �Է� �ް�, ���ڿ��� �ú��ʸ� ����
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

		PuzzleClient client = new PuzzleClient("��Ʈ��ũ �������");

		client.setSize(760,560);

		client.setVisible(true);

		client.connect();

	}

}