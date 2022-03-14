package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Drivetrain extends SubsystemBase {
    private CANSparkMax leftFront;
    private CANSparkMax leftBack;
    private CANSparkMax rightFront;
    private CANSparkMax rightBack;
    private MotorControllerGroup leftGroup;
    private MotorControllerGroup rightGroup;
    
    private DifferentialDrive differentialDrive;
    private boolean singleStickOperation = true;

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
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Left front motor speed", leftFront.get()); //Encoder().getVelocity());
        SmartDashboard.putNumber("Right front motor speed", rightFront.get()); //Encoder().getVelocity());
        SmartDashboard.putNumber("Left back motor speed", leftBack.get()); //Encoder().getVelocity());
        SmartDashboard.putNumber("Right back motor speed", rightBack.get()); //Encoder().getVelocity());
        SmartDashboard.putString("Drive setting", singleStickOperation ? "Single Stick" : "Split Control");
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
        return singleStickOperation;
    }

    public void toggleSingleStick() {
        singleStickOperation = !singleStickOperation;
    }
}
