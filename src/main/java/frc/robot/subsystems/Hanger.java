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


public class Hanger extends SubsystemBase {

    private CANSparkMax left;
    private CANSparkMax right;

    private NetworkTableEntry leftVelocity;
    private NetworkTableEntry rightVelocity;

    public Hanger() {
        left = new CANSparkMax(9, MotorType.kBrushless);
        right = new CANSparkMax(10, MotorType.kBrushless);

        leftVelocity = Shuffleboard.getTab("Hanger").add("Left hanger", left.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 10.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();
        rightVelocity = Shuffleboard.getTab("Hanger").add("Right hanger", right.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 10.5, "automatic bounds", false, "unit", "RPM"))
            .getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        leftVelocity.setNumber(left.getEncoder().getVelocity() / 1000);
        rightVelocity.setNumber(right.getEncoder().getVelocity() / 1000);
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Sets a parameter (type) of the hanger motors to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runHanger(double setPoint, ControlType type) {
        SparkMaxPIDController left_controller = RobotContainer.getDefaultPIDController(left);
        SparkMaxPIDController right_controller = RobotContainer.getDefaultPIDController(right);
        left_controller.setReference(setPoint, type);
        right_controller.setReference(setPoint, type);
    }
}
