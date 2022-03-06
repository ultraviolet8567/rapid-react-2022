package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Hanger;

public class RetractHanger extends CommandBase {
    private final Hanger m_hanger;

    public RetractHanger(Hanger subsystem) {

        m_hanger = subsystem;
        addRequirements(m_hanger);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_hanger.runHanger(-Constants.hangerSpeed, ControlType.kVelocity);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_hanger.runHanger(0, ControlType.kVelocity);
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