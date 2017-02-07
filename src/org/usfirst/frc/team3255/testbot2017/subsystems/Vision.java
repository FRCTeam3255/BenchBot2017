package org.usfirst.frc.team3255.testbot2017.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3255.testbot2017.OI;

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
		
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera1.setResolution(320, 240);
        camera1.setFPS(30);
        UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        camera2.setResolution(320, 240);
        camera2.setFPS(30);
        
        CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
        CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
        CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
        
        Mat image = new Mat();

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
			
			// Put a rectangle on the image
			Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
					new Scalar(255, 255, 255), 5);
			// Give the output stream a new image to display
			 */
			
			if(frontCamOn){
                cvSink2.setEnabled(false);
                cvSink1.setEnabled(true);
                cvSink1.grabFrame(image);
              } else{
                cvSink1.setEnabled(false);
                cvSink2.setEnabled(true);
                cvSink2.grabFrame(image);     
              }
			
			outputStream.putFrame(image);
		}
	}
	
	public void selectForwardCamera() {
		frontCamOn = true;
		
		/*
		usbCameraSink.setEnabled(false);
		usbCameraSink.setSource(frontCamera);
		usbCameraSink.setEnabled(true);
		*/
	}
	
	public void selectRearCamera() {
		frontCamOn = false;
		/*
		usbCameraSink.setEnabled(false);
		usbCameraSink.setSource(rearCamera);
		usbCameraSink.setEnabled(true);
		*/
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}
