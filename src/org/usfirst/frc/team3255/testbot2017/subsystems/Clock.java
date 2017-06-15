package org.usfirst.frc.team3255.testbot2017.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Clock extends Subsystem {
	
	public Clock(){
		
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public double getTimeRemaining() {
		return 150-Timer.getMatchTime();
	}
	
	public boolean isMatchEnding(){
		return (getTimeRemaining() <= 30);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

