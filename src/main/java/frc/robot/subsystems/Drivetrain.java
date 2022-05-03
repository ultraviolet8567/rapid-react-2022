package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Drivetrain extends SubsystemBase {
    public CANSparkMax leftFront;
    public CANSparkMax leftBack;
    public CANSparkMax rightFront;
    public CANSparkMax rightBack;
    private MotorControllerGroup leftGroup;
    private MotorControllerGroup rightGroup;
    private DifferentialDrive differentialDrive;

    private SendableChooser<String> driveMode = new SendableChooser<>();
    
    private NetworkTableEntry reversedToggle;
    private Boolean drivingReversed = false;

    public Drivetrain() {
        leftFront = new CANSparkMax(3, MotorType.kBrushless);
        leftBack = new CANSparkMax(5, MotorType.kBrushless);

        rightFront = new CANSparkMax(4, MotorType.kBrushless);
        rightBack = new CANSparkMax(6, MotorType.kBrushless);

        leftGroup = new MotorControllerGroup(leftFront, leftBack);
        rightGroup = new MotorControllerGroup(rightFront, rightBack);
        rightGroup.setInverted(true);

        differentialDrive = new DifferentialDrive(leftGroup, rightGroup);
        differentialDrive.setSafetyEnabled(true);
        differentialDrive.setExpiration(0.1);
        differentialDrive.setMaxOutput(0.5);
        
        driveMode.addOption("Single stick", "Single stick");
        driveMode.setDefaultOption("Split control", "Split control");
        driveMode.addOption("Tank drive", "Tank drive");

        Shuffleboard.getTab("Match Data").add("Drive mode", driveMode).withWidget(BuiltInWidgets.kComboBoxChooser)
            .withSize(2, 1)
            .withPosition(5, 0);
        reversedToggle = Shuffleboard.getTab("Match Data").add("Drive reversed", false).withWidget(BuiltInWidgets.kToggleSwitch)
            .withSize(1, 1)
            .withPosition(2, 0)
            .getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        // drivingReversed = reversedToggle.getBoolean(drivingReversed);
        leftGroup.setInverted(drivingReversed);
        rightGroup.setInverted(!drivingReversed);
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.   
    public void stopMotors() {
        leftFront.stopMotor();
        leftBack.stopMotor();
        rightFront.stopMotor();
        rightBack.stopMotor();
    }

    public DifferentialDrive getDifferentialDrive() {
        return differentialDrive;
    }

    public Boolean isSingleStickDrive() {
        return driveMode.getSelected() == "Single stick";
    }

    public Boolean isTankDrive() {
        return driveMode.getSelected() == "Tank drive";
    }

    public void toggleSingleStick() {
        // Single-stick mode equates to true
        switch (driveMode.getSelected()) {
            case "Split control":
                driveMode.setDefaultOption("Single stick", "Single stick");
                break;
            case "Single stick":
                driveMode.setDefaultOption("Tank drive", "Tank drive");
                break;
            case "Tank drive":
                driveMode.setDefaultOption("Split control", "Split control");
                break;
        }
    }

    public void toggleDrivingDirection() {
        drivingReversed = !drivingReversed;
        reversedToggle.setBoolean(drivingReversed);
    }
}
