package frc.robot;

import java.util.Map;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Shooter.Velocity;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

    private Command m_autonomousCommand;
    private RobotContainer m_robotContainer;

    private NetworkTableEntry postTime;

    private NetworkTableEntry voltage;
    private NetworkTableEntry current;
    private NetworkTableEntry shooterOn;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer. This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);

        postTime = Shuffleboard.getTab("Match Data").add("Time left", 0).withWidget(BuiltInWidgets.kNumberBar)
            .withProperties(Map.of("min", 0, "max", 135))
            .withPosition(0, 0)
            .withSize(2, 1)
            .getEntry();
        
        voltage = Shuffleboard.getTab("Power").add("Battery Voltage", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "V"))
            .withPosition(0, 0)
            .getEntry();
         
        current = Shuffleboard.getTab("Power").add("Battery Current", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "Amps"))
            .withPosition(3, 0)
            .getEntry();

        // Shooter on message
        shooterOn = Shuffleboard.getTab("Match Data").add("Shooter on", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withSize(1, 1)
            .withPosition(2, 0)
            .getEntry();

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    *
    * This runs after the mode specific periodic functions, but before
    * LiveWindow and SmartDashboard integrated updating.
    */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        postTime.setNumber(Timer.getMatchTime());

        CommandScheduler.getInstance().run();

        voltage.setDouble(RobotContainer.getInstance().pdp.getVoltage());
        current.setDouble(RobotContainer.getInstance().pdp.getTotalCurrent());
        shooterOn.setBoolean(RobotContainer.getInstance().m_shooter.getVelocity() != Velocity.OFF);
    }


    // This function is called once each time the robot enters Disabled mode
    @Override
    public void disabledInit() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);
    }

    @Override
    public void disabledPeriodic() {
    }

    // This autonomous runs the autonomous command selected by your {@link RobotContainer} class
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(0);
    }

    // This function is called periodically during autonomous
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(0);
    }

    // This function is called periodically during operator control
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode
        CommandScheduler.getInstance().cancelAll();
    }

    // This function is called periodically during test mode
    @Override
    public void testPeriodic() {
    }

}
