package utilities;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class PrintLightSensor {

	public static void main(String[] args) {
		int max, min, tot, nbr;
		LightSensor light = new LightSensor(SensorPort.S1);
		int cur = light.getNormalizedLightValue();
		max = cur;
		min = cur;
		tot = cur;
		nbr = 1;
		while (true) {
			cur = light.getNormalizedLightValue();
			if (cur > max)
				max = cur;
			if (cur < min)
				min = cur;
			tot = tot + cur;
			nbr++;
			print(cur, min, max, tot / nbr);
			Delay.msDelay(100);
		}
	}
	
	private static void print(int cur, int min, int max, int avg) {
		LCD.clear();
		LCD.drawString("cur", 0, 0);
		LCD.drawInt(cur, 4, 0);
		LCD.drawString("min", 0, 1);
		LCD.drawInt(min, 4, 1);
		LCD.drawString("max", 0, 2);
		LCD.drawInt(max, 4, 2);
		LCD.drawString("avg", 0, 3);
		LCD.drawInt(avg, 4, 3);
		LCD.refresh();
	}
}
