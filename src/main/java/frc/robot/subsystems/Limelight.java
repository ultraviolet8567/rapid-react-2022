package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
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
    public NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
    private ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");

    public boolean validTargets = false;
    public double hOffset = 0; // -29.8 degrees to 29.8 degrees
    public List<Double> hOffsetList = new ArrayList<>(List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
    public double vOffset = 0; // -24.85 degrees to 24.85 degrees
    public double area = 0; // 0% of image to 100% of image

    private NetworkTableEntry valid_targets; 
    private NetworkTableEntry h_offset; 
    private NetworkTableEntry v_offset; 
    private NetworkTableEntry target_area;
    private NetworkTableEntry calculated_distance;
    private NetworkTableEntry calculated_speed;
    

    public Limelight() {
        valid_targets = limelightTab.add("Sees targets", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 1)
            .getEntry();
        h_offset = limelightTab.add("Horizontal offset", 0).withWidget(BuiltInWidgets.kNumberBar)
            .withProperties(Map.of("min", -29.8, "max", 29.8))
            .withSize(2, 1)
            .withPosition(0, 0)
            .getEntry();
        v_offset = limelightTab.add("Vertical offset", 0).withWidget(BuiltInWidgets.kNumberBar)
            .withProperties(Map.of("min", -24.85, "max", 24.85))
            .withSize(2, 1)
            .withPosition(8, 0)
            .getEntry();
        target_area = limelightTab.add("Area", 0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(1, 1)
            .getEntry();
        calculated_distance = limelightTab.add("Calculated distance", 0).withWidget(BuiltInWidgets.kTextView)
            .withSize(2, 1)
            .withPosition(8, 1)
            .getEntry();
        calculated_speed = Shuffleboard.getTab("Shooter").add("Calculated big velocity", 0).withWidget(BuiltInWidgets.kTextView)
            .withSize(2, 1)
            // .withPosition(6, 2)
            .getEntry();

        limelight.getEntry("ledMode").setDouble(0);
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
        calculated_speed.setDouble(calculatedSpeeds()[0] / 1000);

        hOffsetList.remove(0);
        hOffsetList.add(hOffset);
    }

    // This method will be called once per scheduler run when in simulation
    @Override
    public void simulationPeriodic() {
    }

    // Put methods for controlling this subsystem here. Call these from Commands.

    // Returns the angle of the upper hub in degrees
    public double upperHubAngle() {
        return Constants.Limelight.CAMERA_ANGLE + vOffset;
    }

    public double upperHubDistance() {
        double angleToGoal = upperHubAngle() * (Math.PI / 180.0);
        double distance = (Constants.Limelight.UPPER_HUB_HEIGHT - Constants.Limelight.CAMERA_HEIGHT) / Math.tan(angleToGoal);

        return distance - Constants.Limelight.ROBOT_H_OFFSET;
    }

    public double[] calculatedSpeeds() {
        double x = hOffsetList.stream().mapToDouble(val -> val).average().orElse(0.0);
        // double big = Constants.Limelight.A2 * Math.pow(x, 2) + Constants.Limelight.B2 * x + Constants.Limelight.C2;
        double big = Constants.Limelight.A * Math.pow(x, 3) + Constants.Limelight.B * Math.pow(x, 2) + Constants.Limelight.C * x + Constants.Limelight.D;
        double small = big * Constants.flywheelRatio;

        return new double[] { big, small };
    }
}
