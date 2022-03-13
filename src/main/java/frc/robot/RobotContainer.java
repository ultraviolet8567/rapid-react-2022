package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;


/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private static RobotContainer m_robotContainer = new RobotContainer();

    // The robot's subsystems
    public final Shooter m_shooter = new Shooter();
    public final Drivetrain m_drivetrain = new Drivetrain();
    public final Collection m_collection = new Collection();
    public final Hanger m_hanger = new Hanger();

    // Joysticks
    private final XboxController xboxController = new XboxController(0);

    // A chooser for autonomous commands
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    // The container for the robot.  Contains subsystems, OI devices, and commands
    private RobotContainer() {
        // SmartDashboard Buttons
        // SmartDashboard.putData("Commands/Drive Out Auto", new AutoDriveOut());
        // SmartDashboard.putData("Commands/One Ball Auto", new AutoOneBall());
        // SmartDashboard.putData("Commands/Drive", new Drive(m_drivetrain));
        // SmartDashboard.putData("Commands/DriveToggle", new DriveToggle(m_drivetrain));
        // SmartDashboard.putData("Commands/Collect", new Collect(m_collection));
        // SmartDashboard.putData("Commands/Shoot", new Shoot(m_shooter));
        // SmartDashboard.putData("Commands/ExtendHanger", new ExtendHanger(m_hanger));
        // SmartDashboard.putData("Commands/RetractHanger", new RetractHanger(m_hanger));
        // SmartDashboard.putData("Commands/FlywheelOneIncrease", new IncreaseBigFlywheel(m_shooter));
        // SmartDashboard.putData("Commands/FlywheelOneDecrease", new DecreaseBigFlywheel(m_shooter));
        // SmartDashboard.putData("Commands/FlywheelTwoIncrease", new IncreaseSmallFlywheel(m_shooter));
        // SmartDashboard.putData("Commands/FlywheelTwoDecrease", new DecreaseSmallFlywheel(m_shooter));
        
        // Configure the button bindings
        configureButtonBindings();

        // Configure default commands
        m_drivetrain.setDefaultCommand(new Drive(m_drivetrain));
        // m_shooter.setDefaultCommand(new RunFlywheel(m_shooter));
        
        // Configure autonomous sendable chooser
        m_chooser.setDefaultOption("Drive out auto", new AutoDriveOut());
        m_chooser.addOption("One ball auto", new AutoOneBall());
        
        SmartDashboard.putData("Auto Mode", m_chooser);
    }

    public static RobotContainer getInstance() {
        return m_robotContainer;
    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        // Create some buttons
        final JoystickButton backButton = new JoystickButton(xboxController, XboxController.Button.kBack.value);
        backButton.whenPressed(new DriveToggle(m_drivetrain), true);

        final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);        
        rightBumper.toggleWhenPressed(new Collect(m_collection), true);

        final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);        
        leftBumper.toggleWhenPressed(new Shoot(m_shooter, m_collection), true);

        final JoystickButton yButton = new JoystickButton(xboxController, XboxController.Button.kY.value);        
        yButton.whileHeld(new ExtendHanger(m_hanger), true);

        final JoystickButton aButton = new JoystickButton(xboxController, XboxController.Button.kA.value);        
        aButton.whileHeld(new RetractHanger(m_hanger), true);

        final POVButton up = new POVButton(xboxController, 0);
        up.whileHeld(new IncreaseV(), true);

        final POVButton down = new POVButton(xboxController, 180);
        down.whileHeld(new DecreaseV(), true);

        final POVButton left = new POVButton(xboxController, 270);
        left.whileHeld(new DecreaseH(), true);

        final POVButton right = new POVButton(xboxController, 90);
        right.whileHeld(new IncreaseH(), true);

        final JoystickButton buttonX = new JoystickButton(xboxController, XboxController.Button.kX.value);
        buttonX.whenPressed(new ShootFender(m_shooter), true);

        final JoystickButton buttonB = new JoystickButton(xboxController, XboxController.Button.kB.value);
        buttonB.whenPressed(new ShootDistance(m_shooter), true);
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public XboxController getXboxController() {
        return xboxController;
    }
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // The selected command will be run in autonomous
        return m_chooser.getSelected();
    }

    public static SparkMaxPIDController getDefaultPIDController(CANSparkMax motor) {
        SparkMaxPIDController pid_controller = motor.getPIDController();

        pid_controller.setP(6e-5);
        pid_controller.setI(0);
        pid_controller.setD(0);
        pid_controller.setOutputRange(-1, 1);

        return pid_controller;
    }
}
