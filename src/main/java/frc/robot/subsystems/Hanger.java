package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Hanger extends SubsystemBase {

    private CANSparkMax left;
    private CANSparkMax right;

    public Hanger() {
        left = new CANSparkMax(5, MotorType.kBrushless);
        left.setInverted(false);

        right = new CANSparkMax(6, MotorType.kBrushless);
        right.setInverted(false);
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hanger/Left motor velocity", left.getEncoder().getVelocity());
        SmartDashboard.putNumber("Collection/Right motor velocity", right.getEncoder().getVelocity());
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

    // Add overload to runHanger that does not use PID
    public void runHanger(double speed) {
        right.set(speed);
        left.set(speed);
    }
}
