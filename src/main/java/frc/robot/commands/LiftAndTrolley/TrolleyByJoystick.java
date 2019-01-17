package frc.robot.commands.LiftAndTrolley;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Robot;
import frc.robot.subsystems.Trolley2018;

public class TrolleyByJoystick extends Command {

  boolean setPointSet;

  double trolleyThrottle;
  double stringPotValue;

  static TalonSRX right = Trolley2018.trolley_right;

  public TrolleyByJoystick() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.Trolley2018);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    setPointSet = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    stringPotValue = (Trolley2018.trolley_right.getSelectedSensorPosition(0));

    SmartDashboard.putNumber("LiftStringPotValue,selected sensor", stringPotValue);

    //trolleyThrottle = Robot.oi.operatorJoystick.getRawAxis(1); // uses flight throttle to test
    trolleyThrottle = Robot.oi.operatorJoystick.getStickX(); // 1 uses flight throttle to test

    if ((trolleyThrottle > -.2) && (trolleyThrottle < .2)) { // Dead band

      trolleyThrottle = 0;

      if (!setPointSet) {
        Robot.Lift2018.setSetpoint(stringPotValue);
        setPointSet = true;
      }

    } else {

      setPointSet = false;
      Robot.Lift2018.move(trolleyThrottle); // run lift by %Vbus
      SmartDashboard.putNumber("trolleyThrottle", Trolley2018.trolley_right.getMotorOutputPercent());
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.Trolley2018.move(0); // run lift by %Vbus
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}