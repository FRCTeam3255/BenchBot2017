package org.usfirst.frc.team3255.testbot2017.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem implements Runnable {

	private UsbCamera frontCamera = null;
	private UsbCamera rearCamera = null;
	
	private CameraServer cameraServer = null;

	private CvSource outputStream = null;
	
	private CvSink frontSink = null;
	private CvSink rearSink = null;
	private CvSink selectedSink = null;
	
	private Thread visionThread = null;
	
	public Vision() {
		// create the front and rear cameras
		frontCamera = new UsbCamera("Front Camera", 1);
		rearCamera = new UsbCamera("Rear Camera", 2);
		
		// set the resolution of the front and rear cameras to 640x480
		frontCamera.setResolution(640, 480);
		rearCamera.setResolution(640, 480);
		
		// get the global instance of the camera server
		cameraServer = CameraServer.getInstance();
		
		// add the front and rear cameras to the camera server
		cameraServer.startAutomaticCapture(frontCamera);
		cameraServer.startAutomaticCapture(rearCamera);
		
		// create an output stream
		outputStream = cameraServer.putVideo("Rectangle", 640, 480);
		
		// get the sinks for the front and rear camera
		frontSink = cameraServer.getVideo(frontCamera);
		rearSink = cameraServer.getVideo(rearCamera);
		
		selectForwardCamera();

		visionThread = new Thread(this);
		visionThread.setDaemon(true);
		visionThread.start();
	}
	
	@Override
	public void run() {
		// Get the UsbCamera from CameraServer
//		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(1);
//		// Set the resolution
//		camera.setResolution(640, 480);

//		// Get a CvSink. This will capture Mats from the camera
//		CvSink cvSink = CameraServer.getInstance().getVideo(camera);
		// Setup a CvSource. This will send images back to the Dashboard
//		CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

		// Mats are very memory expensive. Lets reuse this Mat.
		Mat mat = new Mat();

		// This cannot be 'true'. The program will never exit if it is. This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		while (!Thread.interrupted()) {
			// Tell the CvSink to grab a frame from the camera and put it
			// in the source mat.  If there is an error notify the output.
			if (selectedSink.grabFrame(mat) == 0) {
				// Send the output the error.
				outputStream.notifyError(selectedSink.getError());
				// skip the rest of the current iteration
				continue;
			}
			// Put a rectangle on the image
			Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
					new Scalar(255, 255, 255), 5);
			// Give the output stream a new image to display
			outputStream.putFrame(mat);
		}
	}
	
	public void selectForwardCamera() {
		selectedSink = frontSink;
	}
	
	public void selectRearCamera() {
		selectedSink = rearSink;		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
