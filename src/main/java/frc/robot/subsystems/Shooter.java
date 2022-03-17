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
import frc.robot.RobotContainer;


public class Shooter extends SubsystemBase {

    private CANSparkMax bigFlywheel;
    private CANSparkMax smallFlywheel;

    private NetworkTableEntry bVelocity;
    private NetworkTableEntry sVelocity;

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
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        bVelocity.setNumber(bigFlywheel.getEncoder().getVelocity() / 1000);
        sVelocity.setNumber(smallFlywheel.getEncoder().getVelocity() / 1000);
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
}
