package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Shooter extends SubsystemBase {

    private CANSparkMax shooter1;
    private CANSparkMax shooter2;

    public Shooter() {
        shooter1 = new CANSparkMax(9, MotorType.kBrushless);
        shooter1.setInverted(false);

        shooter2 = new CANSparkMax(10, MotorType.kBrushless);
        System.out.print(shooter2.getInverted());
        shooter2.setInverted(false);
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter/First shooter motor", shooter1.getEncoder().getVelocity());
        SmartDashboard.putNumber("Shooter/Second shooter motor", shooter2.getEncoder().getVelocity());
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Sets a parameter (type) of shooterMC1(? no idea what that stands for) to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runShooter1(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(shooter1);
        pid_controller.setReference(setPoint, type);
    }

    // Add overload to runConveyor that does not use PID
    public void runShooter1(double speed) {
        shooter1.set(speed);
    }

    // Sets a parameter (type) of shooterMC2(again, no idea) to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runShooter2(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(shooter2);
        pid_controller.setReference(setPoint, type);
    }

    // Add overload to runConveyor that does not use PID
    public void runShooter2(double speed) {
        shooter2.set(speed);
    }
}