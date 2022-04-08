package frc.robot.commands.hanger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hanger;

public class HangerToggle extends CommandBase {
    private final Hanger m_hanger;

    public HangerToggle(Hanger subsystem) {
        m_hanger = subsystem;
        addRequirements(m_hanger);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_hanger.toggleMode();
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
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
