package org.usfirst.frc.team3729.robot;

import org.usfirst.frc.team3729.robot.commands.TShirtControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NoDriveStation implements Runnable {
	Robot robot;
	TShirtControl tShirtControl;
	double channel6Value;
	
	public NoDriveStation(Robot robot) {
		super();
		this.robot = robot;
		this.tShirtControl = robot.tShirtControl;
		channel6Value = tShirtControl.getRcChannel6().getValue();
		SmartDashboard.putBoolean("rcControlThreadActive", true);
	}


	@Override
	public void run() {
		System.out.println("NoDriveStation Run " + channel6Value);
		
		while (channel6Value > -.5) {
			channel6Value = tShirtControl.getRcChannel6().getValue();
			if (channel6Value > 0) {
				SmartDashboard.putBoolean("rcInCOntrol", true);
				robot.runCannon();
				System.out.println("Run Cannon without Drivestation");
			}
			else {
				SmartDashboard.putBoolean("rcInCOntrol", false);
			}
		}
		SmartDashboard.putBoolean("rcControlThreadActive", false);
	}

}
