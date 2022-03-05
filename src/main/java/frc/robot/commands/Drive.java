package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
    private final Drivetrain m_drivetrain;
    
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
        XboxController xbox = RobotContainer.getInstance().getXboxController();

        SmartDashboard.putNumber("Left Joystick X", xbox.getLeftX());
        SmartDashboard.putNumber("Left Joystick Y", xbox.getLeftY());
        SmartDashboard.putNumber("Right Joystick X", xbox.getRightX());
        SmartDashboard.putNumber("Right Joystick Y", xbox.getRightY());
        SmartDashboard.putString("Description", m_drivetrain.getDifferentialDrive().getDescription());

        if (m_drivetrain.isSingleStickDrive()) {
            if (Math.abs(xbox.getLeftY()) > 0.1 || Math.abs(-xbox.getLeftX()) > 0.1) {
                // Normal single stick drive
                m_drivetrain.getDifferentialDrive().tankDrive(
                    xbox.getLeftY(),
                    -xbox.getLeftX());
            }
            else {
                // Fine-tuning single stick drive
                m_drivetrain.getDifferentialDrive().arcadeDrive(
                    xbox.getRightY() / Math.sqrt(2),
                    -xbox.getRightX() / Math.sqrt(2));
            }
        }
        else {
            // Split control drive
            m_drivetrain.getDifferentialDrive().arcadeDrive(
                xbox.getLeftY(),
                -xbox.getRightX());
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
}