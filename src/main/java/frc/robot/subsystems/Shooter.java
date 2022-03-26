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


public class Shooter extends SubsystemBase {

    private CANSparkMax bigFlywheel;
    private CANSparkMax smallFlywheel;

    private NetworkTableEntry bVelocity;
    private NetworkTableEntry sVelocity;

    private NetworkTableEntry velocitiesToggle;
    private String velocity = "Lower hub";

    public Shooter() {
        bigFlywheel = new CANSparkMax(1, MotorType.kBrushless);
        bigFlywheel.setInverted(false);
        smallFlywheel = new CANSparkMax(2, MotorType.kBrushless);

        bVelocity = Shuffleboard.getTab("Shooter").add("Big flywheel", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();
        sVelocity = Shuffleboard.getTab("Shooter").add("Small flywheel", 0).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();

        velocitiesToggle = Shuffleboard.getTab("Match Data").add("Shooting mode", "Lower hub").withWidget(BuiltInWidgets.kTextView)
            .withSize(2, 1)
            .withPosition(3, 0)
            .getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        bVelocity.setNumber(bigFlywheel.getEncoder().getVelocity() / 1000);
        sVelocity.setNumber(smallFlywheel.getEncoder().getVelocity() / 1000);

        velocitiesToggle.setString(velocity);
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

    public double[] flywheelSpeeds() {
        // Fender
        if (velocity == "Fender") {
            return new double[] { Constants.fenderBigSpeed, Constants.fenderSmallSpeed };
        }
        else if (velocity == "Distance") {
            return new double[] { Constants.distanceBigSpeed, Constants.distanceSmallSpeed };
        }
        // Lower hub
        else {
            return new double[] { Constants.hubBigSpeed, Constants.hubSmallSpeed };
        }
    } 

    public void setMode(String mode) {
        if (mode == "Fender") {
            velocity = "Fender";
        }
        else if (mode == "Distance") {
            velocity = "Distance";
        }
        // Lower hub
        else {
            velocity = "Lower hub";
        }
    }

    public void toggle() {
        if (velocity == "Fender") {
            velocity = "Lower hub";
        }
        // Lower hub
        else {
            velocity = "Fender";
        }
    }
}
