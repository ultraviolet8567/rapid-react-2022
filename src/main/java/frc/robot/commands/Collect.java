package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Collection;


public class Collect extends CommandBase {
    private final Collection m_collection;
    private boolean ballReachedBottom;
    private boolean ballReachedTop;
    private boolean spinConveyor;
    private Timer timer;
    
    public Collect(Collection subsystem) {
        m_collection = subsystem;
        addRequirements(m_collection);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (!m_collection.ballTop()) { m_collection.runConveyor(Constants.conveyorSpeed, ControlType.kVelocity); }
        if (!m_collection.ballBottom()) { m_collection.runIntake(Constants.intakeSpeed, ControlType.kVelocity); }

        ballReachedTop = false;
        ballReachedBottom = false;
        spinConveyor = false;

        timer = new Timer();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_collection.ballTop()) {
            ballReachedTop = true;
            if (!spinConveyor) { m_collection.runConveyor(0, ControlType.kVelocity); }
        }
        if (m_collection.ballBottom()) {
            timer.start();
            if (ballReachedTop) {
                m_collection.runConveyor(Constants.conveyorSpeed / 5, ControlType.kVelocity);
                spinConveyor = true;
            }
        }
        if (timer.get() >= 3) {
            ballReachedBottom = true;
            m_collection.runIntake(0, ControlType.kVelocity);
            if (ballReachedTop) { m_collection.runConveyor(0, ControlType.kVelocity); }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_collection.runIntake(0, ControlType.kVelocity);
        m_collection.runConveyor(0, ControlType.kVelocity);

        // RobotContainer.getInstance().m_shooter.setMode("Fender");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (ballReachedTop && ballReachedBottom);
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
