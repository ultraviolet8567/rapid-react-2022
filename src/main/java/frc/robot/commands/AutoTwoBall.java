package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Collection.Status;


public class AutoTwoBall extends CommandBase {
    private Drivetrain m_drivetrain;
    private Collection m_collection;
    private Shooter m_shooter;
    private Timer timer;

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

        m_collection.extend();
    }
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_collection.intakeStatus() == Status.EXTENDED) {
            m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity);
        }
        else {
            m_collection.runIntake(0, ControlType.kVelocity);
        }

        if (timer.get() >= 11.5) {
            m_drivetrain.stopMotors();
            m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);
        }
        else if (timer.get() >= 9.75) {
            m_shooter.setMode("Fender");
        }
        else if (timer.get() >= 5.75) {
            m_drivetrain.getDifferentialDrive().arcadeDrive(-0.5, 0);
            m_collection.retract();
        }
        else if (timer.get() >= 5.5) {
            m_drivetrain.stopMotors();
        }
        else if (timer.get() >= 0.5) {
            m_drivetrain.getDifferentialDrive().arcadeDrive(0.5, 0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.runSmallFlywheel(0, ControlType.kVelocity);
        m_shooter.runBigFlywheel(0, ControlType.kVelocity);

        m_collection.runIntake(0, ControlType.kVelocity);
        m_collection.runConveyor(0, ControlType.kVelocity);
        m_collection.retract();
    
        m_drivetrain.stopMotors();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
