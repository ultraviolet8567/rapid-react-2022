package frc.robot;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.RobotMode;
import frc.robot.commands.*;
import frc.robot.commands.hanger.*;
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
    public final ShuffleboardTab matchTab = Shuffleboard.getTab("Match Data");
    public final ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");
    public final ShuffleboardTab collectionTab = Shuffleboard.getTab("Collection");
    public final ShuffleboardTab hangerTab = Shuffleboard.getTab("Hanger");
    public final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
    public final ShuffleboardTab powerTab = Shuffleboard.getTab("Power");

    // Subsystems
    public final Shooter m_shooter = new Shooter();
    public final Drivetrain m_drivetrain = new Drivetrain();
    public final Collection m_collection = new Collection();
    public final Hanger m_hanger = new Hanger();
    public final Limelight m_limelight = new Limelight();

    // Joysticks
    private final XboxController xboxController = new XboxController(0);
    private final XboxController hangerController = new XboxController(1);

    // Cameras
    public final UsbCamera frontCamera = CameraServer.startAutomaticCapture(0);
    public final UsbCamera jackCamera = CameraServer.startAutomaticCapture(1);
    public final HttpCamera limelightFeed = new HttpCamera("limelight", "http://10.85.67.11:5800/stream.mjpg");

    // Compressor
    public final Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

    // Power Distribution Panel (PDP)
    public final PowerDistribution pdp = new PowerDistribution(0, ModuleType.kCTRE);

    // Autonomous chooser
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    // The container for the robot.  Contains subsystems, OI devices, and commands
    private RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        // Configure default commands
        m_drivetrain.setDefaultCommand(new Drive(m_drivetrain));
        
        // Configure autonomous sendable chooser and send to Shuffleboard
        m_chooser.addOption("Drive out auto", new AutoDriveOut(m_drivetrain));
        m_chooser.addOption("One ball auto", new AutoOneBall(m_drivetrain, m_collection, m_shooter));
        m_chooser.setDefaultOption("Two ball auto", new AutoTwoBall(m_drivetrain, m_collection, m_shooter));
        matchTab.add("Auto mode", m_chooser).withWidget(BuiltInWidgets.kComboBoxChooser)
            .withSize(2, 1)
            .withPosition(8, 0);

        // Send cameras to Shuffleboard
        matchTab.add("Front camera", frontCamera).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(5, 3)
            .withProperties(Map.of("rotation", "HALF"))
            .withPosition(0, 1);
        hangerTab.add("Jack camera", jackCamera).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(5, 3)
            .withProperties(Map.of("rotation", "QUARTER_CCW"))
            .withPosition(2, 0);
        matchTab.add("Limelight", limelightFeed).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(5, 3)
            .withPosition(5, 1)
            .withProperties(Map.of("show crosshair", false, "show controls", false));

        // Turn on the compressor
        compressor.enableDigital();
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
        backButton.toggleWhenPressed(new AlignShooter(m_drivetrain, m_limelight, m_shooter), true);

        final JoystickButton startButton = new JoystickButton(xboxController, XboxController.Button.kStart.value);
        startButton.whenPressed(new ReverseCollection(m_collection), true);

        final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);        
        rightBumper.toggleWhenPressed(new Collect(m_collection), true);

        final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);        
        leftBumper.toggleWhenPressed(new Shoot(m_shooter, m_collection), true);

        final JoystickButton buttonB = new JoystickButton(xboxController, XboxController.Button.kB.value);
        buttonB.whenPressed(new ShootDistance(m_shooter), true);

        if (Constants.MODE == RobotMode.TESTING) {
            final JoystickButton buttonX = new JoystickButton(xboxController, XboxController.Button.kX.value);
            buttonX.whenPressed(new ShootToggle(m_shooter), true);  
        }

        final JoystickButton h_leftStick = new JoystickButton(hangerController, XboxController.Button.kLeftStick.value);
        h_leftStick.whenPressed(new HangerToggle(m_hanger), true);

        final JoystickButton h_rightStick = new JoystickButton(hangerController, XboxController.Button.kRightStick.value);
        h_rightStick.whenPressed(new IdleModeToggle(m_hanger), true);
        
        final JoystickButton h_leftBumper = new JoystickButton(hangerController, XboxController.Button.kLeftBumper.value);
        h_leftBumper.whileHeld(new ExtendLeftHanger(m_hanger, false), true);
        
        final JoystickButton h_rightBumper = new JoystickButton(hangerController, XboxController.Button.kRightBumper.value);
        h_rightBumper.whileHeld(new ExtendRightHanger(m_hanger), true);

        final JoystickButton h_backButton = new JoystickButton(hangerController, XboxController.Button.kBack.value);
        h_backButton.whileHeld(new RetractLeftHanger(m_hanger, false), true);
        
        final JoystickButton h_startButton = new JoystickButton(hangerController, XboxController.Button.kStart.value);
        h_startButton.whileHeld(new RetractRightHanger(m_hanger), true);

        // Extend or retract both hanger arms together
        final JoystickButton h_buttonY = new JoystickButton(hangerController, XboxController.Button.kY.value);
        h_buttonY.whileHeld(new ExtendLeftHanger(m_hanger, true), true);

        final JoystickButton h_buttonA = new JoystickButton(hangerController, XboxController.Button.kA.value);
        h_buttonA.whileHeld(new RetractLeftHanger(m_hanger, true), true);
    }

    public static RobotContainer getInstance() {
        return m_robotContainer;
    }

    public XboxController getXboxController() {
        return xboxController;
    }

    public XboxController getHangerController() {
        return hangerController;
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
