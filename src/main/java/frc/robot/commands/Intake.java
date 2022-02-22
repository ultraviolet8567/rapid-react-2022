// ROBOTBUILDER TYPE: Command.

package frc.robot.commands;
import com.revrobotics.CANSparkMax.ControlType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;

public class Intake extends CommandBase {
    private final Collection m_collection;
    
    public Intake(Collection subsystem) {
        m_collection = subsystem;
        addRequirements(m_collection);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity);
        m_collection.runConveyor(Constants.intakeSpeed, ControlType.kVelocity);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_collection.ballTop()) {
            m_collection.runConveyor(0, ControlType.kVelocity);
        }
        if (m_collection.ballBottom()) {
            m_collection.runIntake(0, ControlType.kVelocity);
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
        return m_collection.ballTop() && m_collection.ballBottom();
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
