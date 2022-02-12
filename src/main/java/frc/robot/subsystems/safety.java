package frc.robot.subsystems;

public class safety {
    
    int motor1 = 5;
    int motor2 = 5; 
    int motor3 = 5;
    int motor4 = 5;
    int encoder1 = 4;
    int encoder2 = 4;
    int encoder3 = 4;
    int encoder4 = 4;
    int motors_array[] = {motor1, motor2, motor3, motor4};
    int encoders_array[] = {encoder1, encoder2, encoder3, encoder4};
    final double max_resistance = 3;
    double resistance = 0;
    //these are not real values; they are a placeholder for the real ones and they will be removed later
    private void anti_crash(){
        //runs for every motor
        for(int i:motors_array) {
            resistance = Math.abs(motors_array[i] - encoders_array[i]);
            if(resistance > max_resistance) {
                motor1 = 0;
                motor2 = 0; 
                motor3 = 0;
                motor4 = 0; 
            //finds the resistance and disables the motors if the restance is above the limit
            }
        }
    }
}
