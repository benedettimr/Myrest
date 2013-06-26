import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class SSLSocketClient {

	private JFrame jFrame;
	private JLabel lblMsg;
	
	
	
	public SSLSocketClient(JFrame jFrame, JLabel lblMsg) {
		
		this.jFrame = jFrame;
		this.lblMsg = lblMsg;
	}



	public void read(JLabel lbl)
	{
		try {
			SSLServerSocketFactory sslserversocketfactory =
                (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
			SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

			InputStream inputstream = sslsocket.getInputStream();
			InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
			BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

			String string = null;
			while ((string = bufferedreader.readLine()) != null) {
				System.out.println(string);
				System.out.flush();
				lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
				lblMsg.setText(string);
				
				jFrame.setVisible(true);
				
			}
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
