package org.usfirst.frc.team3729.robot;

import org.usfirst.frc.team3729.robot.commands.TShirtControl;

public class NoDriveStation implements Runnable {
	Robot robot;
	TShirtControl tShirtControl;
	double channel6Value;
	
	public NoDriveStation(Robot robot) {
		super();
		this.robot = robot;
		this.tShirtControl = robot.tShirtControl;
		channel6Value = tShirtControl.getRcChannel6().getValue();
	}


	@Override
	public void run() {
		System.out.println("NoDriveStation Run " + channel6Value);
		
		while (channel6Value > -.5) {
			if (channel6Value > 0) {
				System.out.println("Run Cannon without Drivestation");
				robot.runCannon();
			}
			channel6Value = tShirtControl.getRcChannel6().getValue();
		}
	}

}
