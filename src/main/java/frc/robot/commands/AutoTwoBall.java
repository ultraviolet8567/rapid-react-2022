package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Collection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;


public class AutoTwoBall extends CommandBase {
    private Drivetrain m_drivetrain;
    private Collection m_collection;
    private Shooter m_shooter;

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
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
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
