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
        leftFront = new CANSparkMax(4, MotorType.kBrushless);
        leftFront.setInverted(true);

        leftBack = new CANSparkMax(3, MotorType.kBrushless);
        leftBack.setInverted(true);

        rightFront = new CANSparkMax(1, MotorType.kBrushless);
        rightFront.setInverted(false);

        rightBack = new CANSparkMax(2, MotorType.kBrushless);
        rightBack.setInverted(false);

        leftFront.restoreFactoryDefaults();
        leftBack.restoreFactoryDefaults();
        rightFront.restoreFactoryDefaults();
        rightBack.restoreFactoryDefaults();

        leftGroup = new MotorControllerGroup(leftFront, leftBack);
        rightGroup = new MotorControllerGroup(rightFront, rightBack);
        
        differentialDrive = new DifferentialDrive(leftGroup, rightGroup);
        differentialDrive.setSafetyEnabled(true);
        differentialDrive.setExpiration(0.1);
        differentialDrive.setMaxOutput(0.5);
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() { 
        SmartDashboard.putData("Differential Drive", differentialDrive);
        SmartDashboard.putNumber("Motor Speed: Left Front", leftFront.get());
        SmartDashboard.putNumber("Motor Speed: Right Front", rightFront.get());
        SmartDashboard.putNumber("Motor Speed: Left Back", leftBack.get());
        SmartDashboard.putNumber("Motor Speed: Right Back", rightBack.get());
        SmartDashboard.putString("Drive Setting", singleStickOperation ? "Single Stick" : "Split Control");
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.   
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
