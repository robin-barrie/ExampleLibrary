package frc.robot.subsystems;

//import frc.robot.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.LED.LEDRuntime;

/**
 * 
 */
public class LED extends Subsystem {



  public LED() {

  }

  // Set the default command for a subsystem here.

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new LEDRuntime());
  }

}