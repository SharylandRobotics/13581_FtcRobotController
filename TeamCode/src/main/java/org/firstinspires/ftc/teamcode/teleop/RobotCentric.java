package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.RobotHardware;

@TeleOp (name = "Robot Centric Drive", group = "Robot")

public class RobotCentric extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware. Prefix any hardware function with "robot." to
    // access this class.
    RobotHardware robot = new RobotHardware(this);

    @Override
    public void runOpMode() {
        double axial = 0;
        double lateral = 0;
        double yaw = 0;
        boolean on = false;
//        double arm = 0;
//        double handOffset = 0;

        // initialize all the hardware, using the hardware class. See how clean and simple this is?
        robot.init();

        // Send telemetry message to signify robot waiting;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forward, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            axial = -gamepad1.left_stick_y; // Note: pushing stick forward gives negative value
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;

            // While right bumper is held slow mode is activated
            if (gamepad1.right_stick_button) {
                axial = -gamepad1.left_stick_y * 0.25; // Note: pushing stick forward gives negative value
                lateral = gamepad1.left_stick_x * 0.25;
                yaw = gamepad1.right_stick_x * 0.25;
            }

            if (gamepad1.a) {
                robot.intake.setPower(robot.INTAKE_COLLECT);
            } else if (gamepad1.x) {
                robot.intake.setPower(robot.INTAKE_OFF);
            } else if (gamepad1.b) {
                robot.intake.setPower(robot.INTAKE_DEPOSIT);
            }

            if (gamepad1.y) {
                on = !on;
                if (on) {
                    robot.intake2.setPosition(1.0/10);
                } else if (on == false) {
                    robot.intake2.setPosition(0.0);
                }

            }

            if (gamepad1.dpad_down) {
                robot.leftWrist.setPosition(robot.LEFT_WRIST_INTAKE);
            } else if (gamepad1.dpad_up) {
                robot.leftWrist.setPosition(robot.LEFT_WRIST_SCORE);
            }
            if (gamepad1.right_bumper) {
                robot.rightSlide.setPower(robot.RIGHT_SLIDE_EXTEND);
                robot.leftSlide.setPower(robot.LEFT_SLIDE_EXTEND);
            } else if (gamepad1.left_bumper ) {
                robot.rightSlide.setPower(-robot.RIGHT_SLIDE_EXTEND);
                robot.leftSlide.setPower(-robot.LEFT_SLIDE_EXTEND);
            } else {
                robot.rightSlide.setPower(0);
                robot.leftSlide.setPower(0);
            }

            if (gamepad1.right_trigger > 0) {
                robot.vSlidePosition= robot.VSLIDE_SCORE_SAMPLE_HIGH;
            }

            if (gamepad1.left_trigger > 0) {
                robot.vSlidePosition= robot.VSLIDE_START_POSITION;
            }

            // Combine drive and turn for blended motion. Use RobotHardware class.
            robot.driveRobot(axial, lateral, yaw);
            robot.setvSlideDrivePosition();

            if (((DcMotorEx) robot.vSlideDrive).isOverCurrent()){
                telemetry.addLine("MOTOR EXCEEDED CURRENT LIMIT!");
            }

            sleep(50);
        }
    }
}