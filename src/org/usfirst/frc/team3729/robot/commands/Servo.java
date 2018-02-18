package org.usfirst.frc.team3729.robot.commands;

import java.text.NumberFormat;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.tables.ITable;

public class Servo extends DigitalInput {
	protected double topValue = 0.002030; // 19910994107646854934;
	protected double zeroValue = 0.00153; // 557470769453002;
	protected double bottomValue = 0.000984; // 8307708546873221;

	protected double topDelta = topValue - zeroValue;
	protected double bottomDelta = zeroValue - bottomValue;
	protected static NumberFormat percentFormat = NumberFormat.getPercentInstance();

	protected double maxValue, minValue = 1;
	ITable calibrateTable;

	Counter upPeriod;
	String name;

	public Servo(String name, int channel) {
		super(channel);
		this.name = name;
		upPeriod = new Counter(this);
		upPeriod.setSemiPeriodMode(true);
	}

	public Servo(String name, int channel, double topValue, double bottomValue) {
		this(name, channel);
		this.topValue = topValue;
		this.bottomValue = bottomValue;
		zeroValue = topValue - (topValue - bottomValue) / 2;
		topDelta = topValue - zeroValue;
		bottomDelta = zeroValue - bottomValue;

	}

	public void setCalibrateTable(ITable calibrateTable) {
		this.calibrateTable = calibrateTable;
		calibrateTable.putNumber("channel", getChannel());
		calibrateTable.putString("name", name);
	}

	public String getValueString() {
		return percentFormat.format(getValue());
	}

	public double getValue() {
		double raw = getRaw();
		double value = 0;
		if (raw >= zeroValue) {
			value = (raw - zeroValue) / topDelta;
		} else {
			value = (raw - zeroValue) / bottomDelta;
		}
		return value;
	}

	public double getRaw() {
		double rawValue = upPeriod.getPeriod();
		if (!Double.isFinite(rawValue)) {
			rawValue = zeroValue;
		}
		if (rawValue > maxValue) {
			maxValue = rawValue;
			if (calibrateTable != null) {
				calibrateTable.putNumber("maxValue", maxValue);
			}
		}
		if (rawValue < minValue) {
			minValue = rawValue;
			if (calibrateTable != null) {
				calibrateTable.putNumber("minValue", minValue);
			}
		}
		return rawValue;
	}

	@Override
	public String toString() {
		return name + ":" + getValueString();
	}

}
