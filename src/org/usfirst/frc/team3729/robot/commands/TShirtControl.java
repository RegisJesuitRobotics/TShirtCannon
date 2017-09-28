package org.usfirst.frc.team3729.robot.commands;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;

public class TShirtControl implements Runnable {

	boolean tankCharged;

	CANTalon L1, L2, L3, R1, R2, R3, TurningMotor, ElevationMotorLeft, ElevationMotorRight;
	PlaystationController playStation;
	DriverStation driverStation;
	Solenoid[] Barrel;
	DoubleSolenoid doubleSolenoid;
	int ActiveBarrel;
	double timeBetweenR1Presses = 10;
	double timeBetweenR1Presses2 = 10;
	double Limiter;
	DigitalInput LeftMotorDigitalInput = new DigitalInput(1);
	DigitalInput RightMotorDigitalInput = new DigitalInput(2);
	Side SideThatsFillingUp;
	boolean startup;

	// true is left false is right
	enum Side {
		LeftSide, RightSide
	};

	public TShirtControl(PlaystationController playStation) {
		// Movement Motors. R=Right,L=Left, Number = Distance from front.
		L1 = new CANTalon(4);
		L2 = new CANTalon(3);
		R1 = new CANTalon(2);
		R2 = new CANTalon(1);

		playStation = new PlaystationController(0);

		// Cannon Stuff
		TurningMotor = new CANTalon(7);
		ElevationMotorLeft = new CANTalon(5);
		ElevationMotorRight = new CANTalon(6);

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
		R1.set(-RightPower * Limiter);
		R2.set(-RightPower * Limiter);

		L1.set(LeftPower * Limiter);
		L2.set(LeftPower * Limiter);

		// System.out.println(Limiter);

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

		// Vertical Movement
		if (playStation.ButtonTriangle() == true) {

			System.out.println(RightMotorDigitalInput.get());
			System.out.println(LeftMotorDigitalInput.get());
			ElevationMotorLeft.set(1);
			ElevationMotorRight.set(-1);

		} else if (playStation.ButtonX() == true) {
			System.out.println(RightMotorDigitalInput.get());
			System.out.println(LeftMotorDigitalInput.get());
			ElevationMotorLeft.set(-1);
			ElevationMotorRight.set(1);

		} else {
			ElevationMotorLeft.set(0);
			ElevationMotorRight.set(0);
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
		SmartDashboard.putBoolean("Dashboard Charging", tankCharged);
	}

	public void StartTimer() {
		new Thread(this).start();

	}
	/*
	 * 
	 */
}
