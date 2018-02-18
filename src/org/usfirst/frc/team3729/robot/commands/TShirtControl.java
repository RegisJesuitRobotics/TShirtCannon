package org.usfirst.frc.team3729.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TShirtControl implements Runnable {

	boolean tankCharged;

	WPI_TalonSRX L1, L2, L3, R1, R2, R3, TurningMotor, ElevationMotorLeft, ElevationMotorRight;
	PlaystationController playStation;
	DriverStation driverStation;
	Solenoid[] Barrel;
	DoubleSolenoid doubleSolenoid;
	int ActiveBarrel;
	double timeBetweenR1Presses = 10;
	double timeBetweenR1Presses2 = 10;
	double Limiter;
	DigitalInput LeftMotorDigitalInput = new DigitalInput(8);
	DigitalInput RightMotorDigitalInput = new DigitalInput(9);
	Side SideThatsFillingUp;
	boolean startup;

	Servo rcRightYAxis;

	Servo rcLeftXAxis;
	Servo rcLeftYAxis;
	Servo rcChannel5;
	Servo rcChannel6;
	double channel6;
	double rcCannonAngle;

	// true is left false is right
	enum Side {
		LeftSide, RightSide
	};

	enum CannonMovement {
		Up, Down, None
	};

	public TShirtControl(PlaystationController playStation) {
		// Movement Motors. R=Right,L=Left, Number = Distance from front.
		L1 = new WPI_TalonSRX(4);
		L2 = new WPI_TalonSRX(3);
		R1 = new WPI_TalonSRX(2);
		R2 = new WPI_TalonSRX(1);

		playStation = new PlaystationController(0);

		// Cannon Stuff
		TurningMotor = new WPI_TalonSRX(7);
		ElevationMotorLeft = new WPI_TalonSRX(5);
		ElevationMotorRight = new WPI_TalonSRX(6);

		// Solenoid Stuff

		Barrel = new Solenoid[6];
		Barrel[0] = new Solenoid(0);
		Barrel[1] = new Solenoid(1);
		Barrel[2] = new Solenoid(2);
		Barrel[3] = new Solenoid(3);
		Barrel[4] = new Solenoid(4);
		Barrel[5] = new Solenoid(5);
		ActiveBarrel = 0;

		SideThatsFillingUp = Side.LeftSide;// Left Filling
		startup = true;

		doubleSolenoid = new DoubleSolenoid(6, 7);

		// NetworkTable table = NetworkTable.getTable("SmartDashboard");

		rcRightYAxis = new Servo("Left Y Axis", 4, 0.00201015, 0.00100925);
		// rcRightYAxis.setCalibrateTable(table.getSubTable("rcLeftYRaw"));

		rcLeftYAxis = new Servo("Left Y Axis", 0, 0.00199155, 0.0010213499999999999);
		// rcLeftYAxis.setCalibrateTable(table.getSubTable("rcLeftYRaw"));

		rcLeftXAxis = new Servo("Left X Axis", 1, 0.0010213499999999999, 0.0019924);
		// rcLeftXAxis.setCalibrateTable(table.getSubTable("rcLeftXRaw"));

		rcChannel5 = new Servo("Channel 5", 2, 0.00106915, 0.0019852999999999997);
		// rcChannel5.setCalibrateTable(table.getSubTable("rcChannel5Raw"));

		rcChannel6 = new Servo("Channel 6", 3, 0.0009891, 0.0020093); // ,
																		// 0.00106915,
																		// 0.0019852999999999997);
		// rcChannel6.setCalibrateTable(table.getSubTable("rcChannel6Raw"));

		// Code Stuff
		this.playStation = playStation;
		Limiter = 0.3;
		StartTimer();
	}

	public void TShirtDrive() {
		double RightTrigger = playStation.RightTrigger();
		double LeftTrigger = playStation.LeftTrigger();
		double LeftStick = playStation.LeftStickXAxis();
		double Deadzone = 0.1;
		double RightPower;
		double LeftPower;
		double Power;
		double turn = 2 * LeftStick;
		Power = RightTrigger - LeftTrigger;
		if (LeftStick > Deadzone) {

			RightPower = Power - (turn * Power);
			LeftPower = Power;
		} else if (LeftStick < -Deadzone) {

			LeftPower = Power + (turn * Power);
			RightPower = Power;
		} else {
			LeftPower = Power;
			RightPower = Power;
		}
		if (playStation.ButtonShare() == true) {
			FASTButton();
		}

		rcCannonAngle = rcRightYAxis.getValue();
		double rcLeftX = rcLeftXAxis.getValue();
		double rcLeftY = rcLeftYAxis.getValue() * 2;
		double channel5 = rcChannel5.getValue();
		channel6 = rcChannel6.getValue();

		SmartDashboard.putNumber("RightPower", RightPower);
		SmartDashboard.putNumber("LeftPower", LeftPower);
		SmartDashboard.putNumber("LeftTrigger", LeftTrigger);
		SmartDashboard.putNumber("RightTrigger", RightTrigger);
		SmartDashboard.putNumber("LeftStick", LeftStick);
		SmartDashboard.putNumber("Power", Power);
		SmartDashboard.putNumber("effectiveY", Power / 2);
		SmartDashboard.putNumber("effectiveX", LeftStick);

		double rcRightPower = 0;
		double rcLeftPower = 0;

		// delta is -4 from x = 0 to x = 1
		double rcTurn = 2 * rcLeftX;
		double rcPower = rcLeftY;
		double rcDeadZone = .1;
		if (rcLeftX > rcDeadZone) {
			rcRightPower = rcPower - (rcTurn * rcPower);
			rcLeftPower = rcPower;
		} else {
			if (rcLeftX < -rcDeadZone) {
				rcLeftPower = rcPower + (rcTurn * rcPower);
				rcRightPower = rcPower;
			} else {
				rcLeftPower = rcPower;
				rcRightPower = rcPower;
			}
		}
		SmartDashboard.putNumber("rcPower", rcPower);

		SmartDashboard.putNumber("rcRightY", rcCannonAngle);

		SmartDashboard.putNumber("rcLeftX", rcLeftX);
		SmartDashboard.putNumber("rcLeftY", rcLeftY / 2);

		SmartDashboard.putNumber("rcLeftPower", rcLeftPower);
		SmartDashboard.putNumber("rcRightPower", rcRightPower);
		SmartDashboard.putNumber("channel5", channel5);
		SmartDashboard.putNumber("channel6", channel6);
		if (channel6 > -.8) {
			R1.set(-RightPower * Limiter);
			R2.set(-RightPower * Limiter);

			L1.set(LeftPower * Limiter);
			L2.set(LeftPower * Limiter);
		} else {
			R1.set(-rcRightPower * Limiter);
			R2.set(-rcRightPower * Limiter);

			L1.set(rcLeftPower * Limiter);
			L2.set(rcLeftPower * Limiter);
			if (channel5 > .7 && isTankCharged()) {
				SHOOT();
			}
		}
		// System.out.println(Limiter);

	}

	public Servo getRcChannel6() {
		return rcChannel6;
	}

	public void CannonMovement() {
		// Horizontal Movement
		if (playStation.ButtonSquare() == true) {
			TurningMotor.set(1);
			System.out.println("Turning");

		} else if (playStation.ButtonCircle() == true) {
			TurningMotor.set(-1);
			System.out.println("turning 2");

		} else {
			TurningMotor.set(0);
		}

		CannonMovement cannonMovement = CannonMovement.None;

		if (channel6 > -.8) {
			if (playStation.ButtonTriangle() == true) {
				cannonMovement = CannonMovement.Up;

			} else if (playStation.ButtonX() == true) {
				cannonMovement = CannonMovement.Down;
			}
		} else {
			if (rcCannonAngle > .5) {
				cannonMovement = CannonMovement.Up;
			} else {
				if (rcCannonAngle < -.5) {
					cannonMovement = CannonMovement.Down;
				}
			}
		}
		SmartDashboard.putString("CannonMovement", cannonMovement.toString());
		// Vertical Movement

		switch (cannonMovement) {
		case Up:
			System.out.println(RightMotorDigitalInput.get());
			System.out.println(LeftMotorDigitalInput.get());
			ElevationMotorLeft.set(1);
			ElevationMotorRight.set(-1);
			break;
		case Down:
			System.out.println(RightMotorDigitalInput.get());
			System.out.println(LeftMotorDigitalInput.get());
			ElevationMotorLeft.set(-1);
			ElevationMotorRight.set(1);
			break;
		default:
			ElevationMotorLeft.set(0);
			ElevationMotorRight.set(0);
			break;
		}
	}

	public void charging() {

	}

	public void SHOOT() {

		// if (playStation.ButtonL1() == true) {
		// now2 = new Date();
		// if (now2.getTime() - LastPush2.getTime() > 100) {
		// if (ActiveBarrel == 5){
		// Barrel[2].set(true);
		// Barrel[5].set(true);
		// }else{
		SmartDashboard.putNumber("fireBarrel", ActiveBarrel);

		System.out.print("test G");
		Barrel[ActiveBarrel].set(false);
		SwitchBarrel();
		StartTimer();
		// }

		// }
		// LastPush2 = now;
		// }
	}

	public void FASTButton() {

		if (Limiter == 1) {
			Limiter = 0.3;
		} else if (Limiter == 0.3) {
			Limiter = 1;

		}
	}

	// DONT PUT THIS CODE IN THE ROBOT.JAVA, ITS ALLREADY CALLED BY
	// SHOOTTIMERANDSHOOT!!!!!! >:(
	public void SwitchBarrel() {
		System.out.println(ActiveBarrel);
		Barrel[ActiveBarrel].set(true);
		// 1634 then 2+5
		if (ActiveBarrel == 0) {
			ActiveBarrel = 5;
			SideThatsFillingUp = Side.LeftSide;
		} else if (ActiveBarrel == 5) {
			ActiveBarrel = 2;
			SideThatsFillingUp = Side.RightSide;
		} else if (ActiveBarrel == 2) {
			ActiveBarrel = 3;
			SideThatsFillingUp = Side.LeftSide;
		} else if (ActiveBarrel == 3) {
			ActiveBarrel = 1;
			SideThatsFillingUp = Side.RightSide;
		} else if (ActiveBarrel == 1) {
			ActiveBarrel = 4;
			SideThatsFillingUp = Side.LeftSide;
		} else if (ActiveBarrel == 4) {
			ActiveBarrel = 0;
			SideThatsFillingUp = Side.RightSide;
		}

	}

	public void valveCheck(boolean check) {

		Barrel[0].set(check);
		Barrel[1].set(check);
		Barrel[2].set(check);
		Barrel[3].set(check);
		Barrel[4].set(check);
		Barrel[5].set(check);

	}

	@Override
	public void run() {
		System.out.println("Runnable Start");
		try {
			setTankCharged(false);
			if (SideThatsFillingUp == Side.LeftSide || startup) {
				// If the left side is shooting
				doubleSolenoid.set(Value.kForward);
				System.out.println("Charging " + SideThatsFillingUp);
				Thread.sleep(5000);

			}
			if (SideThatsFillingUp == Side.RightSide || startup) {
				// If right side is shooting
				doubleSolenoid.set(Value.kReverse);
				System.out.println("Charging " + SideThatsFillingUp);
				Thread.sleep(5000);

			}
			setTankCharged(true);
			startup = false;

			System.out.println("Runnable End");
		} catch (InterruptedException e) {
			e.printStackTrace();

		}

	}

	public boolean isTankCharged() {
		return tankCharged;

	}

	public void setTankCharged(boolean tankCharged) {
		this.tankCharged = tankCharged;
		SmartDashboard.putBoolean("tankCharged", tankCharged);
	}

	public void StartTimer() {
		new Thread(this).start();

	}
	/*
	 * 
	 */
}
