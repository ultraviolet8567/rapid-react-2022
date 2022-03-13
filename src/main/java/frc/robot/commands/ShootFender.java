package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.revrobotics.CANSparkMax.ControlType;

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
        m_shooter.runBigFlywheel(Constants.fenderSpeedBig, ControlType.kVelocity);
        m_shooter.runSmallFlywheel(Constants.fenderSpeedSmall, ControlType.kVelocity);
    } 
}