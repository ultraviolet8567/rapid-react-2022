package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class AutoDriveOut extends CommandBase {
    private Drivetrain m_drivetrain;
    private Timer timer;

    public AutoDriveOut(Drivetrain subsystem) {
        m_drivetrain = subsystem;
        addRequirements(m_drivetrain);    
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        SmartDashboard.putNumber("Stopwatch", timer.get());

        if (timer.get() < 10) {
            m_drivetrain.getDifferentialDrive().arcadeDrive(0.5, 0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.stopMotors();
        timer.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (timer.get() >= 10);
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
