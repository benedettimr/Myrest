import java.awt.Button;
import java.io.*; 
import java.net.*;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
public class Monitor extends JFrame implements ActionListener {
	public JLabel msg = new JLabel();
	public Button btnfechar = new Button("Reconhecer Mensagem");
	
	public Monitor(){

		Container painel = this.getContentPane(); //captura o painel do JFrame
		//painel.setLayout(new FlowLayout(FlowLayout.CENTER)); //define o layout
		painel.setLayout(new GridLayout(4, 1));

		painel.add(msg);
		painel.add(btnfechar);
		//pane.add(button3);
		btnfechar.addActionListener(this); 

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String title = "Quadro de Aviso Eletr™nico";
		
		msg.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 20));
		//msg.setIcon(icon);
		
		this.setTitle(title);
		this.setSize(1180, 250);
		this.setResizable(false); //formul‡rio n‹o pode ter seu tamanho
		//this.setFocusableWindowState(true); 
		
		this.setVisible(true);
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
			msgrx = new String(receivePacket.getData());
			//System.out.println(msgrx);
			janela.msg.setText(msgrx);
			 
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
		System.exit(0);
		
	}   

}
