package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Limelight extends SubsystemBase {
    private NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
    private ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");

    public boolean validTargets = false;
    public double hOffset = 0; // -29.8 degrees to 29.8 degrees
    public double vOffset = 0; // -24.85 degrees to 24.85 degrees
    public double area = 0; // 0% of image to 100% of image

    private NetworkTableEntry valid_targets; 
    private NetworkTableEntry h_offset; 
    private NetworkTableEntry v_offset; 
    private NetworkTableEntry target_area;
    private NetworkTableEntry calculated_distance;

    // Upper hub height in inches
    private final double UPPER_HUB_HEIGHT = 104;
    // Center of the Limelight lens' height above ground in inches
    private final double CAMERA_HEIGHT = 10;
    // Camera angle in degrees (here we set it to radians to use in code)
    private final double CAMERA_ANGLE = 60;
    // Horizontal and vertical offset from the camera to the shooter
    private final double ROBOT_H_OFFSET = 0;

    private final Map<Double, Double[]> SPEEDS_MAP = new HashMap<Double, Double[]>() {{
        put(40.0, new Double[] { Constants.distanceBigSpeed, Constants.distanceSmallSpeed });
    }};
    

    public Limelight() {
        valid_targets = limelightTab.add("Sees targets", false).withWidget(BuiltInWidgets.kBooleanBox).getEntry();
        h_offset = limelightTab.add("Horizontal offset", 0).withWidget(BuiltInWidgets.kNumberBar)
            .withProperties(Map.of("min", -29.8, "max", 29.8))
            .withSize(2, 1)
            .getEntry();
        v_offset = limelightTab.add("Vertical offset", 0).withWidget(BuiltInWidgets.kNumberBar)
            .withProperties(Map.of("min", -24.85, "max", 24.85))
            .withSize(2, 1)
            .getEntry();
        target_area = limelightTab.add("Area", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
        calculated_distance = limelightTab.add("Calculated distance", 0).withWidget(BuiltInWidgets.kTextView)
            .withSize(2, 1)
            .getEntry();
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        // Read values
        validTargets = limelight.getEntry("tv").getDouble(0.0) == 1;
        hOffset = limelight.getEntry("tx").getDouble(0.0);
        vOffset = limelight.getEntry("ty").getDouble(0.0);
        area = limelight.getEntry("ta").getDouble(0.0);

        // Write values
        valid_targets.setBoolean(validTargets);
        h_offset.setDouble(hOffset);
        v_offset.setDouble(vOffset);
        target_area.setDouble(area);
        calculated_distance.setDouble(upperHubDistance());
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Returns the angle of the upper hub in degrees
    public double upperHubAngle() {
        return CAMERA_ANGLE + vOffset;
    }

    public double upperHubDistance() {
        double angleToGoal = upperHubAngle() * (Math.PI / 180.0);
        double distance = (UPPER_HUB_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToGoal);

        return distance - ROBOT_H_OFFSET;
    }

    public Double[] flywheelSpeeds() {
        Double difference = Double.MAX_VALUE;
        Double[] speeds = null;

        for (Double key : SPEEDS_MAP.keySet()) {
            if (Math.abs(key - vOffset) < difference) {
                difference = Math.abs(key - vOffset);
                speeds = SPEEDS_MAP.get(key);
            }
        }

        return speeds;
    }
}
