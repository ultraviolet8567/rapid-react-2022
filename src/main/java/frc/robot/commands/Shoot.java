package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;


public class Shoot extends CommandBase {
    private final Collection m_collection;
    
    public Shoot(Collection subsystem) {
        m_collection = subsystem;
        addRequirements(m_collection);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // shooter.runShooter1(Constants.shooter1Speed, ControlType.kVelocity);
        m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_collection.runConveyor(0, ControlType.kVelocity);
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