package frc.robot.commands.hanger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Hanger;

public class IdleModeToggle extends CommandBase {
    private final Hanger m_hanger;
    private Timer timer;

    public IdleModeToggle(Hanger subsystem) {
        m_hanger = subsystem;
        addRequirements(m_hanger);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();
        
        m_hanger.toggleIdleMode();

        RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kLeftRumble, 0.5);
        RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kRightRumble, 0.5);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kLeftRumble, 0);
        RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kRightRumble, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return timer.get() >= 1;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
