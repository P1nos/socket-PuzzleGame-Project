package Socket_pkg;

import java.net.*;

import java.io.*;

import java.util.*;

public class PuzzleServer {

	private ServerSocket server;

	private BManager bMan = new BManager(); // 메시지 방송자

	private Random rnd = new Random();

	public PuzzleServer() {
	}

	void startServer() { // 서버를 실행한다.

		try {

			server = new ServerSocket(7777);

			System.out.println("서버소켓이 생성되었습니다.");

			while (true) {

				// 클라이언트와 연결된 스레드를 얻는다.

				Socket socket = server.accept();

				// 스레드를 만들고 실행시킨다.

				Puzzle_Thread ot = new Puzzle_Thread(socket);

				ot.start();

				// bMan에 스레드를 추가한다.

				bMan.add(ot);

				System.out.println("접속자 수: " + bMan.size());

			}

		} catch (Exception e) {

			System.out.println(e);

		}

	}

	// 클라이언트와 통신하는 스레드 클래스

	class Puzzle_Thread extends Thread {

		private int roomNumber = -1; // 방 번호

		private String userName = null; // 사용자 이름

		private Socket socket; // 소켓

		// 게임 준비 여부, true이면 게임을 시작할 준비가 되었음을 의미한다.

		private boolean ready = false;

		private BufferedReader reader; // 입력 스트림

		private PrintWriter writer; // 출력 스트림

		Puzzle_Thread(Socket socket) { // 생성자

			this.socket = socket;

		}

		Socket getSocket() { // 소켓을 반환한다.

			return socket;

		}

		int getRoomNumber() { // 방 번호를 반환한다.

			return roomNumber;

		}

		String getUserName() { // 사용자 이름을 반환한다.

			return userName;

		}

		boolean isReady() { // 준비 상태를 반환한다.

			return ready;

		}

		public void run() {

			try {

				reader = new BufferedReader(

						new InputStreamReader(socket.getInputStream()));

				writer = new PrintWriter(socket.getOutputStream(), true);

				String msg; // 클라이언트의 메시지

				while ((msg = reader.readLine()) != null) {

					// msg가 "[NAME]"으로 시작되는 메시지이면

					if (msg.startsWith("[NAME]")) {

						userName = msg.substring(6); // userName을 정한다.

					}

					// msg가 "[ROOM]"으로 시작되면 방 번호를 정한다.

					else if (msg.startsWith("[ROOM]")) {

						int roomNum = Integer.parseInt(msg.substring(6));

						if (!bMan.isFull(roomNum)) { // 방이 찬 상태가 아니면

							// 현재 방의 다른 사용에게 사용자의 퇴장을 알린다.

							if (roomNumber != -1)

								bMan.sendToOthers(this, "[EXIT]" + userName);

							// 사용자의 새 방 번호를 지정한다.

							roomNumber = roomNum;

							// 사용자에게 메시지를 그대로 전송하여 입장할 수 있음을 알린다.

							writer.println(msg);

							// 사용자에게 새 방에 있는 사용자 이름 리스트를 전송한다.

							writer.println(bMan.getNamesInRoom(roomNumber));

							// 새 방에 있는 다른 사용자에게 사용자의 입장을 알린다.

							bMan.sendToOthers(this, "[ENTER]" + userName);

						}

						else
							writer.println("[FULL]"); // 사용자에 방이 찼음을 알린다.

					}

					// "[STONE]" 메시지는 상대편에게 전송한다.

					else if (roomNumber >= 1 && msg.startsWith("[STONE]"))

						bMan.sendToOthers(this, msg);

					// 대화 메시지를 방에 전송한다.

					else if (msg.startsWith("[MSG]"))

						bMan.sendToRoom(roomNumber,

								"[" + userName + "]: " + msg.substring(5));

					// "[START]" 메시지이면

					else if (msg.startsWith("[START]")) {

						ready = true; // 게임을 시작할 준비가 되었다.

						// 다른 사용자도 게임을 시작한 준비가 되었으면

						if (bMan.isReady(roomNumber)) {

							int a = rnd.nextInt(2);

							if (a == 0) {
								writer.println("[READY]사용자1");

								bMan.sendToOthers(this, "[READY]사용자2");
							}

							else {
								writer.println("[READY]사용자2");

								bMan.sendToOthers(this, "[READY]사용자1");
							}

						}

					}

					// 사용자가 게임을 중지하는 메시지를 보내면

					else if (msg.startsWith("[STOPGAME]"))

						ready = false;

					// 사용자가 게임을 기권하는 메시지를 보내면

					else if (msg.startsWith("[DROPGAME]")) {

						ready = false;

						// 상대편에게 사용자의 기권을 알린다.

						bMan.sendToOthers(this, "[DROPGAME]");

					}

					// 사용자가 이겼다는 메시지를 보내면

					else if (msg.startsWith("[WIN]")) {

						ready = false;

						// 사용자에게 메시지를 보낸다.

						writer.println("[WIN]");

						// 상대편에는 졌음을 알린다.

						bMan.sendToOthers(this, "[LOSE]");

					}

				}

			} catch (Exception e) {

			} finally {

				try {

					bMan.remove(this);

					if (reader != null)
						reader.close();

					if (writer != null)
						writer.close();

					if (socket != null)
						socket.close();

					reader = null;
					writer = null;
					socket = null;

					System.out.println(userName + "님이 접속을 끊었습니다.");

					System.out.println("접속자 수: " + bMan.size());

					// 사용자가 접속을 끊었음을 같은 방에 알린다.

					bMan.sendToRoom(roomNumber, "[DISCONNECT]" + userName);

				} catch (Exception e) {
				}

			}

		}

	}

