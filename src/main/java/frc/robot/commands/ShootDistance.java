package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShootDistance extends CommandBase{
    private final Shooter m_shooter;

    public ShootDistance(Shooter subsystem) {
        m_shooter = subsystem;
        addRequirements(m_shooter);
    }
    
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_shooter.runBigFlywheel(Constants.distanceBigSpeed, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(Constants.distanceSmallSpeed, ControlType.kVelocity);
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
