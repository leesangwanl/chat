package testg03.chat2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame implements ActionListener, Runnable {
	Button btn_exit;// 종료버튼
	Button btn_send;// 전송버튼
	Button btn_connect;// 서버에 접속 버튼

	TextArea txt_list;// 채팅내용
	TextField txt_server_ip;// 서버 아이피 입력필드
	TextField txt_name;// 접속자 이름
	TextField txt_input;// 채팅 입력창

	Socket client;// client 소켓
	BufferedReader br;// 입력 버퍼
	PrintWriter pw;// 출력
	String server_ip;// 서버 아이피 주소
	final int port = 7005;
	CardLayout cl;// 카드 레이아웃(판넬들이 겹쳐있어서 불러줘야만 보임)

	public ChatClient() {
		setTitle("채팅 클라이언트");
		// closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
		});
		
		cl = new CardLayout();
		setLayout(cl);
		Panel connect = new Panel();
		connect.setBackground(Color.LIGHT_GRAY);
		connect.setLayout(new BorderLayout());// connect판넬 레이아웃 구성
		btn_connect = new Button("서버접속");
		btn_connect.addActionListener(this);
		txt_server_ip = new TextField("192.168.0.86", 15);// 자신의 ip를 기본값으로 설정
		txt_name = new TextField("이상완", 15);// 접속자 이름
		Panel connect_sub = new Panel();
		connect_sub.add(new Label("서버아이피(IP): "));
		connect_sub.add(txt_server_ip);
		connect_sub.add(new Label("대화명: "));
		connect_sub.add(txt_name);

		// 채팅화면 구성
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label lblChat = new Label("채팅접속화면", Label.CENTER);
		connect.add(lblChat, BorderLayout.NORTH);
		connect.add(connect_sub, BorderLayout.CENTER);
		connect.add(btn_connect, BorderLayout.SOUTH);

		// 채팅창 화면구성
		txt_list = new TextArea();// 채팅 내용 보여주기
		txt_input = new TextField("", 25);// 채팅입력
		btn_exit = new Button("종료");// 종료버튼
		btn_send = new Button("전송");// 전송버튼
		btn_exit.addActionListener(this);// 종료버튼 수신기 부착
		btn_send.addActionListener(this);// 채팅전송 버튼 수신기 부착
		txt_input.addActionListener(this);// 채팅입력창 버튼 수신기 부착

		Panel chat_sub = new Panel();// 채팅창 sub 판넬
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);

		Label lblChatTitle = new Label("채팅 프로그램  v 1.1", Label.CENTER);
		chat.add(lblChatTitle, BorderLayout.NORTH);
		chat.add(txt_list, BorderLayout.CENTER);
		chat.add(chat_sub, BorderLayout.SOUTH);

		// 프레임에 추가
		add(connect, "접속창");
		add(chat, "채팅창");
		cl.show(this, "접속창");
		setBounds(250, 250, 300, 300);// 위치크기 동시지점
		setVisible(true);
	}

	@Override
	public void run() {
		// System.out.println("수신");
		try {
			client = new Socket(server_ip, port);// 소켓 생성
			InputStream is = client.getInputStream();// 입력
			OutputStream os = client.getOutputStream();// 출력
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(os));
			String msg = txt_name.getText();// 대화명 얻기
			pw.println(msg); // 대화명 전송
			pw.flush();// 내용을 완전히 비움
			txt_input.requestFocus();
			while (true) {
				msg = br.readLine();
				txt_list.append(msg + "\n"); // 대화명에 줄바꿈 추가
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		try {
			if (obj == btn_connect) { // 서버접속 버튼을 눌렀을때
				server_ip = txt_server_ip.getText(); // server_ip 에 txt_server_ip있는 텍스트를 가지고 온다
				Thread th = new Thread(this);
				th.start(); // run 메소드로 신호를 보냄
				cl.show(this, "채팅창");// 카드 레이아웃의 채팅창으로 화면 전환
			} else if (obj == btn_exit) {
				System.exit(0);
			} else if (obj == btn_send || obj == txt_input) {
				String msg = txt_input.getText();// 채팅내용 가져오기
				pw.println(msg);
				pw.flush();
				txt_input.setText("");// 내용 지우기
				txt_input.requestFocus();// 커서를 이곳에 두기
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		new ChatClient();
	}
}