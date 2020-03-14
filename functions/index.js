const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const firestore = admin.firestore();

function onResponse(snap, context){
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
}

exports.sendTeammateRequestNotifications = functions.firestore
   .document('team/requests/{username}/{requestId}')
   .onCreate((snap, context) => onResponse(snap, context));

exports.sendTeammateRequestUpdateNotifications = functions.firestore
   .document('team/requests/{username}/{requestId}')
   .onUpdate((snap, context) => onResponse(snap, context));

var responseToRun = function(document, proposal, responseMsg){
     var responseString = "";
       if(document.response === "GOING"){
            responseString = document.user.name + " can go to your run!";
       } else if(document.response === "BAD_TIME"){
            responseString = "The time is a bad time for " + document.user.name;
       } else if(document.response === "NOT_GOOD"){
            responseString = "The route is a bad route for " + document.user.name;
       }
       var message = {
         notification: {
           title: document.user.name + ' ' + responseMsg + ' ' + proposal.run.name,
           body: responseString
         },
         topic: proposal.author.email.replace("@", "")
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

exports.sendScheduledRunResponseCreatedNotifications = functions.firestore
   .document('team/proposedRuns/responses/{responseId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {

        return firestore.collection('team').doc('proposals').get().then((proposal) => {
            if(proposal.exists){
                return responseToRun(document, proposal.data(), "responded to");
            }
            return "failed to read proposal"
        });
     }

     return "document was null or emtpy";
   });

exports.sendScheduledRunResponseUpdatedNotifications = functions.firestore
   .document('team/proposedRuns/responses/{responseId}')
   .onUpdate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;
     console.log("reading changed document at: ", context.params.responseId);
     var docRef = firestore.collection('team').doc('proposedRuns');
     docRef = docRef.collection('responses').doc(context.params.responseId);
     return docRef.get().then((document) => {
                 if(document.exists){
                      return firestore.collection('team').doc('proposals').get().then((proposal) => {
                                 if(proposal.exists){
                                     return responseToRun(document.data(), proposal.data(), "changed response to");
                                 }
                                 return "failed to read proposal"
                             });
                 }
                 return "failed to read changed document"
             });
   });
