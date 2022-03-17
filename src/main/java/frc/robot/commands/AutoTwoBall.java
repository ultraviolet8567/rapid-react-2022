package frc.robot.commands;

import java.util.Map;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;


public class AutoTwoBall extends CommandBase {
    private Drivetrain m_drivetrain;
    private Collection m_collection;
    private Shooter m_shooter;
    private Timer timer;

    private double rotations = 0;
    private boolean collectedBall = false;

    private NetworkTableEntry stopwatch;

    public AutoTwoBall(Drivetrain drivetrain, Collection collection, Shooter shooter) {
        m_drivetrain = drivetrain;
        m_collection = collection;
        m_shooter = shooter;
        addRequirements(m_drivetrain);    
        addRequirements(m_collection);    
        addRequirements(m_shooter);    
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();

        stopwatch = Shuffleboard.getTab("Auto").add("Stopwatch", timer.get()).withWidget(BuiltInWidgets.kDial)
            .withProperties(Map.of("min", 0, "max", 15, "show value", true))
            .getEntry();

        m_drivetrain.getDifferentialDrive().arcadeDrive(0.5, 0);

        m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity);
        m_shooter.runBigFlywheel(Constants.fenderBigSpeed, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(Constants.fenderSmallSpeed, ControlType.kVelocity);
    }
// Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        stopwatch.setNumber(timer.get());

        if (m_collection.ballBottom()){
            collectedBall = true;
            rotations = -m_drivetrain.leftFront.getEncoder().getPosition();
        }
        if (collectedBall) {
            if (m_drivetrain.leftFront.getEncoder().getPosition() != rotations) {
                m_drivetrain.getDifferentialDrive().arcadeDrive(-0.5, 0);
            }
            else {
                m_drivetrain.stopMotors();
                m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);
            
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.runSmallFlywheel(0, ControlType.kVelocity);
        m_shooter.runBigFlywheel(0, ControlType.kVelocity);

        m_collection.runIntake(0, ControlType.kVelocity);
        m_collection.runConveyor(0, ControlType.kVelocity);
    
        m_drivetrain.stopMotors();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return timer.get() >= 15; // (timer.get() == 15 || !m_collection.ballTop() && !m_collection.ballBottom());
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
