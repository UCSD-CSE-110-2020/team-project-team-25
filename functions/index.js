const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendChatNotifications = functions.firestore
   .document('team/requests/{username}/{requestId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       // don't spam the requester with their own notifications
       if(document.target.email !== context.params.username){
         return 0;
       }
       var message = {
         notification: {
           title: document.requester.name + ' sent you a Teammate request.',
           body: "Tap this to open Walkstatic and send them a reply."
         },
         topic: context.params.username.replace("@", "")
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });
