package frc.robot;

/*
The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
constants. This class should not be used for any other purpose.  All constants should be
declared globally (i.e. public static). Do not put anything functional in this class.

It is advised to statically import this class (or one of its inner classes) wherever the
constants are needed, to reduce verbosity.
*/

public final class Constants {
    public enum RobotMode {
        TESTING,
        MATCH
    }

    public static final RobotMode MODE = RobotMode.TESTING;

    public static final double intakeSpeed = 14.5 * 1000;
    public static final double conveyorSpeed = 10 * 1000;

    public static final double hangerFastSpeed = 12.5 * 1000;
    public static final double hangerSlowSpeed = 6 * 1000;
    
    public static final double hubBigSpeed = 5 * 1000;
    public static final double hubSmallSpeed = 11.5 * 1000;
    
    public static final double fenderBigSpeed = 9.89 * 1000;
    public static final double fenderSmallSpeed = 15.55 * 1000;
    
    public static final double distanceBigSpeed = 11 * 1000;
    public static final double distanceSmallSpeed = 11 * 1000;

    public static final double flywheelRatio = 833.0 / 530.0;

    public static final class Limelight {
        // Flywheel speed equation coefficients
        public static final double A = -0.0548;
        public static final double B = 1.45;
        public static final double C = -76.7;
        public static final double D = 11600;
        public static final double A2 = 1.08;
        public static final double B2 = -105;
        public static final double C2 = 11400;


        // Upper hub height in inches
        public static final double UPPER_HUB_HEIGHT = 104.0;
        // Center of the Limelight lens' height above ground in inches
        public static final double CAMERA_HEIGHT = 24.5;
        // Camera angle in degrees (here we set it to radians to use in code)
        public static final double CAMERA_ANGLE = 52.2;
        // Horizontal offset from the camera to the back bumper
        public static final double ROBOT_H_OFFSET = 10;
    }
}
