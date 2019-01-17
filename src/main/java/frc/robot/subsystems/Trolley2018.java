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

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.LiftAndTrolley.TrolleyByJoystick;

public class Trolley2018 extends Subsystem {

  /* --- Motor type --- */
  public static TalonSRX trolley_right;

  /* --- Port/Can Number --- */

  private final static int trolleyRight = 4;

  /* --- Set Variables --- */

  public double maxSpeedUp = 0.5;
  private double maxSpeedDown = 0.5;
  private double nominalSpeed = 0;
  private double kF = 0;
  private double kP = 1;
  private double kI = 0.001;
  private double kD = 0;
  private int allowableError = 0;

  public static int forwardTrolleySoftLimit = 585;
  public static int reverseTrolleySoftLimit = 50;

  protected void initDefaultCommand() {
    setDefaultCommand(new TrolleyByJoystick());
  }

  public Trolley2018() {

    trolley_right = new TalonSRX(trolleyRight); // 4
    trolley_right.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0); // string pot
    trolley_right.setSensorPhase(false);
    trolley_right.setInverted(false);
    trolley_right.setStatusFramePeriod(0, 0, 0);
    trolley_right.setNeutralMode(NeutralMode.Brake);
    trolley_right.configForwardSoftLimitEnable(false, 0);
    trolley_right.configReverseSoftLimitEnable(false, 0);

    setSoftLimits(forwardTrolleySoftLimit, reverseTrolleySoftLimit);

    /* set the peak and nominal outputs, 12V? means full */
    trolley_right.configNominalOutputForward(nominalSpeed, 0);
    trolley_right.configNominalOutputReverse(-nominalSpeed, 0);
    trolley_right.configPeakOutputForward(maxSpeedUp, 0);
    trolley_right.configPeakOutputReverse(-maxSpeedDown, 0);

    /*
     * set the allowable closed-loop error, Closed-Loop output will be neutral
     * within this range. See Table in Section 17.2.1 for native units per rotation.
     */
    trolley_right.configAllowableClosedloopError(0, allowableError, 0);

    /* set closed loop gains in slot0, typically kF stays zero. */
    trolley_right.config_kF(0, kF, 0);
    trolley_right.config_kP(0, kP, 0);
    trolley_right.config_kI(0, kI, 0);
    trolley_right.config_kD(0, kD, 0);
  }

  /**
   * Sets the position of the trolley
   */
  public void setSetpoint(double pos) {
    trolley_right.set(ControlMode.Position, pos);
  }

  /**
   * Gets the set point of the trolley
   */
  public double getSetpoint() {
    return trolley_right.getClosedLoopTarget(0);
  }

  /**
   * Gets the set point error of the trolley
   */
  public double getError() {
    return trolley_right.getClosedLoopError(0);
  }

  /**
   * Gets the position of the trolley
   */
  public double getPosition() {
    return trolley_right.getSelectedSensorPosition(0);
  }

  /**
   * Runs the trolley by power input(JOYSTICK)
   * 
   * @param power Percentage of power given to the motors to move the trolley
   *              (Value of -1 TO 1)
   */
  public void move(double power) {
    trolley_right.set(ControlMode.PercentOutput, power);
  }

  public void stop() {
    trolley_right.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Set the soft limits of the trolley Soft limits are position limits that are
   * envoked through the talons, and are not physical stops
   * 
   * @param forward encoder value in which the talon keeps the system from going
   *                past (-4096 - 4096)
   * @param reverse encoder value in which the talon keeps the system from going
   *                past (-4096 - 4096)
   */
  public static void setSoftLimits(int forward, int reverse) {
    trolley_right.configForwardSoftLimitThreshold(forward, 0);
    trolley_right.configReverseSoftLimitThreshold(reverse, 0);
  }

  /**
   * Runs the code while this subsystem is in use Debug, turn on/off
   */
  public void periodic() {
    if (true) {
      SmartDashboard.putNumber("forwardTROLLEYSoftLimit", forwardTrolleySoftLimit);
      SmartDashboard.putNumber("trolley POSITION", this.getPosition());
      SmartDashboard.putNumber("reverseTROLLEYSoftLimit", reverseTrolleySoftLimit);
    }
  }
}