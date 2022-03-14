package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;


public class AutoOneBall extends CommandBase {
    private Drivetrain m_drivetrain;
    private Collection m_collection;
    private Shooter m_shooter;
    private Timer timer;

    public AutoOneBall(Drivetrain drivetrain, Collection collection, Shooter shooter) {
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

        m_shooter.runBigFlywheel(Constants.fenderBigSpeed, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(Constants.fenderSmallSpeed, ControlType.kVelocity);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        SmartDashboard.putNumber("Stopwatch", timer.get());

        if (timer.get() > 10) {
            m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);
        }
        if (timer.get() > 15) {
            m_drivetrain.getDifferentialDrive().arcadeDrive(0.5, 0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.runBigFlywheel(0, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(0, ControlType.kVelocity);

        m_collection.runConveyor(0, ControlType.kVelocity);

        m_drivetrain.stopMotors();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (timer.get() > 25);
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