	class BManager extends Vector { // 메시지를 전달하는 클래스

		BManager() {
		}

		void add(Puzzle_Thread ot) { // 스레드를 추가한다.

			super.add(ot);

		}

		void remove(Puzzle_Thread ot) { // 스레드를 제거한다.

			super.remove(ot);

		}

		Puzzle_Thread getOT(int i) { // i번째 스레드를 반환한다.

			return (Puzzle_Thread) elementAt(i);

		}

		Socket getSocket(int i) { // i번째 스레드의 소켓을 반환한다.

			return getOT(i).getSocket();

		}

		// i번째 스레드와 연결된 클라이언트에게 메시지를 전송한다.

		void sendTo(int i, String msg) {

			try {

				PrintWriter pw = new PrintWriter(getSocket(i).getOutputStream(), true);

				pw.println(msg);

			} catch (Exception e) {
			}

		}

		int getRoomNumber(int i) { // i번째 스레드의 방 번호를 반환한다.

			return getOT(i).getRoomNumber();

		}

		synchronized boolean isFull(int roomNum) { // 방이 찼는지 알아본다.

			if (roomNum == 0)
				return false; // 대기실은 차지 않는다.

			// 다른 방은 2명 이상 입장할 수 없다.

			int count = 0;

			for (int i = 0; i < size(); i++)

				if (roomNum == getRoomNumber(i))
					count++;

			if (count >= 2)
				return true;

			return false;

		}

		// roomNum 방에 msg를 전송한다.

		void sendToRoom(int roomNum, String msg) {

			for (int i = 0; i < size(); i++)

				if (roomNum == getRoomNumber(i))

					sendTo(i, msg);

		}

		// ot와 같은 방에 있는 다른 사용자에게 msg를 전달한다.

		void sendToOthers(Puzzle_Thread ot, String msg) {

			for (int i = 0; i < size(); i++)

				if (getRoomNumber(i) == ot.getRoomNumber() && getOT(i) != ot)

					sendTo(i, msg);

		}

		// 게임을 시작할 준비가 되었는가를 반환한다.

		// 두 명의 사용자 모두 준비된 상태이면 true를 반환한다.

		synchronized boolean isReady(int roomNum) {

			int count = 0;

			for (int i = 0; i < size(); i++)

				if (roomNum == getRoomNumber(i) && getOT(i).isReady())

					count++;

			if (count == 2)
				return true;

			return false;

		}

		// roomNum방에 있는 사용자들의 이름을 반환한다.

		String getNamesInRoom(int roomNum) {

			StringBuffer sb = new StringBuffer("[PLAYERS]");

			for (int i = 0; i < size(); i++)

				if (roomNum == getRoomNumber(i))

					sb.append(getOT(i).getUserName() + "\t");

			return sb.toString();

		}

	}

	public static void main(String[] args) {

		PuzzleServer server = new PuzzleServer();

		server.startServer();

	}

}