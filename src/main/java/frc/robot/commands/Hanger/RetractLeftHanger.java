package frc.robot.commands.Hanger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hanger;


public class RetractLeftHanger extends CommandBase {
    private final Hanger m_hanger;
    private final boolean m_both;

    public RetractLeftHanger(Hanger subsystem, boolean both) {
        m_hanger = subsystem;
        m_both = both;
        addRequirements(m_hanger);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (m_both) {
            m_hanger.runLeftHanger(true);
            m_hanger.runRightHanger(true);
        }
        else {
            m_hanger.runLeftHanger(true);
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (m_both) {
            m_hanger.stopLeftHanger();
            m_hanger.stopRightHanger();
        }
        else {
            m_hanger.stopLeftHanger();
        }
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
