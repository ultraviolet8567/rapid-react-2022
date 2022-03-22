package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;


public class Collection extends SubsystemBase {
    private CANSparkMax intake;
    private CANSparkMax conveyor;

    private DigitalInput detectorTop;
    private DigitalInput detectorBottom;

    private NetworkTableEntry intakeVelocity;
    private NetworkTableEntry conveyorVelocity;
    private NetworkTableEntry detectedTop;
    private NetworkTableEntry detectedBottom;
    
    private NetworkTableEntry reversedToggle;
    private Boolean directionReversed = false;

    public Collection() {
        intake = new CANSparkMax(7, MotorType.kBrushless);
        intake.setInverted(true);

        conveyor = new CANSparkMax(8, MotorType.kBrushless);

        detectorTop = new DigitalInput(0);
        detectorBottom = new DigitalInput(1);

        intakeVelocity = Shuffleboard.getTab("Collection").add("Intake", intake.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "RPM"))
            .withPosition(0, 0)
            .getEntry();
        conveyorVelocity = Shuffleboard.getTab("Collection").add("Conveyor", conveyor.getEncoder().getVelocity()).withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("lower bound", -0.5, "upper bound", 15.5, "automatic bounds", false, "unit", "RPM"))
            .withPosition(3, 0)
            .getEntry();

        ShuffleboardLayout sensors = Shuffleboard.getTab("Collection").getLayout("Sensors", BuiltInLayouts.kList)
            .withSize(2, 2);
        detectedTop = sensors.add("Top ball", ballTop()).withWidget(BuiltInWidgets.kBooleanBox).getEntry();
        detectedBottom = sensors.add("Bottom ball", ballBottom()).withWidget(BuiltInWidgets.kBooleanBox).getEntry();

        reversedToggle = Shuffleboard.getTab("Match Data").add("Collect reversed", false).withWidget(BuiltInWidgets.kToggleSwitch)
            .withSize(1, 1)
            .withPosition(7, 0)
            .getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        intakeVelocity.setNumber(intake.getEncoder().getVelocity() / 1000);
        conveyorVelocity.setNumber(conveyor.getEncoder().getVelocity() / 1000);
        detectedTop.setBoolean(ballTop());
        detectedBottom.setBoolean(ballBottom());

        // directionReversed = reversedToggle.getBoolean(directionReversed);
        intake.setInverted(!directionReversed);
        conveyor.setInverted(directionReversed);
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

    public void toggleDirection() {
        directionReversed = !directionReversed;
        reversedToggle.setBoolean(directionReversed);
    }

    // Sets a parameter (type) of the intake to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runIntake(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(intake);
        pid_controller.setReference(setPoint, type);
    }

    // Sets a parameter (type) of the intake to the given value (setPoint)
    // e.g. possible parameters are ControlType.kDutyCycle, ControlType.kPosition, ControlType.kVelocity, and ControlType.kVoltage
    public void runConveyor(double setPoint, ControlType type) {
        SparkMaxPIDController pid_controller = RobotContainer.getDefaultPIDController(conveyor);
        pid_controller.setReference(setPoint, type);
    }
}
