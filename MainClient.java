
import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;


public class MainClient {

	public static void main(String[] args) throws Exception {
		String connected = "Connected";
        String waiting = "Waiting...";
        String closing = "Closing...";
        
		while (true)
		{
			LCD.drawString(waiting,0,0);
			LCD.refresh();

	        BTConnection btc = Bluetooth.waitForConnection();
	        
			LCD.clear();
			LCD.drawString(connected,0,0);
			LCD.refresh();	

			DataInputStream dis = btc.openDataInputStream();
			DataOutputStream dos = btc.openDataOutputStream();
			
			for(int i=0;i<100;i++) {
				String str = dis.readUTF();
				LCD.drawString(str, 0, 1);
				LCD.refresh();
				Motor.A.setSpeed(700);
				Motor.B.setSpeed(700);
				int l = str.length();
				for (int j = 0; j < l; j++) {
					action(str.charAt(j));
				}
				stop();
				dos.writeUTF(str);
				dos.flush();
			}
			
			dis.close();
			dos.close();
			Thread.sleep(100); // wait for data to drain
			LCD.clear();
			LCD.drawString(closing,0,0);
			LCD.refresh();
			btc.close();
			LCD.clear();
		}
	}

	private static void action(char charAt) {
		switch (charAt) {
			case 'h': tournerGauche();
				break;
			case 'j': reculer();
				break;
			case 'k': avancer();
				break;
			case 'l': tournerDroite();
				break;
			default:
				Sound.beep();
		}
	}

	private static void tournerDroite() {
		Motor.A.backward();
		Motor.B.forward();
		Delay.msDelay(1000);
	}
	
	private static void tournerGauche() {
		Motor.A.forward();
		Motor.B.backward();
		Delay.msDelay(1000);
	}

	private static void avancer() {
		Motor.A.forward();
		Motor.B.forward();
		Delay.msDelay(1000);
	}

	private static void reculer() {
		Motor.A.backward();
		Motor.B.backward();
		Delay.msDelay(1000);
	}
	
	private static void stop() {
		for (int i = 9; i >= 0; i--) {
			Motor.A.setSpeed(i * 70);
			Motor.B.setSpeed(i * 70);
			Delay.msDelay(10);
		}
	}
}
