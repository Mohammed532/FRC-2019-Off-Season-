/*---------------------------------------------------------------------------------------*/                                                                            
/*            66666666       222222222222222         1111111       333333333333333       */
/*           6::::::6       2:::::::::::::::22      1::::::1      3:::::::::::::::33     */
/*          6::::::6        2::::::222222:::::2    1:::::::1      3::::::33333::::::3    */
/*         6::::::6         2222222     2:::::2    111:::::1      3333333     3:::::3    */
/*        6::::::6                      2:::::2       1::::1                  3:::::3    */
/*       6::::::6                       2:::::2       1::::1                  3:::::3    */
/*      6::::::6                     2222::::2        1::::1          33333333:::::3     */
/*     6::::::::66666           22222::::::22         1::::l          3:::::::::::3      */
/*    6::::::::::::::66       22::::::::222           1::::l          33333333:::::3     */
/*    6::::::66666:::::6     2:::::22222              1::::l                  3:::::3    */
/*    6:::::6     6:::::6   2:::::2                   1::::l                  3:::::3    */
/*    6:::::6     6:::::6   2:::::2                   1::::l                  3:::::3    */
/*    6::::::66666::::::6   2:::::2       222222   111::::::111   3333333     3:::::3    */
/*     66:::::::::::::66    2::::::2222222:::::2   1::::::::::1   3::::::33333::::::3    */
/*       66:::::::::66      2::::::::::::::::::2   1::::::::::1   3:::::::::::::::33     */
/*         666666666        22222222222222222222   111111111111    333333333333333       */
/*                                                                                    ©  */
/*---------------------------------------------------------------------------------------*/

package frc.robot;

import frc.autonomous.Auto;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SolenoidBase;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer; //Timer Package
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Ultrasonic; //Utrasonic Packege
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;

//Camera Packages
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;


public class Robot extends IterativeRobot { 
  private static final String kDefaultAuto = "Default";
  private static final String showcaseAuto = "Showcase Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final XboxController xbox = new XboxController(0);
  private final DifferentialDrive r_Drive = new DifferentialDrive(new Spark (0), new Spark (1));
  //Spark(0) and Spark(1) are for driving
  private final DifferentialDrive intake = new DifferentialDrive(new Spark(2), new Spark (3));
  private static Ultrasonic GoalSensor = new Ultrasonic(0, 1); //trig, echo
  private static final DoubleSolenoid catSol = new DoubleSolenoid(0, 1);
  double speedMod;
  //Timer Object(s)
  private static Timer clockwork = new Timer();

  //Custom Objects
  private final Catapult catapult = new Catapult(catSol);
  private final Auto auto = new Auto();

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
    m_chooser.addOption("My Auto", showcaseAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();

    //Turns on Ultrasonic sensor    
    GoalSensor.setAutomaticMode(true);

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    clockwork.reset();
    clockwork.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case showcaseAuto:
        auto.showcase(r_Drive, clockwork);
        break;
      case kDefaultAuto:
      default:
        auto.aDefault(r_Drive, clockwork);
        break;
    }

  }

  @Override
  public void teleopInit(){
    SmartDashboard.putNumber("Ultrasonic Distance Value", GoalSensor.getRangeInches());
    SmartDashboard.putBoolean("Ultrasonic Status", GoalSensor.isEnabled());
  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putNumber("Ultrasonic Distance Value", GoalSensor.getRangeInches());
    speedMod = getspeedMod(xbox);

    double lXAxis = xbox.getRawAxis(0);
    double lYAxis = xbox.getRawAxis(1);
    
    //Ultrasonic (Teleop)
    double t_GoalSensorValue = GoalSensor.getRangeInches(); //Checks how far sensor is from an object
    
    r_Drive.arcadeDrive(lYAxis * speedMod, lXAxis * speedMod); 

    r_intake();

    if(xbox.getAButton()){
      catapult.launch();
    } 

    if(xbox.getStartButton()){ 
      catapult.e_pull(); //Emergency Button
    } 
  }
  
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

    catSol.set(DoubleSolenoid.Value.kOff);

    
    catSol.set(DoubleSolenoid.Value.kForward);
    catSol.set(DoubleSolenoid.Value.kReverse);
  }

  //Changes the max speed of robot for optional controlling
  public double getspeedMod(XboxController xbox){
    speedMod = 0.6;
    boolean ybutton = xbox.getYButton();
    boolean bbutton = xbox.getBButton();
    boolean xbutton = xbox.getXButton();

    if(xbutton){
      speedMod = 0.75;  
    }
    if(ybutton){
      speedMod = 0.5;  
    }
    if(bbutton){
      speedMod = 0.25;  
    }
    
      return speedMod;
  }

  public void r_intake(){
    if(xbox.getBumper(Hand.kRight)){
      intake.arcadeDrive(-0.8, 0); //*Intake 
    }
    if(xbox.getBumper(Hand.kLeft)){
      intake.stopMotor();
    }
  }
  

}

/*---------------------------------------------------------------------------------------*/                                                                            
/*            66666666       222222222222222         1111111       333333333333333       */
/*           6::::::6       2:::::::::::::::22      1::::::1      3:::::::::::::::33     */
/*          6::::::6        2::::::222222:::::2    1:::::::1      3::::::33333::::::3    */
/*         6::::::6         2222222     2:::::2    111:::::1      3333333     3:::::3    */
/*        6::::::6                      2:::::2       1::::1                  3:::::3    */
/*       6::::::6                       2:::::2       1::::1                  3:::::3    */
/*      6::::::6                     2222::::2        1::::1          33333333:::::3     */
/*     6::::::::66666           22222::::::22         1::::l          3:::::::::::3      */
/*    6::::::::::::::66       22::::::::222           1::::l          33333333:::::3     */
/*    6::::::66666:::::6     2:::::22222              1::::l                  3:::::3    */
/*    6:::::6     6:::::6   2:::::2                   1::::l                  3:::::3    */
/*    6:::::6     6:::::6   2:::::2                   1::::l                  3:::::3    */
/*    6::::::66666::::::6   2:::::2       222222   111::::::111   3333333     3:::::3    */
/*     66:::::::::::::66    2::::::2222222:::::2   1::::::::::1   3::::::33333::::::3    */
/*       66:::::::::66      2::::::::::::::::::2   1::::::::::1   3:::::::::::::::33     */
/*         666666666        22222222222222222222   111111111111    333333333333333       */
/*                                                                                    ©  */
/*---------------------------------------------------------------------------------------*/

