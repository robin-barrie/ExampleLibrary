package frc.robot.commands.Chassis;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.Robot;
import frc.robot.subsystems.Chassis;

/**
 * 
 */
public class DriveByJoystickAndVision extends Command {

  // Gets the joystick to be used from OI.java
  private Joystick driverJoystick = Robot.oi.driverJoystick;

  private double turnConstant, turn, speed;
  private double tx, ta, ty, tv;
  private double target;

  // CONSTRUCTOR
  public DriveByJoystickAndVision() {

    // VARIABLE_SETTING

    // REQUIRES
    requires(Robot.Chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    turnConstant = 0.075;
    target = 0.03;
    Robot.Chassis.setBrakeMode(NeutralMode.Brake);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0); // has valid target
    tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0); // tx horizontal offset degrees from crosshair +-27 degrees
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0); // ty vertical offset from crosshair +-20.5 degrees.
    ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0); // target % of image

    speed = Robot.oi.driverJoystick.getRawAxis(1); // Left y

    if (true) { // tv == 1.0
      turn = tx * turnConstant;
      speed = speed * ((target - ta) / target);
    } else {
      turn = driverJoystick.getRawAxis(4); // Right x
    }

    boolean squaredInputs = true;

    Chassis.talonDrive.arcadeDrive(0.0, turn, squaredInputs);

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Chassis.talonDrive.arcadeDrive(0, 0, false);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}