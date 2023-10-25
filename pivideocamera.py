from picamera import PiCamera
from time import sleep

camera = PiCamera()
camera.vflip = True
camera.start_preview()
camera.start_recording('/home/pi/vid.h264')
sleep(5)
camera.resolution(600,900)
camera.stop_recording()
camera.stop_preview()