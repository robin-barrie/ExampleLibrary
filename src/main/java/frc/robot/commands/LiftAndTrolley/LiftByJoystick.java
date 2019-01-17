package frc.robot.commands.LiftAndTrolley;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.Robot;
import frc.robot.subsystems.Lift2018;

public class LiftByJoystick extends Command {

  boolean setPointSet;
  double liftThrottle;
  double stringPotValue;

  static TalonSRX right = Lift2018.lift_right;
  static VictorSPX left = Lift2018.lift_left;

  public LiftByJoystick() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.Lift2018);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    stringPotValue = (Lift2018.lift_right.getSelectedSensorPosition(0));

    SmartDashboard.putNumber("LiftStringPotValue,selected sensor", stringPotValue);

    // liftThrottle = Robot.oi.operatorJoystick.getRawAxis(1); // uses flight throttle to test
    liftThrottle = Robot.oi.operatorJoystick.getThrottleToggle(); // 4 uses flight throttle toggle to test 

    if ((liftThrottle > -.2) && (liftThrottle < .2)) { // Dead band

      liftThrottle = 0;

      if (!setPointSet) {
        Robot.Lift2018.setSetpoint(stringPotValue);
        setPointSet = true;
      }
    } else {
      setPointSet = false;
      Robot.Lift2018.move(liftThrottle); // run lift by %Vbus
      SmartDashboard.putNumber("liftThrottle", Lift2018.lift_right.getMotorOutputPercent());
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
    Robot.Lift2018.move(0); // run lift by %Vbus
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}