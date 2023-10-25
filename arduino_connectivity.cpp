int SensorPin = 13;
int LedPin = 2;
int BuzzerPin = 3;
void setup()
{
    pinMode(LedPin, OUTPUT);
    pinMode(BuzzerPin, OUTPUT);
    pinMode(SensorPin, INPUT);
    Serial.begin(9600);
}
void loop()
{
    int SensorValue = digitalRead(SensorPin);
    Serial.print("SensorPin: ");
    Serial.println(SensorValue);
    delay(100);
    if (SensorValue == 1)
    {
        digitalWrite(LedPin, HIGH);
        digitalWrite(BuzzerPin, LOW);
    }
    else
    {
        digitalWrite(LedPin, LOW);
        digitalWrite(BuzzerPin, HIGH);
    }
}