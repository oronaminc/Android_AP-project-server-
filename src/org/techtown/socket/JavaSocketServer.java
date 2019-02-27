package org.techtown.socket;
/*
Test CODE

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
public class JavaSocketServer {
 
    public static final int ServerPort = 9999;
    private int num_client = 0;
    private String msg;
    private DataOutputStream[] dos_arr = new DataOutputStream[10];
 
    public void go() throws IOException {
        ServerSocket ss = null;
        Socket s = null;
        int i;
 
        try {
            ss = new ServerSocket(ServerPort);
            System.out.println("S: Server Opend");
            while (true) {
                s = ss.accept();
                for( i = 0 ; i < 10 ; i++ ) {
                    if( dos_arr[i] == null ) {
                        dos_arr[i] = new DataOutputStream(s.getOutputStream());
                        break;
                    }
                }
                if( i == 10 ) {
                    System.out.println("Server is full");
                    continue;
                }
                num_client++;
                ServerThread st = new ServerThread(s, i);
                st.start();
                msg = String.format(Integer.toString(i+1) + "님이 입장했습니다.");
                send(msg);
                System.out.println(msg);
            }
        } finally {
            if (s != null)
                s.close();
            if (ss != null)
                ss.close();
            System.out.println("Server Closed");
        }
    }
 
    // send to all clients
    void send(String kkk) {
        int i;
        
        try {
            for (i = 0 ; i < 10; i++) {
                if( dos_arr[i] != null ) dos_arr[i].writeUTF(kkk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Listening thread
    public class ServerThread extends Thread {
        private Socket socket;
        private DataInputStream dis;
        private int id;
 
        ServerThread(Socket s, int i) {
            socket = s;
            id = i;
        }
        public void run() {
            try {
                service();
            } catch (IOException e) {
                msg = String.format(Integer.toString(id+1) + "님이 연결을 종료했습니다.");
                send(msg);
                System.out.println(msg);
                dos_arr[id] = null;
                num_client--;
            }
        }
 
        private void service() throws IOException {
            dis = new DataInputStream(socket.getInputStream());
 
            String str = null;
            while (true) {
                str = dis.readUTF();
                if (str == null) {
                    msg = String.format(Integer.toString(id+1) + "님이 연결을 종료했습니다.");
                    send(msg);
                    System.out.println(msg);
                    dos_arr[id] = null;
                    num_client--;
                    break;
                }
                msg = String.format(Integer.toString(id+1) + "님: " + str);
                send(msg);
                System.out.println(msg);
            }
        }
    }
 
    public static void main(String[] args) {
    	JavaSocketServer s = new JavaSocketServer();
        try {
            s.go();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/

/*
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public abstract class JavaSocketServer extends JFrame implements ActionListener {
	@SuppressWarnings("null")
	public static void main(String[] args)  {	
		
		//서버 소켓 정의하기 (5001 : Listen Port / 5002 : Send Port)
		ServerSocket serverSock1 = null;
		ServerSocket serverSock2 = null;
		
		
		
		JFrame frm = new JFrame("AP SERVER");
		frm.setBounds(100,100, 500, 100);
		frm.setLayout(new FlowLayout());
		
		JButton btn1 = new JButton("Send Json File");
		JButton btn2 = new JButton("Find File");
		JTextArea ta = new JTextArea(2,20);

		val v = new val("", serverSock1);

		frm.add(btn1);
		frm.add(btn2);
		frm.add(ta);
		frm.setVisible(true);

		try {
			serverSock2 = new ServerSocket(5002); // 파일 줄 때 써요.
			serverSock1 = new ServerSocket(5001); // 파일 받을 때 써야 함.
			v.serverSocket = serverSock1;
			System.out.println("Listening at port " + "5001 & 5002" + " ...");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		btn2.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(); //FileChooser 선언
                jfc.setDialogTitle("Choose path"); //FileChooser 창 제목
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //DIRECTORIES_ONLY, FILES_ONLY, FILES_AND_DIRECTORIES
                int option = jfc.showSaveDialog(null); //FileChooser 창안의 버튼인덱스 반환
                if (option == JFileChooser.APPROVE_OPTION) { //승인 버튼
                	File f = jfc.getSelectedFile();     
                	String path = f.getAbsolutePath(); // File f의 절대경로
                	ta.setText(path);
                	System.out.println(path);//your code here
                	v.path2 = path;
                	} else { // 승인버튼 외(X, 취소)
                		System.out.println("저장 취소");
                		}
                }
			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});		
		//파일을 주는 부분
		btn1.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent arg0) {
				try {
						if (v.path2 != "") {
							ta.setText("파일을 보냈습니다.");

							Socket2 s2 = new Socket2(v.path2, v.serverSocket);
							Thread t2 = new Thread(s2);
							t2.start();
						}else {
							ta.setText("파일을 업로드해주세요");
							System.out.println("파일을 업로드에 실패했습니다.");
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					}

					@Override
					public void mouseEntered(MouseEvent e) {}

					@Override
					public void mouseExited(MouseEvent e) {}

					@Override
					public void mousePressed(MouseEvent e) {}

					@Override
					public void mouseReleased(MouseEvent e) {}
				});
						
			Socket1 s1 = new Socket1(serverSock1);
			Thread t1 = new Thread(s1);
			t1.start();
		}
		
}

// 받아드리는 건 Socket1





class Socket1 implements Runnable{
	ServerSocket serverSock;
	
	public Socket1(ServerSocket s) {
		serverSock = s;
	}
	public void run() {
		try {

			//ServerSocket serverSock = new ServerSocket(5001);
			while(true) {
				Socket sock = serverSock.accept();
				InetAddress clientHost = sock.getLocalAddress(); 
				int clientPort = sock.getPort();
				System.out.println("A client connectecd. host : " + clientHost + ", port : " + clientPort);
				
				try {
					ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
					try {
						Object obj = instream.readObject();
						if(obj != null) {
							System.out.println(obj);
							}
						}catch(Exception e) {
							System.out.println("obj를 받을 수 없음");
							}
					} catch (Exception e) {
						System.out.println("instream 객체를 만들 수 없음");
						}
				sock.close();
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

//주는 건 Socket2
class Socket2 implements Runnable{
	ServerSocket serverSock2;
	String path;
	public Socket2(String str, ServerSocket s) {
		path = str;
		serverSock2 = s;
	}
	//final String path = "D:\\Infra\\sample.json";
	JSONParser parser = new JSONParser();
	@SuppressWarnings("resource")
	public void run() {
		try {
			Object obj2 = parser.parse(new FileReader(path));
			//ServerSocket serverSock2 = new ServerSocket(5002);	
			System.out.println("파일을 보냈습니다");
			while(true) { //클라이언트 연결 대기하기
				System.out.println("While 문으로 들어왔습니다.");
				Socket sock2 = serverSock2.accept();
				InetAddress clientHost = sock2.getLocalAddress(); 
				int clientPort = sock2.getPort();
				System.out.println("A client connectecd. host : " + clientHost + ", port : " + clientPort);
				try {
					ObjectOutputStream outstream = new ObjectOutputStream(sock2.getOutputStream());
					try {
						outstream.writeObject(obj2);
						outstream.flush();
						} catch(Exception e){
							System.out.println("obj2를 만들 수 없음");
							}
					}catch(Exception e) {
						System.out.println("outstream 객체를 만들 수 없음");
						}
				sock2.close();
				
				}
			
		}catch(Exception e) {
			e.printStackTrace();

			System.out.println("파일이 잘못 되었네요.");
			}
		}
	}



class val{
	String path2;
	ServerSocket serverSocket;	
	public val(String str, ServerSocket ss) {
		path2 = str;
		serverSocket = ss;
		}
	}

*/

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.*;
import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class JavaSocketServer {
	public static void main(String[] args) {
		
		//JAVA Swing 을 통해서 통신하기
		JFrame frm = new JFrame("AP SERVER");
		frm.setBounds(100,100, 500, 100);
		frm.setLayout(new FlowLayout());
		
		JButton btn1 = new JButton("Send File");
		JButton btn2 = new JButton("Find File");
		JTextArea ta = new JTextArea(2,20);
		
		ServerSocket server_out = null;
		String path = "";
		val vv = new val(server_out, path);

		frm.add(btn1);
		frm.add(btn2);
		frm.add(ta);
		frm.setVisible(true);
		
		ta.setText("파일을 업로드해주세요");


		// 보내기 버튼을 클릭 했을 때 반응할 수 있도록 실행
		btn1.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent ee){
			try {	
				Socket2 s2 = new Socket2(vv.serverSocket, vv.path);	
				Thread t2 = new Thread(s2);
				t2.start();
				
				ta.setText("파일을 보냈습니다.");
				
				}catch(Exception e) {
					e.printStackTrace();
				}
				}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}
			});
		
		btn2.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(); //FileChooser 선언
                jfc.setDialogTitle("Choose path"); //FileChooser 창 제목
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //DIRECTORIES_ONLY, FILES_ONLY, FILES_AND_DIRECTORIES
                int option = jfc.showSaveDialog(null); //FileChooser 창안의 버튼인덱스 반환
                if (option == JFileChooser.APPROVE_OPTION) { //승인 버튼
                	File f = jfc.getSelectedFile();     
                	String path = f.getAbsolutePath(); // File f의 절대경로
                	ta.setText(path);
                	vv.path = path;
                	} else { // 승인버튼 외(X, 취소)
                		System.out.println("저장 취소");
                		}
                }
			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		
		
		//11001 & 11002 port를 열어둠
		try {
			vv.serverSocket = new ServerSocket(11002);
			ServerSocket server_in = new ServerSocket(11001);
			//ServerSocket server_in_image = new ServerSocket(11003);
			Socket1 s1 = new Socket1(server_in);
			//Socket3 s3 = new Socket3(server_in_image);
			Thread t1 = new Thread(s1);
			//Thread t3 = new Thread(s3);
			t1.start();
			//t3.start();
			System.out.println("Listening at port " + "11001 & 11002" + " ...");
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}

