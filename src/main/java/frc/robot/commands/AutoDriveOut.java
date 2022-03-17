package frc.robot.commands;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class AutoDriveOut extends CommandBase {
    private Drivetrain m_drivetrain;
    private Timer timer;

    private NetworkTableEntry stopwatch;

    public AutoDriveOut(Drivetrain subsystem) {
        m_drivetrain = subsystem;
        addRequirements(m_drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();

        stopwatch = Shuffleboard.getTab("Auto").add("Stopwatch", timer.get()).withWidget(BuiltInWidgets.kDial)
            .withProperties(Map.of("min", 0, "max", 15, "show value", true))
            .getEntry();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        stopwatch.setNumber(timer.get());

        m_drivetrain.getDifferentialDrive().arcadeDrive(0.5, 0);
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
        return timer.get() >= 15;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
