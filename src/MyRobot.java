
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

//l'entré est symbolisé par la valeur 2 dans le tableau représentant le labyrinthe
// la sortie est représentée par la valeur 3, les murs par la valeur -1 et un chemin libre par la valeur 1


public class MyRobot {

	/*
	 * ROBOT SPECIFICATION
	 * 
	 * NXT_PORT_A: left motor
	 * NXT_PORT_C: right motor
	 * 
	 * NXT_PORT_S1: light sensor
	 * NXT_PORT_S2: right ultrasonic sensor
	 * NXT_PORT_S3: front ultrasonic sensor
	 * NXT_PORT_S4: left ultrasonic sensor
	 */

	LightSensor lightSensor = new LightSensor (SensorPort.S1);
	UltrasonicSensor leftSensor = new UltrasonicSensor(SensorPort.S4);
	UltrasonicSensor frontSensor = new UltrasonicSensor(SensorPort.S3);
	UltrasonicSensor rightSensor = new UltrasonicSensor(SensorPort.S2);


	int distance=0; //distance between 2 black stripe of the first cast
	boolean blackstripes=false; // exit
	public MyRobot() {
	}

	// Motor methods
	public void moveForward(int tachoCount) { //use only for moving 1 case forward
		int totalTachoCount;
		Motor.A.setSpeed(700);
		Motor.C.setSpeed(700);
		Motor.A.forward();
		Motor.C.forward();
		while(this.isOnACase()  && !blackstripes );
		while(!this.isOnACase());
		System.out.println("boucle passé");
		blackstripes= false;
		totalTachoCount = Motor.A.getTachoCount() + tachoCount;
		while (Motor.A.getTachoCount() < totalTachoCount) {
			if(!this.isOnACase()){
				blackstripes=true;
			}
		}
		this.stopMotors();
	}

	public void turnLeft(){
		int currentTachoCount = Motor.A.getTachoCount();
		Motor.A.setSpeed(700);
		Motor.A.forward();
		while (Motor.A.getTachoCount() < currentTachoCount + 292){
			if(!this.isOnACase()){
				blackstripes=true;
			}
		}
		Motor.A.stop();
	}

	public void turnRight(){
		int currentTachoCount = Motor.C.getTachoCount();
		Motor.C.setSpeed(700);
		Motor.C.forward();
		while (Motor.C.getTachoCount() < currentTachoCount + 292){
			if(!this.isOnACase()){
				blackstripes=true;
			}
		}
		Motor.C.stop();
	}

	public void stopMotors(){
		for (int i = 35; i >= 0; i--) {
			Motor.A.setSpeed(i * 20);
			Motor.C.setSpeed(i * 20);
			Delay.msDelay(10);
		}
	}

	public void uTurn() {
		int currentTachoCount = Motor.A.getTachoCount();
		Motor.A.setSpeed(700);
		Motor.C.setSpeed(700);
		Motor.A.forward();
		Motor.C.backward();
		while (Motor.A.getTachoCount() < currentTachoCount + 180){ // tacho value for u-turn
		}
		this.stopMotors();
	}

	// Ultrasonic sensor methods

	// returns true if there is a wall at the case limit
	public boolean leftWall(){
		return leftSensor.getDistance() < 15;
	}

	// returns true if there is a wall at the case limit
	public boolean rightWall(){
		return rightSensor.getDistance() < 15;
	}

	// returns true if there is a wall at the case limit
	public boolean frontWall(){
		return frontSensor.getDistance() < 15;
	}

	// Light sensor methods
	// returns true if the robot is on a case, else returns false
	public boolean isOnACase(){
		return (lightSensor.getLightValue() > 50);
	}

