/* To configure your WIFI connection, duplicate this file and name it "config.h" */

#define WIFI_SSID "WIFI_NAME"
#define WIFI_PASSWORD "WIFI_PASSWORD"

/* Firebase API Key */
#define API_KEY ""

// Replace with the new user in Authenticate
#define USER_EMAIL "example@example.com"
#define USER_PASSWORD "example@example.com"

#define DATABASE_URL "esp32-13749-default-rtdb.europe-west1.firebasedatabase.app" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

/** Define the database secret (optional)
  *
  * This database secret needed only for this example to modify the database rules
  *
  * If you edit the database rules yourself, this is not required.
  */
#define DATABASE_SECRET "DATABASE_SECRET"