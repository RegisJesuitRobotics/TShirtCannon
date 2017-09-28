
package org.usfirst.frc.team3729.robot;

import java.util.Date;

import org.usfirst.frc.team3729.robot.commands.PlaystationController;
import org.usfirst.frc.team3729.robot.commands.TShirtControl;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	
	Date LastPush = new Date();
	Date LastPush2 = new Date();
	Date LastPush3 = new Date();
	Date now = new Date();
	Date now2 = new Date();
	Date now3 = new Date();

	PlaystationController playStation;
	TShirtControl tShirtControl;
	boolean isShooting, isShootEnabled;

	@Override
	public void robotInit() {

		playStation = new PlaystationController(0);
		tShirtControl = new TShirtControl(playStation);
		isShooting = false;

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {
		tShirtControl.valveCheck(false);

		// isShootEnabled = true;
	}

	@Override
	public void teleopPeriodic() {

		double timeBetweenPresses = 300;
		double oversample = 50;

		double timeBetweenPresses2 = 300;
		double timeBetweenPresses3 = 300;
		double oversample2 = 50;
		double oversample3 = 50;
		tShirtControl.TShirtDrive();
		tShirtControl.CannonMovement();
		tShirtControl.charging();

		// SHOOT TIMER

		if (playStation.ButtonL1() && tShirtControl.isTankCharged()) {
				tShirtControl.SHOOT();

		}

		// SPEEDY BOY TIMER
		if (playStation.ButtonShare()) {
			now2 = new Date();
			if (now2.getTime() - LastPush2.getTime() < oversample2) {
				LastPush2 = now2;
			} else if (now2.getTime() - LastPush2.getTime() > timeBetweenPresses2) {
				tShirtControl.FASTButton();
				LastPush2 = now2;
			}

		}
		


		}


	

	@Override
	public void testPeriodic() {

	}
}
