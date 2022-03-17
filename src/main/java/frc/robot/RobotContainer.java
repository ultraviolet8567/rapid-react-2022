package frc.robot;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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

    // Shuffleboard tabs
    public final ShuffleboardTab driveSettings = Shuffleboard.getTab("Drive Settings");
    public final ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");
    public final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
    public final ShuffleboardTab collectionTab = Shuffleboard.getTab("Collection");
    public final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
    public final ShuffleboardTab hangerTab = Shuffleboard.getTab("Hanger");

    // Subsystems
    public final Shooter m_shooter = new Shooter();
    public final Drivetrain m_drivetrain = new Drivetrain();
    public final Collection m_collection = new Collection();
    public final Hanger m_hanger = new Hanger();

    // Joysticks
    private final XboxController xboxController = new XboxController(0);

    // Cameras
    public UsbCamera frontCamera = CameraServer.startAutomaticCapture(0);
    public UsbCamera backCamera = CameraServer.startAutomaticCapture(1);

    // Autonomous chooser
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    // The container for the robot.  Contains subsystems, OI devices, and commands
    private RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        // Configure default commands
        m_drivetrain.setDefaultCommand(new Drive(m_drivetrain));
        m_shooter.setDefaultCommand(new ShootLowerHub(m_shooter));
        
        // Configure autonomous sendable chooser and send to Shuffleboard
        m_chooser.addOption("Drive out auto", new AutoDriveOut(m_drivetrain));
        m_chooser.setDefaultOption("One ball auto", new AutoOneBall(m_drivetrain, m_collection, m_shooter));
        m_chooser.addOption("Two ball auto", new AutoTwoBall(m_drivetrain, m_collection, m_shooter));
        autoTab.add("Auto mode", m_chooser).withWidget(BuiltInWidgets.kComboBoxChooser)
            .withSize(2, 1);
        
        // Send camera to ShuffleBoard
        cameraTab.add("Front camera", frontCamera).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(5, 4);
        cameraTab.add("Back camera", backCamera).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(5, 4);
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

        final JoystickButton startButton = new JoystickButton(xboxController, XboxController.Button.kStart.value);
        startButton.toggleWhenPressed(new StopFlywheels(m_shooter), true);

        final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);        
        rightBumper.toggleWhenPressed(new Collect(m_collection), true);

        final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);        
        leftBumper.toggleWhenPressed(new Shoot(m_shooter, m_collection), true);

        final JoystickButton buttonY = new JoystickButton(xboxController, XboxController.Button.kY.value);        
        buttonY.whileHeld(new ExtendHanger(m_hanger), true);

        final JoystickButton buttonA = new JoystickButton(xboxController, XboxController.Button.kA.value);        
        buttonA.whileHeld(new RetractHanger(m_hanger), true);

        final JoystickButton buttonX = new JoystickButton(xboxController, XboxController.Button.kX.value);
        buttonX.whenPressed(new ShootFender(m_shooter), true);

        final JoystickButton buttonB = new JoystickButton(xboxController, XboxController.Button.kB.value);
        buttonB.whenPressed(new ShootDistance(m_shooter), true);

        final POVButton up = new POVButton(xboxController, 0);
        up.whileHeld(new IncreaseV(), true);

        final POVButton down = new POVButton(xboxController, 180);
        down.whileHeld(new DecreaseV(), true);

        final POVButton left = new POVButton(xboxController, 270);
        left.whileHeld(new DecreaseH(), true);

        final POVButton right = new POVButton(xboxController, 90);
        right.whileHeld(new IncreaseH(), true);
    }

    public static RobotContainer getInstance() {
        return m_robotContainer;
    }

    public XboxController getXboxController() {
        return xboxController;
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
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
