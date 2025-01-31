// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private final Victor victor1 = new Victor(1);
  private final Victor victor3 = new Victor(3);
  private final Victor victor2 = new Victor(2);
  private final Victor victor4 = new Victor(4);
  private final XboxController xbox = new XboxController(0);
  private final DifferentialDrive differentialDrive = new DifferentialDrive(victor1,victor3); 
  //The weird id's are because of a weird glitch where the cans would reset their ids when the robot restarts
  //private final CANSparkMax sparkMax1 = new CANSparkMax(1, MotorType.kBrushless);

  private final CANSparkMax sparkMax3 = new CANSparkMax(53, MotorType.kBrushless);
  //Spark 1: Base / First arm motor, Spark 2: 2nd Arm Motor / Middle Spark 3: Hand 
  private RobotContainer m_robotContainer;
  public double forwardVelocity = 0; 
  public double turnVelocity = 0;
  public double sideVelocity = 0;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    victor1.addFollower(victor2);
    victor3.addFollower(victor4);
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    victor3.setInverted(true);
    differentialDrive.setSafetyEnabled(false);
    m_robotContainer = new RobotContainer();
    SmartDashboard.setDefaultNumber("speed", 0);

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    //sparkMax2.set(.1);

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
    
    if(xbox.getRightTriggerAxis() > .5)
      {
        differentialDrive.arcadeDrive(xbox.getLeftY() * .5, xbox.getRightX() * .5);
      }
      
      if(xbox.getRightTriggerAxis() < .5)
      {
      motorSet();
      if (xbox.getRawButton(1) || xbox.getRawButton(4))
      {
        suckDrive();  
      }
      else 
      {
        differentialDrive.arcadeDrive(forwardVelocity, turnVelocity);
      }
    }
    
    
      
    

        /* 
        if(xbox.getRightTriggerAxis() < .5)
    {
      if(xbox.getLeftX() < .5)
      {
        if(xbox.getLeftTriggerAxis() > .5)
        {
          differentialDrive.arcadeDrive(-xbox.getLeftY(), -xbox.getRightX());
        } 
        else
        {
          differentialDrive.arcadeDrive(-xbox.getLeftY() * .5, -xbox.getRightX() * .5);
        }
      }
      else 
      {
        victor1.set(xbox.getLeftX());
        victor2.set(xbox.getLeftX());
        victor3.set(-xbox.getLeftX());
        victor4.set(-xbox.getLeftX());

      }
      
    }
    else
    {
      differentialDrive.stopMotor();
    }*/
    
  }

public void suckDrive()
{
  victor1.set(-sideVelocity);
  victor3.set(sideVelocity);
  victor2.set(sideVelocity);
  victor4.set(sideVelocity);
}

public void motorSet()
{
  if(xbox.getRawButton(3)) 
  {
     forwardVelocity = SmartDashboard.getNumber("speed", 0);
  }
  else if(xbox.getRawButton(2))
  {
    forwardVelocity = -SmartDashboard.getNumber("speed", 0);
  }
  else
  {
    forwardVelocity = 0;
  }
  //xbox.getRawButton(3);
  
  if(xbox.getRawButton(7)) 
  {
     turnVelocity = SmartDashboard.getNumber("speed", 0);
  }
  else if(xbox.getRawButton(8))
  {
    turnVelocity = -SmartDashboard.getNumber("speed", 0);
  }
  else
  {
    turnVelocity = 0;
  }

  if(xbox.getRawButton(1))
  {
    sideVelocity = 0.4;
  }
  else if(xbox.getRawButton(4))
  {
    sideVelocity = -0.4;
  }
  else
  {
    sideVelocity = 0;
  }
  //xbox.getRawButton(3);
  
}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
