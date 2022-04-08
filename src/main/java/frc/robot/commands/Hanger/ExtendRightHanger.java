package frc.robot.commands.hanger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hanger;


public class ExtendRightHanger extends CommandBase {
    private final Hanger m_hanger;

    public ExtendRightHanger(Hanger subsystem) {
        m_hanger = subsystem;
        addRequirements(m_hanger);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_hanger.runRightHanger(false);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_hanger.stopRightHanger();
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
