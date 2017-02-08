package org.usfirst.frc.team3255.testbot2017.subsystems;

import org.opencv.core.Mat;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem implements Runnable {

	private UsbCamera frontCamera = null;
	private UsbCamera rearCamera = null;
	
	private CameraServer cameraServer = null;
	
	private CvSink frontSink = null;
	private CvSink rearSink = null;
	
	private CvSource outputStream = null;
		
	private Thread visionThread = null;
	
	private boolean frontCamOn = true;
	
	public Vision() {
		visionThread = new Thread(this);
		visionThread.setDaemon(true);
		visionThread.start();
	}
	
	@Override
	public void run() {
		
		// ===== Initializations ===== //
		//Creates CameraServer
		cameraServer = CameraServer.getInstance();
		//Starts capture of the frontCamera
		frontCamera = cameraServer.startAutomaticCapture(0);
		//Sets resolution and frame rate of frontCamera
        frontCamera.setResolution(320, 240);
        frontCamera.setFPS(30);
        //Starts capture of the rearCamera
        rearCamera = cameraServer.startAutomaticCapture(1);
		//Sets resolution and frame rate of rearCamera
        rearCamera.setResolution(320, 240);
        rearCamera.setFPS(30);
        
        //Creates sinks (steams) and have them get video from their respective cameras
        frontSink = cameraServer.getVideo(frontCamera);
        rearSink = cameraServer.getVideo(rearCamera);
        //Creates outPutStream called "Switcher" which will be put on the dashboard
        outputStream = cameraServer.putVideo("Switcher", 320, 240);
        
        //Creates a mat to hold image
        Mat image = new Mat();

        
        				////======= Thread Loop ======////
		// This cannot be 'true'. The program will never exit if it is. This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		while (!Thread.interrupted()) {
			
			/*
			// Tell the CvSink to grab a frame from the camera and put it
			// in the source mat.  If there is an error notify the output.
			if (usbCameraSink.grabFrame(mat) == 0) {
				// Send the output the error.
				outputStream.notifyError("grabFrame failed: " + usbCameraSink.getError());
				// skip the rest of the current iteration
				continue;
			}
			*/
			
			/*
			// ===== Drawing on image ===== //
			// Put a rectangle on the image
			Imgproc.rectangle(image, new Point(100, 100), new Point(400, 400),
					new Scalar(255, 255, 255), 5);
			*/
			
			// ===== Camera Selection ==== //
			// Checks to see if boolean is set to true (changed in selecting methods)
			/* To avoid bandwidth & stream errors, the current camera
			FPS must be set to 0 and its sink must be disabled before
			the new camera's sink is enabled and its FPS is set to 30. */
			if(frontCamOn){
				rearCamera.setFPS(0);		//Sets FPS to 0 for rear camera
                rearSink.setEnabled(false); //Disables stream for rear camera 
                frontCamera.setFPS(30);		//Sets FPS to 30 for front camera
                frontSink.setEnabled(true); //Disables stream for rear camera
                frontSink.grabFrame(image); //Grabs the frames from front camera
              } 
			else{
            	frontCamera.setFPS(0);		//Sets FPS to 0 for front camera
                frontSink.setEnabled(false);//Disables stream for front camera 
                rearCamera.setFPS(30);		//Sets FPS to 30 for rear camera
                rearSink.setEnabled(true);	//Disables stream for rear camera
                rearSink.grabFrame(image);  //Grabs the frames from rear camera   
              }
			
			// ===== Output Steam to Dashboard ===== //
			outputStream.putFrame(image);	//Puts grabbed frames in output stream "Switcher"
		}
	}
	
	public void selectForwardCamera() {
		//Sets frontCamOn boolean to true
		frontCamOn = true;
	}
	
	public void selectRearCamera() {
		//Sets frontCamOn boolean to false
		frontCamOn = false;
	}

	@Override
	protected void initDefaultCommand() {
	}
}
