import time
import Adafruit_DHT
import RPi.GPIO as GPIO
# Set up GPIO for LEDs
GPIO.setmode(GPIO.BCM)
led_pin = 18  # Example GPIO pin for the LED
GPIO.setup(led_pin, GPIO.OUT)
# Initialize DHT11 sensor
dht_pin = 17  # GPIO pin for DHT11 data
sensor = Adafruit_DHT.DHT11
# Threshold temperature in Celsius
threshold_temp = 25.0
try:
    while True:
    # Read temperature and humidity from DHT11 sensor
        humidity, temperature = Adafruit_DHT.read_retry(sensor, dht_pin)
        if humidity is not None and temperature is not None:
    # Print temperature and humidity
    # Check if temperature crosses the threshold
            print(f"Temperature: {temperature:.2f}°C, Humidity: {humidity:.2f}%")
            if temperature > threshold_temp:
                GPIO.output(led_pin, GPIO.HIGH)  # Turn on the LED
            else:
                GPIO.output(led_pin, GPIO.LOW)  # Turn off the LED
        else:
            print("Failed to retrieve data from DHT11 sensor")
        time.sleep(2)  # Wait for 2 seconds before reading again
except KeyboardInterrupt:
    GPIO.cleanup()  # Clean up GPIO on program exit
