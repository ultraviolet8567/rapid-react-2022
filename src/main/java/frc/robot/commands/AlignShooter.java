package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;


public class AlignShooter extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final Limelight m_limelight;
    private final Shooter m_shooter;
    private Timer timer;
    private boolean aligned;

    public AlignShooter(Drivetrain drivetrain, Limelight limelight, Shooter shooter) {
        m_drivetrain = drivetrain;
        m_limelight = limelight;
        m_shooter = shooter;
        addRequirements(m_drivetrain);
        addRequirements(m_limelight);
        addRequirements(m_shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer = new Timer();

        aligned = false;
        m_drivetrain.getDifferentialDrive().stopMotor();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (aligned) {
            m_drivetrain.getDifferentialDrive().stopMotor();

            if (timer.get() >= 1.5) {
                RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kLeftRumble, 0);
                RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kRightRumble, 0);
            }
            else if (timer.get() >= 0.5) {
                m_shooter.setMode("Limelight");

                RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kLeftRumble, 0.5);
                RobotContainer.getInstance().getXboxController().setRumble(RumbleType.kRightRumble, 0.5);
            }
        }
        else {
            if (Math.abs(m_limelight.hOffset) < 1) {
                timer.start();
                aligned = true;
            }
            else {
                double kP = 0.02;
                double min = 0.2;
                double turn = kP * m_limelight.hOffset;

                if (m_limelight.hOffset > 0) { turn = turn + min; }
                else { turn = turn - min; }

                m_drivetrain.getDifferentialDrive().arcadeDrive(0.0, turn);
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.getDifferentialDrive().stopMotor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return timer.get() > 1.5;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
