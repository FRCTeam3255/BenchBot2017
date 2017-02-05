package org.usfirst.frc.team3255.testbot2017.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem implements Runnable {

	private UsbCamera frontCamera = null;
	private UsbCamera rearCamera = null;
	
	private CameraServer cameraServer = null;
	
	private CvSink usbCameraSink = null;
	
	private CvSource outputStream = null;
		
	private Thread visionThread = null;
	
	public Vision() {
		visionThread = new Thread(this);
		visionThread.setDaemon(true);
		visionThread.start();
	}
	
	@Override
	public void run() {
		// create the front and rear cameras
		frontCamera = new UsbCamera("Front Camera", 1);
		rearCamera = new UsbCamera("Rear Camera", 2);
		
		// set the resolution of the front and rear cameras to 640x480
		frontCamera.setResolution(640, 480);
		rearCamera.setResolution(640, 480);

		// get the global instance of the camera server
		cameraServer = CameraServer.getInstance();
		
//		cameraServer.addCamera(frontCamera);
//	    cameraServer.addCamera(rearCamera);

	    // get a CvSink and connect it to the selected camera
	    usbCameraSink = new CvSink("Selected Camera Sink");
	    usbCameraSink.setSource(frontCamera);
	    cameraServer.addServer(usbCameraSink);

	    // create an output stream
		outputStream = CameraServer.getInstance().putVideo("Selected Camera", 640, 480);
				
		// Mats are very memory expensive. Lets reuse this Mat.
		Mat mat = new Mat();

		// This cannot be 'true'. The program will never exit if it is. This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		while (!Thread.interrupted()) {
			// Tell the CvSink to grab a frame from the camera and put it
			// in the source mat.  If there is an error notify the output.
			if (usbCameraSink.grabFrame(mat) == 0) {
				// Send the output the error.
				outputStream.notifyError("grabFrame failed: " + usbCameraSink.getError());
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
		usbCameraSink.setSource(frontCamera);
	}
	
	public void selectRearCamera() {
		usbCameraSink.setSource(rearCamera);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}
