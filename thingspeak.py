import Adafruit_DHT as dht
from urllib.request import urlopen
myAPI = ''
ThingsURL = 'https://api.thingspeak.com/update?api_key=%s' % myAPI


def DHT11_data():
    hum1,temp1 = dht.read_retry(dht.DHT11, 23)
    return hum1,temp1


while True:
    hum, temp = DHT11_data()
    if isinstance(hum, float) and isinstance(temp, float):
        hum = '%.2f' % hum
        temp = '%.2f' % temp
        print(hum,temp)
    coms = urlopen(ThingsURL + '&field1=%s&field2=%s' % (temp, hum))
    print(coms.read())