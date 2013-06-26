import java.awt.Button;
import java.io.*; 
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;



public class Monitor extends JFrame implements ActionListener {
	public JLabel msg = new JLabel();
	public Button btnfechar = new Button("Reconhecer Mensagem");
	
	
	
	/**
	 * Atualizado 26/06/2013
	 */
	public Monitor(){

		Container painel = this.getContentPane(); //captura o painel do JFrame
		//painel.setLayout(new FlowLayout(FlowLayout.CENTER)); //define o layout
		painel.setLayout(new GridLayout(4, 1));

		painel.add(msg);
		painel.add(btnfechar);
		//pane.add(button3);
		btnfechar.addActionListener(this); 

		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE); 
		this.setUndecorated(true);  
		String title = "Quadro de Aviso Eletr™nico";
		
		msg.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 20));
		//msg.setIcon(icon);
		
		this.setTitle(title);
		this.setSize(800, 400);
		//Pega o tamanho da tela
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		//Calcula a posição do frame a partir da resolucao usada
		int x = (screenSize.width - this.getWidth()) / 2;
		int y = (screenSize.height - this.getHeight()) / 2;

		//Define a posicao (centralizada)
		setLocation(x, y); 
		
		
		
		
		this.setResizable(false); //formul‡rio n‹o pode ter seu tamanho
		//this.setFocusableWindowState(true); 
		
		this.setVisible(false);
		registrar();
		}
	public String gerarHash(String ip, String mat) throws Exception{
		String msec = ""+System.currentTimeMillis();
		String plaintext = ip+mat+msec;
		
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(plaintext.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
		  hashtext = "0"+hashtext;
		}
		return hashtext;
	}
	public String gerarJson(Map<String, Object> mapToJson){
		String myjson = "{";
		for (Map.Entry<String, Object> entry : mapToJson.entrySet()){
			
			myjson +="\""+entry.getKey()+"\":" + "\""+entry.getValue()+"\",";
			
		}
		
		//System.out.println(myjson);
		int pos = myjson.lastIndexOf(",");
		myjson = myjson.substring(0,pos)+"}";
		return myjson;
	}
	public void doPost(String json) {
		try {
			 
			URL url = new URL("http://servidordtl.eletrosul.gov.br:8081/operation/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
	 
			String input = json;
	 
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			System.out.println(conn.getResponseCode());
//			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//				throw new RuntimeException("Failed : HTTP error code : "
//					+ conn.getResponseCode());
//			}
//	 
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					(conn.getInputStream())));
//	 
//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println(output);
//			}
	 
			conn.disconnect();
	 
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		 }
	 
		
	}
	public void registrar() {
		/**
		 * registrar endereço ip no servidor servidordtl.eletrosul.gov.br:8081/operation/
		 * json { ip,matricula,keyhash }
		 */
		
		String localhostname=null;
		try {
			localhostname = java.net.Inet4Address.getLocalHost().getHostName();
			java.net.InetAddress inetAdd = java.net.InetAddress.getByName(localhostname);
						System.out.println ("IP Address is : " + inetAdd.getHostAddress());
			String user = System.getProperty("user.name"); 
			System.out.println(user);
			String hashKey = gerarHash(inetAdd.toString(),user);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put( "ip", inetAdd.getHostAddress());
			data.put( "matricula", user );
			data.put( "idkey", hashKey );
			String json = gerarJson(data);
			System.out.println(json);
			doPost(json);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //System.out.println(localhostname);
		
		
	}

	 public static void main(String[] args) {
		 
		 //System.out.println("estou rotina principal");
		 Integer PORT = 5000;
		 String msgrx = "aguardando msg:";
		 //String msg1 = "essa Ž a primeira mensagem 1";   
		 Monitor janela = new Monitor();   
		
		 janela.setAlwaysOnTop (true);
		 
		 janela.msg.setText(msgrx);
	        
		 /**
		  * 
		 **/
		while (true)
		{
		 try {
			 //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket clientSocket = new DatagramSocket(5000); 
			 
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			clientSocket.receive(receivePacket);
			
			
			msgrx = new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
			
			janela.msg.setHorizontalAlignment(SwingConstants.CENTER);
			janela.msg.setText(msgrx);
			
			janela.setVisible(true);
			 
			clientSocket.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		}
	    
		 
	 }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		msg.setText("clicado botao");
		this.setVisible(false);
		
	}   

}
