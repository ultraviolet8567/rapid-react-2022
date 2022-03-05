package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Collection extends SubsystemBase {
    private CANSparkMax intake;
    private CANSparkMax conveyor;

    private DigitalInput detectorTop;
    private DigitalInput detectorBottom;

    public Collection() {
        intake = new CANSparkMax(7, MotorType.kBrushless);
        intake.setInverted(false);

        conveyor = new CANSparkMax(8, MotorType.kBrushless);
        conveyor.setInverted(false);

        detectorTop = new DigitalInput(0);
        addChild("DetectorTop", detectorTop);

        detectorBottom = new DigitalInput(1);
        addChild("DetectorBottom", detectorBottom);
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Collection/Intake velocity", intake.getEncoder().getVelocity());
        SmartDashboard.putNumber("Collection/Conveyor velocity", conveyor.getEncoder().getVelocity());
        SmartDashboard.putBoolean("Collection/Detected - Top ball", ballTop());
        SmartDashboard.putBoolean("Collection/Detected - Bottom ball", ballBottom());
        SmartDashboard.putNumber("Ball count", (ballTop() ? 1 : 0) + (ballBottom() ? 1 : 0));
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Returns whether a ball is detected by the top photoelectric sensor
    public boolean ballTop() {
        // Flip the value of the DIO since detection corresponds to 0
        return !detectorTop.get();
    }

    // Returns whether a ball is detected by the bottom photoelectric sensor
    public boolean ballBottom() {
        // Flip the value of the DIO since detection corresponds to 0
        return !detectorBottom.get();
    }

    // Sets a parameter (type) of the intake to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runIntake(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(intake);
        pid_controller.setReference(setPoint, type);
    }

    // Add overload to runIntake that does not use PID
    public void runIntake(double speed) {
        intake.set(speed);
    }

    // Sets a parameter (type) of the intake to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runConveyor(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(conveyor);
        pid_controller.setReference(setPoint, type);
    }

    // Add overload to runConveyor that does not use PID
    public void runConveyor(double speed) {
        conveyor.set(speed);
    }
}
