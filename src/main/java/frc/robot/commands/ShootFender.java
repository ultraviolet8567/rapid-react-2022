package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShootFender extends CommandBase{
    private final Shooter m_shooter;

    public ShootFender(Shooter subsystem) {
        m_shooter = subsystem;
        addRequirements(m_shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooter.runBigFlywheel(Constants.distanceBigSpeed, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(Constants.distanceSmallSpeed, ControlType.kVelocity);
    } 
}