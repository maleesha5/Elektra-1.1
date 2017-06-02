#include <FS.h>                   //this needs to be first, or it all crashes and burns...
#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed for library
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>          //https://github.com/tzapu/WiFiManager

//#include <ArduinoJson.h>          //https://github.com/bblanchon/ArduinoJson
// FirebaseRoom_ESP8266 is a sample that demo using multiple sensors
// and actuactor with the FirebaseArduino library.
#include <NTPClient.h>
#include <ESP8266WiFi.h>

#include <WiFiUdp.h>

// Set these to run example.
#define FIREBASE_HOST "elektra-f89b8.firebaseio.com"
#define FIREBASE_AUTH "drjgpAg4DCiG2cz2HnEUY8qOD1ARqov9NHunEULo"
#define WIFI_SSID "TP-LINK_CA14A0"
#define WIFI_PASSWORD "windows9"

double Voltage = 0;
double Amps = 0;
const int relay = 04;
double AmpsFinal = 0;
double VolFinal = 0;
double Power = 0;
float result;
const int sensorIn = A0;
int mVperAmp = 100; // use 100 for 20A Module and 66 for 30A Module

double VRMS = 0;
double AmpsRMS = 0;

WiFiUDP ntpUDP;

NTPClient timeClient(ntpUDP);


// You can specify the time server pool and the offset, (in seconds)
// additionaly you can specify the update interval (in milliseconds).
// NTPClient timeClient(ntpUDP, "europe.pool.ntp.org", 19800, 60000);

//define your default values here, if there are different values in config.json, they are overwritten.
//char mqtt_server[40];
//char mqtt_port[6] = "8080";
char blynk_token[34] = "-KlSYxM-NZillgZQvWgL";

//flag for saving data
bool shouldSaveConfig = false;

//callback notifying us of the need to save config
void saveConfigCallback () {
  Serial.println("Should save config");
  shouldSaveConfig = true;
}


void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  Serial.println();

  //clean FS, for testing
 // SPIFFS.format();

  //read configuration from FS json
  Serial.println("mounting FS...");

  if (SPIFFS.begin()) {
    Serial.println("mounted file system");
    if (SPIFFS.exists("/config.json")) {
      //file exists, reading and loading
      Serial.println("reading config file");
      File configFile = SPIFFS.open("/config.json", "r");
      if (configFile) {
        Serial.println("opened config file");
        size_t size = configFile.size();
        // Allocate a buffer to store contents of the file.
        std::unique_ptr<char[]> buf(new char[size]);

        configFile.readBytes(buf.get(), size);
        DynamicJsonBuffer jsonBuffer;
        JsonObject& json = jsonBuffer.parseObject(buf.get());
        json.printTo(Serial);
        if (json.success()) {
          Serial.println("\nparsed json");

         // strcpy(mqtt_server, json["mqtt_server"]);
        //  strcpy(mqtt_port, json["mqtt_port"]);
          strcpy(blynk_token, json["blynk_token"]);

        } else {
          Serial.println("failed to load json config");
        }
      }
    }
  } else {
    Serial.println("failed to mount FS");
  }
  //end read



  // The extra parameters to be configured (can be either global or just in the setup)
  // After connecting, parameter.getValue() will get you the configured value
  // id/name placeholder/prompt default length
  //WiFiManagerParameter custom_mqtt_server("server", "mqtt server", mqtt_server, 40);
 // WiFiManagerParameter custom_mqtt_port("port", "mqtt port", mqtt_port, 6);
  WiFiManagerParameter custom_blynk_token("ID", "device id", blynk_token, 32);

  //WiFiManager
  //Local intialization. Once its business is done, there is no need to keep it around
  WiFiManager wifiManager;

  //reset settings - for testing
  wifiManager.resetSettings();
  
  //set config save notify callback
  wifiManager.setSaveConfigCallback(saveConfigCallback);

  //set static ip
  //wifiManager.setSTAStaticIPConfig(IPAddress(10,0,1,99), IPAddress(10,0,1,1), IPAddress(255,255,255,0));
  
  //add all your parameters here
 // wifiManager.addParameter(&custom_mqtt_server);
 // wifiManager.addParameter(&custom_mqtt_port);
  wifiManager.addParameter(&custom_blynk_token);

  

  //set minimu quality of signal so it ignores AP's under that quality
  //defaults to 8%
  //wifiManager.setMinimumSignalQuality();
  
  //sets timeout until configuration portal gets turned off
  //useful to make it all retry or go to sleep
  //in seconds
  //wifiManager.setTimeout(120);

  //fetches ssid and pass and tries to connect
  //if it does not connect it starts an access point with the specified name
  //here  "AutoConnectAP"
  //and goes into a blocking loop awaiting configuration
  if (!wifiManager.autoConnect("ELEKTRA")) {
    Serial.println("failed to connect and hit timeout");
    delay(3000);
    //reset and try again, or maybe put it to deep sleep
    ESP.reset();
    delay(5000);
  }

  //if you get here you have connected to the WiFi
  Serial.println("connected...yeey :)");

  //read updated parameters
 //// strcpy(mqtt_server, custom_mqtt_server.getValue());