	// explore lab'
	public void explore(){
		LinkedLabyrinth ll = new LinkedLabyrinth();
		Orientation robori = Orientation.NORTH; // Starting robot Orientation
		Position robpos = new Position(0, 0); // Starting robot position

		while (! blackstripes) {
			ll.setWall(robpos, robori.rotate(false), this.rightWall());
			ll.setWall(robpos, robori, this.frontWall());
			ll.setWall(robpos, robori.rotate(true), this.leftWall());
			if (! this.rightWall() && ! ll.isExplored(robpos.next(robori.rotate(false)))) {
				this.turnRight();
				robori = robori.rotate(false);
				this.moveForward(150);
				robpos = robpos.next(robori);
				robpos = ll.setExplored(robpos, true);

			}
			else{
				if (! this.frontWall() && ! ll.isExplored(robpos.next(robori))) {
					this.moveForward(150);
					robpos = robpos.next(robori);
					robpos = ll.setExplored(robpos, true);
				} else {
					if (! this.leftWall() && ! ll.isExplored(robpos.next(robori.rotate(true)))) {
						this.turnLeft();
						robori = robori.rotate(true);
						this.moveForward(150);
						robpos = robpos.next(robori);
						robpos = ll.setExplored(robpos, true);
					} else {
						if (! this.rightWall()) {
							this.turnRight();
							robori = robori.rotate(false);
							this.moveForward(150);
							robpos = robpos.next(robori);
							robpos = ll.setExplored(robpos, true);

						}
						else{
							if (! this.frontWall()) {
								this.moveForward(150);
								robpos = robpos.next(robori);
								robpos = ll.setExplored(robpos, true);
							} else {
								if (! this.leftWall()) {
									this.turnLeft();
									robori = robori.rotate(true);
									this.moveForward(150);
									robpos = robpos.next(robori);
									robpos = ll.setExplored(robpos, true);
								} else {
									this.uTurn();
									robori = robori.rotate(true).rotate(true);
									this.moveForward(150);
									robpos = robpos.next(robori);
									robpos = ll.setExplored(robpos, true);
								}
							}
						}
					}
				}
			}
		}
		this.stopMotors();
		ll.setEnd(robpos);
		while(ll.isNotExplored()){
			ll.setWall(robpos, robori.rotate(false), this.rightWall());
			ll.setWall(robpos, robori, this.frontWall());
			ll.setWall(robpos, robori.rotate(true), this.leftWall());
			if (! this.rightWall() && ! ll.isExplored(robpos.next(robori.rotate(false)))) {
				this.turnRight();
				robori = robori.rotate(false);
				this.moveForward(150);
				robpos = robpos.next(robori);
				robpos = ll.setExplored(robpos, true);

			}
			else{
				if (! this.frontWall() && ! ll.isExplored(robpos.next(robori))) {
					this.moveForward(150);
					robpos = robpos.next(robori);
					robpos = ll.setExplored(robpos, true);
				} else {
					if (! this.leftWall() && ! ll.isExplored(robpos.next(robori.rotate(true)))) {
						this.turnLeft();
						robori = robori.rotate(true);
						this.moveForward(150);
						robpos = robpos.next(robori);
						robpos = ll.setExplored(robpos, true);
					} else {
						if (! this.rightWall()) {
							this.turnRight();
							robori = robori.rotate(false);
							this.moveForward(150);
							robpos = robpos.next(robori);
							robpos = ll.setExplored(robpos, true);

						}
						else{
							if (! this.frontWall()) {
								this.moveForward(150);
								robpos = robpos.next(robori);
								robpos = ll.setExplored(robpos, true);
							} else {
								if (! this.leftWall()) {
									this.turnLeft();
									robori = robori.rotate(true);
									this.moveForward(150);
									robpos = robpos.next(robori);
									robpos = ll.setExplored(robpos, true);
								} else {
									this.uTurn();
									robori = robori.rotate(true).rotate(true);
									this.moveForward(150);
									robpos = robpos.next(robori);
									robpos = ll.setExplored(robpos, true);
								}
							}
						}
					}
				}
			}

		}
		this.finish(ll);
	}

	public void action (char toDo) {
		switch (toDo) {
		case 'f':	this.moveForward(150);
		break;
		case 'l': this.turnLeft();
		break;
		case 'r':	this.turnRight();
		break;
		default:	break;
		}
	}

	public void followInstructions (String instructions) {
		for (int i = 0; i <  instructions.length(); i++) {
			this.action(instructions.charAt(i));
		}
	}


	public static void main(String []args) {
		/*
		System.out.println("Avant pathfinding");
	    affiche();
	    trouverES();
	    System.out.println("valeur d'entrée : " + entreeX + "," +entreeY + " valeur de sortie : " + sortieX + "," +sortieY );
	    path();
	    System.out.println("Après pathfinding");
	    affiche();
	    affiche2(ordres());
	    //reset();
	    System.out.println("Après reset");
	    affiche();*/
		MyRobot r = new MyRobot();
		Delay.msDelay(2000);
		/*while (true) {
			LCD.clear();
			System.out.println("DEVANT " + r.frontSensor.getDistance());
			System.out.println("DROUTE " + r.rightSensor.getDistance());
			System.out.println("GAUCHE " + r.leftSensor.getDistance());
			Delay.msDelay(400);
		}*/
		r.explore();/*
		String toDo = new String("fffflflffff");
		r.followInstructions(toDo);*/
	}

	public void finish(LinkedLabyrinth ll) {
		// Sound to play to mean we finished exploration
		Sound.playNote(Sound.XYLOPHONE, 1046, 200);
		Sound.playNote(Sound.XYLOPHONE, 1174, 200);
		Sound.playNote(Sound.XYLOPHONE, 1318, 200);

		// The client (robot) waits for the connection the server will ask for
		BTConnection btc = Bluetooth.waitForConnection();

		// Sound to play to mean we're connected
		Sound.playNote(Sound.XYLOPHONE, 1046, 200);

		// We also use streams like the server
		DataInputStream dis = btc.openDataInputStream();
		DataOutputStream dos = btc.openDataOutputStream();

		// We read a String and send it back
		String llstr = ll.toString();
		try {
			dos.writeUTF(llstr);
			dos.flush();
		} catch (IOException ioe) {
			System.out.println("IO Exception writing String:");
			System.out.println(ioe.getMessage());
			return;
		}
		// Sound to play to mean we've sent the labyrinth
		Sound.playNote(Sound.XYLOPHONE, 1174, 200);

		String path = "";
		try {
			path = dis.readUTF();
			System.out.println(path);
		} catch (IOException ioe) {
			System.out.println("IO Exception receiving String:");
			System.out.println(ioe.getMessage());
			return;
		}
		// Sound to play to mean we've received the path
		Sound.playNote(Sound.XYLOPHONE, 1318, 200);

		this.followInstructions(path);
	}
}
