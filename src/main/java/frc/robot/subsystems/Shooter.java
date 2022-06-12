package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.RobotMode;


public class Shooter extends SubsystemBase {
    private CANSparkMax bigFlywheel;
    private CANSparkMax smallFlywheel;

    private NetworkTableEntry bVelocity;
    private NetworkTableEntry sVelocity;
    private NetworkTableEntry bSet;
    private NetworkTableEntry sSet;
    private NetworkTableEntry bNumVelocity;
    private NetworkTableEntry sNumVelocity;

    private NetworkTableEntry velocitiesToggle;
    private Velocity velocity = Velocity.OFF;
    private double calibrationSpeed = Constants.fenderBigSpeed;

    public Shooter() {
        bigFlywheel = new CANSparkMax(1, MotorType.kBrushless);
        bigFlywheel.setInverted(false);
        smallFlywheel = new CANSparkMax(2, MotorType.kBrushless);

        velocitiesToggle = Shuffleboard.getTab("Match Data").add("Shooting mode", "Lower hub").withWidget(BuiltInWidgets.kTextView)
            .withSize(2, 1)
            .withPosition(3, 0)
            .getEntry();

        bVelocity = Shuffleboard.getTab("Shooter").add("Big flywheel", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 70.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();
        sVelocity = Shuffleboard.getTab("Shooter").add("Small flywheel", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 70.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();
        bSet = Shuffleboard.getTab("Shooter").add("Big flywheel set speed", calibrationSpeed / 1000).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.5, "max", 70, "block increment", 0.01))
            .withPosition(6, 0)
            .getEntry();
        sSet = Shuffleboard.getTab("Shooter").add("Small flywheel set speed", calibrationSpeed * Constants.flywheelRatio / 1000).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.5, "max", 70, "block increment", 0.01))
            .withPosition(6, 1)
            .getEntry();
        
        if (Constants.MODE == RobotMode.TESTING) {
            bNumVelocity = Shuffleboard.getTab("Shooter").add("Big V", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 3)
                .getEntry();
            sNumVelocity = Shuffleboard.getTab("Shooter").add("Small V", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(1, 3)
                .getEntry();
        }
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        velocitiesToggle.setString(velocity.toString());

        bVelocity.setNumber(bigFlywheel.getEncoder().getVelocity() / 1000);
        sVelocity.setNumber(smallFlywheel.getEncoder().getVelocity() / 1000);
        calibrationSpeed = 1000 * bSet.getDouble(calibrationSpeed);
            
        sSet.setNumber(calibrationSpeed * Constants.flywheelRatio / 1000);

        if (Constants.MODE == RobotMode.TESTING) {
            bNumVelocity.setNumber(bigFlywheel.getEncoder().getVelocity() / 1000);
            sNumVelocity.setNumber(smallFlywheel.getEncoder().getVelocity() / 1000);
        }

        runFlywheels();
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Sets a parameter (type) of the big flywheel to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runBigFlywheel(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(bigFlywheel);
        pid_controller.setReference(setPoint, type);
    }

    // Sets a parameter (type) of the big flywheel to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runSmallFlywheel(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(smallFlywheel);
        pid_controller.setReference(setPoint, type);
    }

    public void runFlywheels() {
        if (velocity != Velocity.LIMELIGHT) {
            double[] speeds;

            switch (velocity) {
                case LOWER_HUB:
                    speeds = new double[] { Constants.hubBigSpeed, Constants.hubSmallSpeed };
                    break;
                case FENDER:
                    speeds = new double[] { Constants.fenderBigSpeed, Constants.fenderSmallSpeed };
                    break;
                case CALIBRATION:
                    speeds = new double[] { calibrationSpeed, calibrationSpeed * Constants.flywheelRatio };
                    break;
                case LIMELIGHT:
                    speeds = RobotContainer.getInstance().m_limelight.calculatedSpeeds();
                    break;
                default:
                    speeds = new double[] { 0, 0 };
                    break;
            }

            runBigFlywheel(speeds[0], ControlType.kVelocity);
            runSmallFlywheel(speeds[1], ControlType.kVelocity);
        }
    } 

    public void setMode(String mode) {
        switch (mode) {
            case "Lower hub":
                velocity = Velocity.LOWER_HUB;
                break;
            case "Fender":
                velocity = Velocity.FENDER;
                break;
            case "Calibration":
                velocity = Velocity.CALIBRATION;
                break;
            case "Limelight":
                velocity = Velocity.LIMELIGHT;
                break;
            default:
                velocity = Velocity.OFF;
                break;
        }
    }

    public void toggle() {
        switch (velocity) {
            case OFF:
                velocity = Velocity.LOWER_HUB;
                break;
            case LOWER_HUB:
                velocity = Velocity.FENDER;
                break;
            case FENDER:
                velocity = Velocity.CALIBRATION;
                break;
            case CALIBRATION:
                velocity = Velocity.LIMELIGHT;
                break;
            default:
                velocity = Velocity.OFF;
                break;
        }
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public enum Velocity {
        OFF,
        LOWER_HUB,
        FENDER,
        CALIBRATION,
        LIMELIGHT
    }
}
