package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;


public class Collect extends CommandBase {
    private final Collection m_collection;
    private boolean ballReachedBottom = false;
    private boolean ballReachedTop = false;
    
    public Collect(Collection subsystem) {
        m_collection = subsystem;
        addRequirements(m_collection);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (!m_collection.ballTop()) { m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity); }
        if (!m_collection.ballBottom()) { m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity); }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_collection.ballTop()) {
            m_collection.runConveyor(0, ControlType.kVelocity);
            ballReachedTop = true;
        }
        if (m_collection.ballBottom()) {
            m_collection.runIntake(0, ControlType.kVelocity);
            ballReachedBottom = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_collection.runIntake(0, ControlType.kVelocity);
        m_collection.runConveyor(0, ControlType.kVelocity);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (ballReachedTop && ballReachedBottom);
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
