package testg02;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends Frame implements ActionListener {
	Button btnExit;
	TextArea ta;
	Vector vChatList;
	ServerSocket ss;
	Socket sockClient;

	public ChatServer() {
		setTitle("ä�ü���");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		vChatList = new Vector();
		btnExit = new Button("��������");
		btnExit.addActionListener(this);
		ta = new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnExit, BorderLayout.SOUTH);
		setBounds(250, 250, 200, 200);
		setVisible(true);

		chatStart();

	}

	private void chatStart() {
		try {
			ss = new ServerSocket(5005);
			while (true) {
				sockClient = ss.accept();// ���� ������
				ta.append(sockClient.getInetAddress().getHostAddress() + "������\n");// �������� IP���
				ChatHandle threadChat = new ChatHandle();
				vChatList.add(threadChat);
				threadChat.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new ChatServer();
	}

	class ChatHandle extends Thread {
		BufferedReader br = null;
		PrintWriter pw = null;

		public ChatHandle() {
			try {
				// �Է�
				InputStream is = sockClient.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				// ���
				OutputStream os = sockClient.getOutputStream();
				pw = new PrintWriter(new OutputStreamWriter(os));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendAllClient(String msg) {
			int size = vChatList.size();
			try {
				for (int i = 0; i < size; i++) {
					ChatHandle chr = (ChatHandle) vChatList.elementAt(i);
					chr.pw.println(msg);
					chr.pw.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				String name = br.readLine();
				sendAllClient(name + "�Բ��� ����");
				while (true) {// ä�ó���ޱ�
					String msg = br.readLine();
					String str = sockClient.getInetAddress().getHostName();
					ta.append(msg + "/n");
					if (msg.equals("@@Exit")) {
						break;
					} else {
						sendAllClient(name + " : " + msg); // ������ ��忡�� �޼��� ���� \
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				vChatList.remove(this);
				try {
					br.close();
					pw.close();
					sockClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}// catch
			}// finally
		}// run
	}
}
