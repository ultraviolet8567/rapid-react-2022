package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

    private SendableChooser<Boolean> driveMode = new SendableChooser<>();

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
        
        driveMode.setDefaultOption("Single stick", true);
        driveMode.addOption("Split control", false);

        Shuffleboard.getTab("Drive Settings").add("Drive mode", driveMode).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1);
        Shuffleboard.getTab("Drive Settings").add("Differential drive", differentialDrive).withWidget(BuiltInWidgets.kDifferentialDrive).withSize(4, 3);
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
    public void stopMotors() {
        leftFront.stopMotor();
        leftBack.stopMotor();
        rightFront.stopMotor();
        rightBack.stopMotor();
    }

    public DifferentialDrive getDifferentialDrive() {
        return differentialDrive;
    }

    public boolean isSingleStickDrive() {
        return driveMode.getSelected();
    }

    public void toggleSingleStick() {
        // Single-stick mode equates to true
        if (driveMode.getSelected()) {
            driveMode.setDefaultOption("Split control", false);
        }
        else {
            driveMode.setDefaultOption("Single stick", true);
        }
    }
}
