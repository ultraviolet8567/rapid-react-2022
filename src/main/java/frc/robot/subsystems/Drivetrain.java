// ROBOTBUILDER TYPE: Subsystem.

package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS



public class Drivetrain extends SubsystemBase {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private PWMSparkMax rightFront;
    private PWMSparkMax rightBack;
    private MotorControllerGroup rightGroup;
    private PWMSparkMax leftFront;
    private PWMSparkMax leftBack;
    private MotorControllerGroup leftGroup;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Drivetrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        rightFront = new PWMSparkMax(12);
        addChild("RightFront",rightFront);
        rightFront.setInverted(false);

        rightBack = new PWMSparkMax(3);
        addChild("RightBack",rightBack);
        rightBack.setInverted(false);

        rightGroup = new MotorControllerGroup(rightFront, rightBack  );
        addChild("RightGroup",rightGroup);


        leftFront = new PWMSparkMax(7);
        addChild("LeftFront",leftFront);
        leftFront.setInverted(true);

        leftBack = new PWMSparkMax(11);
        addChild("LeftBack",leftBack);
        leftBack.setInverted(true);

        leftGroup = new MotorControllerGroup(leftFront, leftBack  );
        addChild("LeftGroup",leftGroup);
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() { 
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.
}