class val{
	ServerSocket serverSocket;
	String path;
	public val(ServerSocket ss, String pp) {
		serverSocket = ss;
		path = pp;
		}
	}


class Socket1 implements Runnable{
	ServerSocket serverSock;

	public Socket1(ServerSocket s) {
		serverSock = s;
	}
	public void run() {
		try {
			String path = "C:\\AP";
			File folder = new File (path);
			Writer output = null;
			//ServerSocket serverSock = new ServerSocket(5001);
			while(true) {
				Socket sock = serverSock.accept();
				InetAddress clientHost = sock.getLocalAddress(); 
				int clientPort = sock.getPort();
				System.out.println("A client connectecd. host : " + clientHost + ", port : " + clientPort);
			
				ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
				String ss = (String) instream.readObject();
				String[] arr = ss.split("");
				System.out.println(arr);
				Integer i = Integer.valueOf(arr[0]);
				for (Integer x=1; x <= i; x++) {
					for (Integer y=1; y <= Integer.valueOf(arr[x]); y++) {
						System.out.println(x + " "+y);
						File file = new File(folder + "/"+x+"/"+y);
						if(!file.exists()) {
							file.mkdirs();
						}
					}
				}
				/*
				JSONObject obj2 =(JSONObject) instream.readObject();
				String obj = obj2.toJSONString().trim();
				System.out.println("받은 데이터 : " + obj);
		
				
				if(!folder.exists()) {
					folder.mkdir();
				}
				File file = new File (folder + "/data.json");
				output = new BufferedWriter(new FileWriter(file));
				output.write(obj.toString());
				output.close();
				*/
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}




class Socket2 implements Runnable{
	ServerSocket serverSock2;
	String path;
	JSONParser parser = new JSONParser();
	
	public Socket2(ServerSocket s, String p) {
		serverSock2 = s;
		path = p;
	}

	public void run() {
		try {
			System.out.println(path);
			Socket sock2 = serverSock2.accept();
			JSONObject obj2 = (JSONObject) parser.parse(new FileReader(path));
			//ServerSocket serverSock2 = new ServerSocket(5002);	
			System.out.println("파일을 보냈습니다");
			//while(true) { //클라이언트 연결 대기하기
			ObjectOutputStream outstream = new ObjectOutputStream(sock2.getOutputStream());
			outstream.writeObject(obj2);
			outstream.flush();
			System.out.println("보낼 데이터 : " + path + " from Server.");
			sock2.close();
			//}			
		}catch(Exception e) {
			e.printStackTrace();

			System.out.println("파일이 잘못 되었네요.");
			}
		}
	}


class Socket3 implements Runnable{
	ServerSocket serverSock;

	public Socket3(ServerSocket s) {
		serverSock = s;
	}
	public void run() {
		try {
			String path = "C:\\AP";
			File folder = new File (path);
			Writer output = null;
			while(true) {
				Socket sock = serverSock.accept();
				InetAddress clientHost = sock.getLocalAddress(); 
				int clientPort = sock.getPort();
				System.out.println("A client connectecd. host : " + clientHost + ", port : " + clientPort);
			
				ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
				String ss = (String) instream.readObject();
				String[] arr = ss.split("");
				System.out.println(arr);
				Integer i = Integer.valueOf(arr[0]);
				for (Integer x=1; x <= i; x++) {
					for (Integer y=1; y <= Integer.valueOf(arr[x]); y++) {
						System.out.println(x + " "+y);
						File file = new File(folder + "/"+x+"/"+y);
						if(!file.exists()) {
							file.mkdirs();
						}
					}
				}
				/*
				JSONObject obj2 =(JSONObject) instream.readObject();
				String obj = obj2.toJSONString().trim();
				System.out.println("받은 데이터 : " + obj);
		
				
				if(!folder.exists()) {
					folder.mkdir();
				}
				File file = new File (folder + "/data.json");
				output = new BufferedWriter(new FileWriter(file));
				output.write(obj.toString());
				output.close();
				*/
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}