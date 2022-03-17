package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
    private final Drivetrain m_drivetrain;
    private SlewRateLimiter filterLX;
    private SlewRateLimiter filterLY;
    private SlewRateLimiter filterRX;
    private SlewRateLimiter filterRY;
    
    public Drive(Drivetrain subsystem) {
        m_drivetrain = subsystem;
        addRequirements(m_drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        filterLX = new SlewRateLimiter(0.1);
        filterLY = new SlewRateLimiter(0.1);
        filterRX = new SlewRateLimiter(0.1);
        filterRY = new SlewRateLimiter(0.1);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        XboxController xbox = RobotContainer.getInstance().getXboxController();

        if (m_drivetrain.isSingleStickDrive()) {
            if (Math.abs(xbox.getLeftY()) > 0.1 || Math.abs(xbox.getLeftX()) > 0.1) {
                // Normal single stick drive
                m_drivetrain.getDifferentialDrive().arcadeDrive(
                    limiter("LeftY", -1),
                    limiter("LeftX", 1));
            }
            else {
                // Fine-tuning single stick drive
                m_drivetrain.getDifferentialDrive().arcadeDrive(
                    limiter("RightY", -0.5),
                    limiter("RightX", 0.5));
            }
        }
        else {
            // Split control drive
            m_drivetrain.getDifferentialDrive().arcadeDrive(
                limiter("LeftY", -1),
                limiter("RightX", 1));
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

    private double limiter(String joystick, double multiplier) {
        XboxController xbox = RobotContainer.getInstance().getXboxController();
        double value;
        
        switch(joystick) {
            case "LeftX":
                value = xbox.getLeftX() * multiplier;
                break;
            case "LeftY":
                value = xbox.getLeftY() * multiplier;
                break;
            case "RightX":
                value = xbox.getRightX() * multiplier;
                break;
            case "RightY":
                value = xbox.getRightY() * multiplier;
                break;
            default:
                value = 0;
                break;
        }

        return value;
    }
}
