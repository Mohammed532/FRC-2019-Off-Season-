/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//shoelace
package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;

//Camera Packages
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot { 
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final XboxController xbox = new XboxController(1);
  double speedMod = 0.6;
  private final DifferentialDrive r_Drive = new DifferentialDrive(new Spark (0), new Spark (1));
  //private final DifferentialDrive intake = new DifferentialDrive(new Spark(2), new Spark (3));
  //Spark(0) and Spark(1) are for driving
  private final DifferentialDrive r_intake = new Spark(4);
  private static Ultrasonic GoalSensor = new Ultrasonic(0, 1);

  //Camera Setup
  final int IMG_HEIGHT = 340;
  final int IMG_WIDTH = 340;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);*/

    UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();

    //Turns on Ultrasonic sensor    
    GoalSensor.setAutomaticMode(true);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
     autoSelected = SmartDashboard.getString("Auto Selector",
     defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);*/
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }*/

    //Ultrasonic (Auto)
    double a_GoalSensorValue = GoalSensor.getRangeInches();

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double lXAxis = xbox.getRawAxis(0);
    double lYAxis = xbox.getRawAxis(1);
    r_Drive.arcadeDrive (lYAxis, lXAxis);
    
    if(xbox.getBumper(Hand.kRight)){
      r_intake.arcadeDrive(-0.5,0.0); //*Intake 
    }
  }
      
    


    
    //Ultrasonic (Teleop)
    double t_GoalSensorValue = GoalSensor.getRangeInches(); //Checks how far sensor is from an object



  }
  public double getspeedMod(XboxController xbox){

    boolean ybutton = xbox.getYButton();
    boolean bbutton = xbox.getBButton();
    boolean abutton = xbox.getAButton();
    if(ybutton){
    return 0.75;  
    }
    if(bbutton){
    return 0.5;  
    }
    if(abutton){
    return 0.25;  
    }
    double speedMod2 = speedMod;
    return speedMod2;
  }
  
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  
  }



}
