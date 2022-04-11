package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;
import frc.robot.subsystems.Shooter;


public class Shoot extends CommandBase {
    private final Collection m_collection;
    private final Shooter m_shooter;
    
    public Shoot(Shooter shooter, Collection collection) {
        m_collection = collection;
        m_shooter = shooter;
        addRequirements(m_collection);
        addRequirements(m_shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);
        // m_shooter.setMode("Fender");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_collection.runConveyor(0, ControlType.kVelocity);
        m_shooter.setMode("Off");;
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
