// m_hanger

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Hanger extends SubsystemBase {

    private CANSparkMax left;
    private CANSparkMax right;
    private MotorControllerGroup hangerGroup;

    public Hanger() {
        left = new CANSparkMax(8, MotorType.kBrushless);
        left.setInverted(false);

        right = new CANSparkMax(9, MotorType.kBrushless);
        right.setInverted(false);

        hangerGroup = new MotorControllerGroup(left, right  );
        addChild("HangerGroup",hangerGroup);
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Sets a parameter (type) of the hanger motors to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runHanger(double setPoint, ControlType type) {
        SparkMaxPIDController left_controller = left.getPIDController();
        SparkMaxPIDController right_controller = right.getPIDController();
        left_controller.setReference(setPoint, type);
        right_controller.setReference(setPoint, type);
    }

    // Add overload to runHanger that does not use PID
    public void runHanger(double speed) {
        right.set(speed);
        left.set(speed);
    }
}