//  strcpy(mqtt_port, custom_mqtt_port.getValue());
  strcpy(blynk_token, custom_blynk_token.getValue());

  //save the custom parameters to FS
  if (shouldSaveConfig) {
    Serial.println("saving config");
    DynamicJsonBuffer jsonBuffer;
    JsonObject& json = jsonBuffer.createObject();
   // json["mqtt_server"] = mqtt_server;
    //json["mqtt_port"] = mqtt_port;
    json["blynk_token"] = blynk_token;

    File configFile = SPIFFS.open("/config.json", "w");
    if (!configFile) {
      Serial.println("failed to open config file for writing");
    }

    json.printTo(Serial);
    json.printTo(configFile);
    configFile.close();
    //end save
  }

  Serial.println("local ip");
  Serial.println(WiFi.localIP());

   Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  boolean  status = Firebase.getBool("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/status");

    pinMode(relay,OUTPUT);

  if(status){
    
digitalWrite(relay, HIGH);
  }else{


digitalWrite(relay, LOW);
  }

    timeClient.begin();

}

void loop() {
  


boolean  status = Firebase.getBool("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/status");


  if(status){
    
              Serial.println("OFF");
digitalWrite(relay, HIGH);
  }else{
      Serial.println(status);

          Serial.println("ON");
                Serial.println(status);

digitalWrite(relay, LOW);
  }
  
Voltage = getVPP();
 
VRMS = (Voltage/2.0) *0.707; 
AmpsRMS = (VRMS * 1000)/mVperAmp;
 
AmpsFinal =( AmpsRMS - 0.14 );
VolFinal = (VRMS - 0.0138) * 100 ; 
Power = AmpsFinal * VolFinal ; 

if(AmpsFinal < 0.0){
  
  AmpsFinal = 0.0;
  
}
if(VolFinal < 0.0){
  
  AmpsFinal = 0.0;
  
}
Serial.print(VolFinal);
Serial.println(" Vol RMS");
Serial.print(AmpsFinal);
Serial.println(" Amps RMS");
Serial.print(Power);
Serial.println(" POWER");
 //delay(1000);

  timeClient.update();

  int day = timeClient.getDay();
  int hr = timeClient.getHours();
  int minute = timeClient.getMinutes();
  unsigned long epochTime = timeClient.getEpochTime();
  
  String epochTm = String(epochTime);
  String strHr = String(hr);
  String strMin = String(minute);


  String ftime = timeClient.getFormattedTime();
  
  Serial.println(epochTime);



  Firebase.setFloat("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/voltage/"+epochTm,Power);
  Firebase.setFloat("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/latestWatt",Power);
  Firebase.setFloat("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/latestAmpere",AmpsFinal);
  Firebase.setFloat("b7c1fd1eaf3825f3/devices/-KlSYxM-NZillgZQvWgL/latestVoltage",VolFinal);

// Get the reading from the current sensor
delay(2000);

}



float getVPP()
{


  int readValue;             //value read from the sensor
  int maxValue = 0;          // store max value here
  int minValue = 1024;          // store min value here
  
   uint32_t start_time = millis();
   while((millis()-start_time) < 1000) //sample for 1 Sec
   {
       readValue = analogRead(sensorIn);
       // see if you have a new maxValue
       if (readValue > maxValue) 
       {
           /*record the maximum sensor value*/
           maxValue = readValue;
       }
       if (readValue < minValue) 
       {
           /*record the maximum sensor value*/
           minValue = readValue;
       }
   }
   
   // Subtract min from max
   result = ((maxValue - minValue) * 5.0)/1024.0;
      
   return result;
 }
