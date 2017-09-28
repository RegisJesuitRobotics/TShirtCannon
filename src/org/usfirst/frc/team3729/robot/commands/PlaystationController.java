
package org.usfirst.frc.team3729.robot.commands;

import edu.wpi.first.wpilibj.Joystick;

public class PlaystationController extends Joystick {
	public PlaystationController(int port) {
		super(port);
	}

	public boolean ButtonSquare() {
		return super.getRawButton(1);
	}

	public boolean ButtonX() {
		return super.getRawButton(2);
	}

	public boolean ButtonCircle() {
		return super.getRawButton(3);
	}

	public boolean ButtonTriangle() {
		return super.getRawButton(4);
	}

	public boolean ButtonL1() {
		return super.getRawButton(5);
	}

	public boolean ButtonR1() {
		return super.getRawButton(6);
	}

	public boolean ButtonL2() {
		return super.getRawButton(7);
	}

	public boolean ButtonR2() {
		return super.getRawButton(8);
	}

	public boolean ButtonShare() {
		return super.getRawButton(9);
	}

	public boolean ButtonOptions() {
		return super.getRawButton(10);
	}

	public boolean ButtonL3() {
		return super.getRawButton(11);
	}

	public boolean ButtonR3() {
		return super.getRawButton(12);
	}

	public boolean ButtonPS() {
		return super.getRawButton(13);
	}

	public boolean ButtonTouchscreen() {
		return super.getRawButton(14);
	}

	public double LeftStickXAxis() {
		return super.getRawAxis(0);
	}

	public double LeftStickYAxis() {
		return super.getRawAxis(1);
	}

	public double RightStickXAxis() {
		return super.getRawAxis(2);
	}

	public double RightStickYAxis() {
		return super.getRawAxis(5);
	}

	public double LeftTrigger() {
		return super.getRawAxis(3);
	}

	public double RightTrigger() {
		return super.getRawAxis(4);
	}
}
