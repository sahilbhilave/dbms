from piphotocamera import PiCamera
from time import sleep

camera = PiCamera()
camera.start_preview()

camera.vflip = True
sleep(10)
camera.capture('/home/pi/Desktop/test_n.png')
camera.stop_preview()