/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.LiftAndTrolley.LiftByJoystick;

public class Lift2018 extends Subsystem {

	/* --- Motor types --- */
	public static VictorSPX lift_left;
	public static TalonSRX lift_right;

	/* --- Port/Can Numbers --- */

	private final static int liftRight = 8;
	private final static int liftLeft = 9;

	/* --- Set Variables --- */

	private double maxSpeedUp = 0.5;
	private double maxSpeedDown = 0.5;
	private double nominalSpeed = 0;
	private double kF = 0;
	private double kP = 1;
	private double kI = 0;
	private double kD = 0;
	private int allowableError = 0;

	public static int forwardLIFTSoftLimit = 590;
	public static int reverseLIFTSoftLimit = 40;

	protected void initDefaultCommand() {
		 setDefaultCommand(new LiftByJoystick());
	}

	public Lift2018() {

		lift_right = new TalonSRX(liftRight);	// 8
		lift_right.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0); // string pot
		lift_right.setSensorPhase(false);
		lift_right.setInverted(false);
		lift_right.setStatusFramePeriod(0, 0, 0);
		lift_right.setNeutralMode(NeutralMode.Brake);

		lift_left = new VictorSPX(liftLeft);	// 9
		lift_left.follow(lift_right);
		lift_left.setInverted(true);
		lift_left.setNeutralMode(NeutralMode.Brake);

		lift_right.configForwardSoftLimitEnable(false, 0);
		lift_left.configForwardSoftLimitEnable(false, 0);

		lift_right.configReverseSoftLimitEnable(false, 0);
		lift_left.configReverseSoftLimitEnable(false, 0);

		setSoftLimits(forwardLIFTSoftLimit, reverseLIFTSoftLimit);

		/* set the peak and nominal outputs, 12V? means full */
		lift_right.configNominalOutputForward(nominalSpeed, 0);
		lift_right.configNominalOutputReverse(nominalSpeed, 0);
		lift_right.configPeakOutputForward(maxSpeedUp, 0);
		lift_right.configPeakOutputReverse(-maxSpeedDown, 0);

		/*
		 * set the allowable closed-loop error, Closed-Loop output will be neutral
		 * within this range. See Table in Section 17.2.1 for native units per rotation.
		 */
		lift_right.configAllowableClosedloopError(0, allowableError, 0);

		/* set closed loop gains in slot0, typically kF stays zero. */
		lift_right.config_kF(0, kF, 0);
		lift_right.config_kP(0, kP, 0);
		lift_right.config_kI(0, kI, 0);
		lift_right.config_kD(0, kD, 0);

	}

	/**
	 * Sets the position of the lift
	 */
	public void setSetpoint(double pos) {
		lift_right.set(ControlMode.Position, pos);
	}

	/**
	 * Gets the set point of the lift
	 */
	public double getSetpoint() {
		return lift_right.getClosedLoopTarget(0);
	}

	/**
	 * Gets the position of the lift
	 */
	public double getPosition() {
		return lift_right.getSelectedSensorPosition(0);
	}

	/**
	 * Runs the lift by joystick
	 */

	public void move(double power) {
		lift_right.set(ControlMode.PercentOutput, power);
	}

	public void stop() {
		lift_right.set(ControlMode.PercentOutput, 0);
	}

	/**
	 * Set the software limits of the lift
	 */
	public static void setSoftLimits(int forward, int reverse) {
		lift_right.configForwardSoftLimitThreshold(forward, 0);
		lift_right.configReverseSoftLimitThreshold(reverse, 0);
	}

	/**
	 * Debug, turn on/off, need to setup variable somewhere...
	 */
	public void periodic() {
		if (true) {
			SmartDashboard.putNumber("forwardLIFTSoftLimit", forwardLIFTSoftLimit);
			SmartDashboard.putNumber("reverseLIFTSoftLimit", reverseLIFTSoftLimit);
		}
	}
}