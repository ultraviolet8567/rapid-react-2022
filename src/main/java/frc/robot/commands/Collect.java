package frc.robot.commands;

import static frc.robot.subsystems.Collection.Status.*;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Collection;


public class Collect extends CommandBase {
    private final Collection m_collection;
    private Timer timer = new Timer();
    
    public Collect(Collection subsystem) {
        m_collection = subsystem;
        addRequirements(m_collection);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_collection.extend();
        m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity);

        timer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_collection.intakeStatus() == EXTENDED && timer.get() >= 0.5) {
            m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity);
        }
        if (m_collection.ballTop()) {
            m_collection.runConveyor(0, ControlType.kVelocity);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_collection.runIntake(0, ControlType.kVelocity);
        m_collection.runConveyor(0, ControlType.kVelocity);

        m_collection.retract();
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
