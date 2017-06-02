
//including required packages
var functions = require('firebase-functions');
var weather = require('openweather-apis');
const admin = require('firebase-admin');
var moment = require('moment');
var momentZone = require('moment-timezone');
var every = require('schedule').every;
admin.initializeApp(functions.config().firebase);

// Get a database reference to our blog
var db = admin.database();


//Method which tracks the status changes of the device
exports.trackDevice = functions.database
    .ref('/{appID}/devices/{pushID}/status')
    .onWrite(event => {
        const deviceStatus = event.data.val()

        console.log("status changed " + deviceStatus)
        const promise = addTime(event.params.pushID, event.params.appID, deviceStatus)
        return promise
    })

//addTime function which sets the changes in the database
function addTime(deviceID, applicationID, currentStatus) {
    // do your job .
    var minute = momentZone().tz("Asia/Colombo").format("mm");
    var hour = momentZone().tz("Asia/Colombo").format("k");
    var month = momentZone().tz("Asia/Colombo").format("M");
    var day = momentZone().tz("Asia/Colombo").format("D");

    var report = db.ref("/report/" + applicationID + "/" + deviceID + "/" + month + "/" + day + "/" + hour);

    report.child(minute).set(currentStatus)
}



var currentLoc//currentLocation of the device

//setting variables related openweather package
weather.setAPPID('6b305548a0cfd94c7fa2bcbc99037db8');
weather.setLang('en');
weather.setUnits('metric');

//funntion which checks the weather status every 10 minutes for all the active devices
/*every('10m').do(function () {
    // do your job .

    var ref = db.ref()
    var mobileStatus
    var appId
    var deviceName
    var location
    var registrationToken

    ref.orderByChild("mobileNotification").on("child_added", function (snapshot) {
        mobileStatus = snapshot.val().mobileNotification;
        registrationToken = snapshot.val().notificationTokens;
        console.log("Reg Token" + registrationToken)

        appId = snapshot.key;
        if (mobileStatus) {
            ref = db.ref(appId + "/devices");
            ref.orderByChild("status").on("child_added", function (snapshot) {
                if (snapshot.val().status) {
                    ref = db.ref(appId + "/devices/" + snapshot.key);
                    ref.on("value", function (snapshot) {
                        deviceName = snapshot.val().deviceName
                        location = snapshot.val().location

                    });
                    weather.getSmartJSON(function (err, smart) {
                        var status = smart.weathercode
                        if (/^(2\d\d|300)/.test(status)) {
                            console.log("WEATHER HAZARD!" + deviceName + " LOC " + location)
                            return sendNotification(deviceName, location, registrationToken)
                        }

                    });
                }


            });
        }
    });



});

*/


//function which sendds the notifications for the devices according the passed parameters
function sendNotification(deviceName, currentLoc, registrationToken) {

    console.log("Successfully sent message:" + registrationToken);


    var payload = {
        notification: {
            title: "Device in Danger!!",
            body: "Your device " + deviceName + " at " + currentLoc + " is in DANGER!!"
        }
    };

    // Send a message to the device corresponding to the provided
    // registration token.
    admin.messaging().sendToDevice(registrationToken, payload)
        .then(function (response) {
            // See the MessagingDevicesResponse reference documentation for
            // the contents of response.
            console.log("Successfully sent message:", response);
        })
        .catch(function (error) {
            console.log("Error sending message:", error);
        });
}