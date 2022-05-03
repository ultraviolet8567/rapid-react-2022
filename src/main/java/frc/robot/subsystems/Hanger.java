package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.RobotMode;


public class Hanger extends SubsystemBase {

    private CANSparkMax left;
    private CANSparkMax right;

    private NetworkTableEntry leftVelocity;
    private NetworkTableEntry rightVelocity;
    private NetworkTableEntry hangerSpeed;
    private NetworkTableEntry idleMode;

    private boolean fastMode = true;
    private boolean highBar = false;

    public Hanger() {
        left = new CANSparkMax(9, MotorType.kBrushless);
        left.setIdleMode(IdleMode.kBrake);
        right = new CANSparkMax(10, MotorType.kBrushless);
        right.setIdleMode(IdleMode.kBrake);
        
        if (Constants.MODE == RobotMode.TESTING) {
            leftVelocity = Shuffleboard.getTab("Hanger").add("Left hanger", left.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
                .withProperties(Map.of("lower bound", -0.5, "upper bound", 10.5, "automatic bounds", false, "unit", "RPM"))
                .withPosition(2, 0)
                .getEntry();
            rightVelocity = Shuffleboard.getTab("Hanger").add("Right hanger", right.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
                .withProperties(Map.of("lower bound", -0.5, "upper bound", 10.5, "automatic bounds", false, "unit", "RPM"))
                .withPosition(5, 0)
                .getEntry();
        }

        ShuffleboardLayout settings = Shuffleboard.getTab("Hanger").getLayout("Settings", BuiltInLayouts.kList)
            .withPosition(0, 0)
            .withSize(2, 2);
        idleMode = settings.add("HIGH BAR", highBar).withWidget(BuiltInWidgets.kBooleanBox).getEntry();
        hangerSpeed = settings.add("Fast mode", fastMode).withWidget(BuiltInWidgets.kBooleanBox).getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        if (Constants.MODE == RobotMode.TESTING) {
            leftVelocity.setNumber(left.getEncoder().getVelocity() / 1000);
            rightVelocity.setNumber(right.getEncoder().getVelocity() / 1000);
        }

        hangerSpeed.setBoolean(fastMode);
        idleMode.setBoolean(highBar);
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    public void toggleMode() {
        fastMode = !fastMode;
    }

    public void runLeftHanger(boolean reverse) {
        SparkMaxPIDController left_controller = RobotContainer.getDefaultPIDController(left);
        double setPoint = reverse ? -1 : 1;
        
        if (fastMode) {
            setPoint = setPoint * Constants.hangerFastSpeed;
        }
        else {
            setPoint = setPoint * Constants.hangerSlowSpeed;
        }
        left_controller.setReference(setPoint, ControlType.kVelocity);
    }

    public void runRightHanger(boolean reverse) {
        SparkMaxPIDController right_controller = RobotContainer.getDefaultPIDController(right);
        double setPoint = reverse ? -1 : 1;

        if (fastMode) {
            setPoint = setPoint * Constants.hangerFastSpeed;
        }
        else {
            setPoint = setPoint * Constants.hangerSlowSpeed;
        }
        right_controller.setReference(setPoint, ControlType.kVelocity);
    }

    public void stopLeftHanger() {
        left.stopMotor();
    }
    
    public void stopRightHanger() {
        right.stopMotor();
    }

    public void toggleIdleMode() {
        highBar = !highBar;

        if (highBar) {
            left.setIdleMode(IdleMode.kCoast);
            right.setIdleMode(IdleMode.kCoast);
        }
        else {
            left.setIdleMode(IdleMode.kBrake);
            right.setIdleMode(IdleMode.kBrake);
        }
    }
}
