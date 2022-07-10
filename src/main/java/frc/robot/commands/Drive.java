package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final XboxController xbox = RobotContainer.getInstance().getXboxController();
    
    public Drive(Drivetrain subsystem) {
        m_drivetrain = subsystem;
        addRequirements(m_drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_drivetrain.isSingleStickDrive()) {
            if (Math.abs(xbox.getLeftY()) > 0.1 || Math.abs(xbox.getLeftX()) > 0.1) {
                // Normal single stick drive
                m_drivetrain.getDifferentialDrive().arcadeDrive(
                    -xbox.getLeftX(),
                    xbox.getLeftY(),
                    true);
                m_drivetrain.stopMotors();
            }
            else {
                // Fine-tuning single stick drive
                SmartDashboard.putString("IMPORTANT INFO", "get good at driving");
            }
        }
        else if (m_drivetrain.isTankDrive()) {
            m_drivetrain.getDifferentialDrive().tankDrive(
                -xbox.getLeftY(),
                -xbox.getRightY(),
                true);
        }
        else {
            // Split control drive
            m_drivetrain.getDifferentialDrive().arcadeDrive(
                -xbox.getLeftY(),
                xbox.getRightX(),
                true);
        }
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

    // Exponential modifier function: 0.1 * Math.pow(3.5, 2 * input) - 0.1
}
