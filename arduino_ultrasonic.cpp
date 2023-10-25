const int trigPin = 9;
const int LED = 11;
const int echoPin = 10;

long duration;
int distance;

void setup()
{
    Serial.println("Started");
    pinMode(trigPin,OUTPUT);
    pinMode(echoPin,INPUT);
    pinMode(LED,OUTPUT);
    Serial.begin(4800);
}


void loop()
{
    digitalWrite(trigPin,LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin,HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin,LOW);

    duration = pulseIn(echoPin,HIGH);

    distance = duration * 0.034 / 2;

    Serial.print("Distance : ");
    Serial.println(distance);
}