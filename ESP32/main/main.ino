#include <Arduino.h>
#include <WiFi.h>
#include <FirebaseESP32.h>
#include "FeatherShieldESP32Pinouts.h"
#include "Zanshin_BME680.h"

// Provide the token generation process info.
#include <addons/TokenHelper.h>

// Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

/* Define the WiFi credentials */
#define WIFI_SSID "nanashi-hvywxx9"
#define WIFI_PASSWORD "4XC;id/rUukJ"

#define API_KEY "AIzaSyA5L8TCPsduWWBVNbrAq9k4ZrDLPTbrEC4"

#define USER_EMAIL "sansnom01@proton.me"
#define USER_PASSWORD "sansnom01@proton.me"

#define DATABASE_URL "esp32-13749-default-rtdb.europe-west1.firebasedatabase.app" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

/** Define the database secret (optional)
 *
 * This database secret needed only for this example to modify the database rules
 *
 * If you edit the database rules yourself, this is not required.
 */
#define DATABASE_SECRET "DATABASE_SECRET"

#define DEVICE_ID "1"
#define DEVICE_NAME "Home"

/* Define the Firebase Data object */
FirebaseData fbdo;

/* Define the FirebaseAuth data for authentication data */
FirebaseAuth auth;

/* Define the FirebaseConfig data for config data */
FirebaseConfig config;

unsigned long dataMillis = 0;
int count = 0;

BME680_Class BME680;

struct SensorReading {
  int32_t temperature;
  int32_t humidity;
  int32_t pressure;
  int32_t gas;
};

void setup() {

    Serial.begin(115200);

    Serial.print(F("- Initializing BME680 sensor\n"));
    while (!BME680.begin(I2C_STANDARD_MODE)) {  // Start BME680 using I2C, use first device found
      Serial.print(F("-  Unable to find BME680. Trying again in 5 seconds.\n"));
      delay(5000);
    }

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(300);
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println();

    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

    /* Assign the api key (required) */
    config.api_key = API_KEY;

    /* Assign the user sign in credentials */
    auth.user.email = USER_EMAIL;
    auth.user.password = USER_PASSWORD;

    /* Assign the RTDB URL */
    config.database_url = DATABASE_URL;

    // Comment or pass false value when WiFi reconnection will control by your code or third party library e.g. WiFiManager
    Firebase.reconnectNetwork(true);

    // Since v4.4.x, BearSSL engine was used, the SSL buffer need to be set.
    // Large data transmission may require larger RX buffer, otherwise connection issue or data read time out can be occurred.
    fbdo.setBSSLBufferSize(4096 /* Rx buffer size in bytes from 512 - 16384 */, 1024 /* Tx buffer size in bytes from 512 - 16384 */);

    fbdo.setResponseSize(4096);

    /* Assign the callback function for the long running token generation task */
    config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h

    /* Initialize the library with the Firebase authen and config */
    Firebase.begin(&config, &auth);

    /** Now modify the database rules (if not yet modified)
     *
     * The user, <user uid> in this case will be granted to read and write
     * at the certain location i.e. "/UsersData/<user uid>".
     *
     * If you database rules has been modified, please comment this code out.
     *
     * The character $ is to make a wildcard variable (can be any name) represents any node key
     * which located at some level in the rule structure and use as reference variable
     * in .read, .write and .validate rules
     *
     * For this case $userId represents any <user uid> node that places under UsersData node i.e.
     * /UsersData/<user uid> which <user uid> is user UID.
     *
     * Please check your the database rules to see the changes after run the below code.
     */
    String var = "$userId";
    String val = "($userId === auth.uid && auth.token.premium_account === true && auth.token.admin === true)";
    Firebase.setReadWriteRules(fbdo, "", var, val, val, DATABASE_SECRET);

    Firebase.setString(fbdo, ("/devices/" + String(DEVICE_ID) + "/name").c_str(), DEVICE_NAME);

    pinMode(A0,OUTPUT);

    Serial.print(F("- Setting 16x oversampling for all sensors\n"));
    BME680.setOversampling(TemperatureSensor, Oversample16);
    BME680.setOversampling(HumiditySensor, Oversample16);
    BME680.setOversampling(PressureSensor, SensorOff);

    Serial.print(F("- Setting IIR filter to a value of 4 samples\n"));
    BME680.setIIRFilter(IIR4);

    Serial.print(F("- Disable gas measurement\n"));
    BME680.setGas(320, 0);
}



void loop() {
  if (millis() - dataMillis > 1000 && Firebase.ready()) {
      dataMillis = millis();
      
      if (count >= 150){
          count = 0;
      }
      // Firebase.ready() should be called repeatedly to handle authentication tasks.
    
      SensorReading sensorData;
      BME680.getSensorData(sensorData.temperature, sensorData.humidity, sensorData.pressure, sensorData.gas);  // Get readings

      String path = "/data-1";

      count++;

      Firebase.setInt(fbdo, "/latest", count);

      Serial.printf((String(count) + " | Device 1 - set temperature value : %s\n").c_str(), Firebase.setFloat(fbdo, path + "-" + count +"/temperature", sensorData.temperature / 100.0) ? "ok" : fbdo.errorReason().c_str());
      Serial.printf((String(count) + " | Device 1 - set humidity value : %s\n").c_str(), Firebase.setFloat(fbdo, path + "-" + count +"/humidity", sensorData.humidity / 1000.0) ? "ok" : fbdo.errorReason().c_str());

    }
    analogWrite(A5, 0);
}
