package com.amazon.asksdk.address;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.asksdk.address.exceptions.DeviceAddressClientException;
import com.amazon.asksdk.address.exceptions.UnauthorizedException;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Permissions;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.slu.Slot; 

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

public class DeviceAddressSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(DeviceAddressSpeechlet.class);
    private static final String DOCTOR_CARD_TITLE = "DOCTOR FINDER";
    private static final String ALL_ADDRESS_PERMISSION = "read::alexa:device:all:address";
    private static final String WELCOME_TEXT_1 = "Hi! I am Doctor Finder. I can locate doctors in your area. What type of doctor are you looking for?.";
    private static final String WELCOME_TEXT_2 = "Hi! I am Doctor Finder. I can help you find doctors. What type of doctor are you looking for? " ;
    private static final String WELCOME_TEXT_3 = "Doctor Finder! reporting to duty!. I can help you find doctors. What type of doctor are you looking for? ";
    private static final String WELCOME_TEXT_4 = "Doctor Finder! reporting to duty!. I can locate doctors in your area. What type of doctor are you looking for? ";
    private static final String WELCOME_TEXT_5 = "Hey! I am Doctor Finder. You can ask me to locate doctors in your area.  What type of doctor are you looking for?";
    private static final String WELCOME_TEXT_6 = "Hello there! I am Doctor Finder. I can find hospitals nearby you.  What type of doctor are you looking for?  ";
    private static final String WELCOME_TEXT_7 = "Hello, human. I am Doctor Finder. I can help you look up doctors.  What type of doctor are you looking for?.";
    private static final String HELP_TEXT_1 = "You can ask me something like, \" find heart doctors near me \" . You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_2 = "You can ask me things like, \" suggest me a Pulmonologist \" . You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_3 = "I can help you with things like, \" find me a Radiologist \" . You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_4 = "You can ask me, \" Dentist nearby \" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_5 = "I can help you find doctors in your area, you can say something like, \" search for a Neurologist\" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_6 = "I can find any type of doctors near you. Please say what type of doctor you want. For example \" Allergists \" . You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_7 = "You can ask me, \" locate an eye doctor near me \" . You can exit by saying stop.  What type of doctor are you looking for?";
    private static final String HELP_TEXT_8 = "I can help you with things like, \" please suggest me a ENT doctor \" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_9 = "I can help you find doctors in your area, Please say, \" hospital's nearby \" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_10 = "You can ask me to locate, doctors in your area by saying,  \" doctors near me \" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String HELP_TEXT_11 = "I can tell you about good dctors near you \" please suggest me an liver doctor \" .  You can exit by saying stop. What type of doctor are you looking for?";
    private static final String UNHANDLED_TEXT = "Sorry i do not understand that, I am still learning. You can say \"help \" to get an idea of what i can do. You can exit by saying stop.";
    private static final String ERROR_TEXT = "There was an error with the skill. Please try again.";
    private static final String CANCEL_TEXT = "Goodbye, . Hope you get well soon ";
    private static final String[] addresses =  new String[3];//address array
    private static final Double[] phone = new Double[3];//ratings array
    private static final String[] poss = {" First. ", " Second. ", " Third. "};
    int k=0;

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) 
    {
        SessionStartedRequest sessionStartedRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onSessionStarted requestId={}, sessionId={}", sessionStartedRequest.getRequestId(),
            session.getSessionId());
    }
    /* 
    onLaunch handles the launch request and gives a user a random welcome text and keeps the session open
    */
    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        LaunchRequest launchRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onLaunch requestId={}, sessionId={}", launchRequest.getRequestId(),
            session.getSessionId());
            int x = (int)(Math.random()*((7-1)+1))+1;
            //random response chosen here
            switch(x)
            {
                case 1:
                    return getAskResponse(DOCTOR_CARD_TITLE, WELCOME_TEXT_1);
                case 2:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_2);
                case 3:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_3);
                case 4:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_4);
                case 5:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_5); 
                case 6:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_6);
                case 7:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_7);
                default:
                    return getAskResponse(DOCTOR_CARD_TITLE,  WELCOME_TEXT_3);
            }
    }
    /*
    Intent handlers
    GetDoctors: This intet handles the user requests for a doctor.
    getMoreInfoIntent: This intent handles the useres request asking more informatin about the doctors
    AMAZON.HelpIntent: Gives random help messages
    AMAZON.CancelIntent and AMAZON.StopIntent: end the session
    */
    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest intentRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onIntent requestId={}, sessionId={}", intentRequest.getRequestId(), session.getSessionId());
        Intent intent = intentRequest.getIntent();
        String intentName = getIntentName(intent);
        log.info("Intent received: {}", intentName);


        switch(intentName) {
            case "GetDoctors":
            //acces to address is required so this check
                Permissions permissions = session.getUser().getPermissions();
                if (permissions == null)
                {
                    log.info("The user hasn't authorized the skill. Sending a permissions card.");
                    return getPermissionsResponse();
                }
                //The slot "part" has information about the type of doctor the user requested
                try
                {
                    Slot nameSlot = intent.getSlot("part");
                    String part=nameSlot.getValue();//getting slot value
                    SystemState systemState = getSystemState(speechletRequestEnvelope.getContext());
                    String apiAccessToken = systemState.getApiAccessToken();
                    String deviceId = systemState.getDevice().getDeviceId();
                    String apiEndpoint = systemState.getApiEndpoint();
                    AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient( deviceId, apiAccessToken, apiEndpoint);
                    Address addressObject = alexaDeviceAddressClient.getFullAddress();
                    if (addressObject == null) 
                    {
                        return getAskResponse(DOCTOR_CARD_TITLE, ERROR_TEXT);
                    }
                    return getAddressResponse(addressObject.getCity(),addressObject.getPostalCode(),part);
                } 
                catch (UnauthorizedException e) 
                {
                    return getPermissionsResponse();
                }
                 catch (DeviceAddressClientException e)
                {
                    log.error("Device Address Client failed to successfully return the address.", e);
                    return getAskResponse(DOCTOR_CARD_TITLE, ERROR_TEXT);
                }
            case "getMoreInfoIntent":
                //Slot position has information about which doctor the user wants to know about
                Slot nameSlot = intent.getSlot("position");
                String pos=nameSlot.getValue();
                return getMoreInfoResponse(pos);//This function call handles getting more information about the doctors
            case "AMAZON.HelpIntent":
                int x = (int)(Math.random()*((11-1)+1))+1;
                //random help response is selected
                switch(x){
                    case 1:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_1);
                    case 2:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_2);
                    case 3:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_3);
                    case 4:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_4);
                    case 5:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_5); 
                    case 6:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_6);
                    case 7:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_7);
                    case 8:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_8);
                    case 9:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_9);
                    case 10:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_10);
                    case 11:
                        return getAskResponse(DOCTOR_CARD_TITLE, HELP_TEXT_11);
                }
            case "AMAZON.CancelIntent":
            case "AMAZON.StopIntent": 
                return getTellResponse(DOCTOR_CARD_TITLE, CANCEL_TEXT);//Ends session
            default:
                 return getAskResponse(DOCTOR_CARD_TITLE, UNHANDLED_TEXT);
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope)
    {
        SessionEndedRequest sessionEndedRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onSessionEnded requestId={}, sessionId={}", sessionEndedRequest.getRequestId(),session.getSessionId());
    }
    /*
    getAddressResponse function makes an api call to maps api and returns a response speechlet which has the doctor names
    It also stores detailes about the doctors in global varibale so that it can be accesed if the user wants more information about them
    */
    private SpeechletResponse getAddressResponse(String city, String zipCode, String part) {
        String speechText="", s="",docNames="";
        //check if the user desnt want a specific doctor and hasn't mentioned a value for the part slot
        if(part == null || part.isEmpty()){part="";}
        if(part.equalsIgnoreCase("find")){part="";}
        //passing api access key
        GooglePlaces client = new GooglePlaces("[API KEY GOES HERE]");
        int i=1;
        k=0;
        String nos="";
        try{
            //api call to locate doctors based on zip code
            s =part+" doctors near "+zipCode;
            List<Place> places = client.getPlacesByQuery(s, GooglePlaces.DEFAULT_RESULTS);
            //itterating through the results and saving the necessary information
            for(Place place : places) {
                if(i<4) {//Limiting the maximum number of results to 3.
                    docNames += " "+poss[i-1]+ place.getName()+"."; 
                    addresses[i-1] = place.getAddress(); 
                    phone[i-1] = place.getRating();
                    k++;
                }
                i++;
            }
            //building the response speech. the variable "k" has information about how many results were obtained
            if(k==1){nos="First ";}
            if(k==2){nos="First, or Second ";}
            if(k==3){nos="First, Second, or Third ";}
            speechText = "I found these "+part+" doctors near you. "+docNames+". For more information about them please say "+nos+". ";
        }
        catch(GooglePlacesException e) {
            i=1;
            k=0;
            try{
                //api call to locate doctors based on state if zipcode returns 0 results
                s =part+" doctors near "+city;
                List<Place> places = client.getPlacesByQuery(s, GooglePlaces.DEFAULT_RESULTS);
                for(Place place : places) {
                    if(i<4) {//Limiting the maximum number of results to 3.
                        docNames += " "+poss[i-1]+ place.getName()+"."; 
                        addresses[i-1] = place.getAddress(); 
                        phone[i-1] = place.getRating();
                        k++;
                    }
                    i++;
                }
                //building the response speech. the variable "k" has information about how many results were obtained
                if(k==1){nos="First ";}
                if(k==2){nos="First, or Second ";}
                if(k==3){nos="First, Second, or Third ";}
                speechText = "I found these "+part+" doctors near you. "+docNames+". For more information about them please say "+nos+". ";
            }
            catch(GooglePlacesException e1) {
                //) results found
                speechText = "Sorry, I did not find any "+part+" doctors near you";
                SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
                PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
                return SpeechletResponse.newTellResponse(speech, card);
                }
            }
            SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
            Reprompt reprompt = getReprompt(speech);
            return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    /*
    getMoreInfoResponse returns a speechlet containing details about the requested doctor
    */

    private SpeechletResponse getMoreInfoResponse(String position){
        String speechText="",add="";
        Double rating=0.0;
        String sp="";
        switch(position){
            case "first":
            case "1 st":
            case "one":
            case "1":
            case "1st":
                add=addresses[0];
                rating=phone[0];
                if(k==1){sp ="";}
                else if(k==2){sp = ". If you want information about the other doctor say, Second";}
                else if(k==3){sp = ". If you want information about the other doctors say Second, or Third";}
                break;
            case "second":
            case "2nd":
            case "two":
            case "2'nd":
            case "2":
                add=addresses[1];
                rating=phone[1];
                if(k==2){sp = ". If you want information about the other doctor say, First";}
                else if(k==3){sp = ". If you want information about the other doctors say First, or Third";}
                break;
            case "third":
            case "3 rd":
            case "three":
            case "3'rd":
            case "3":
            case "3rd":
                add=addresses[2];
                rating=phone[2];
                sp = ". If you want information about the other doctors say First, or Second";
                break;
            default:
                speechText = "Sorry, I did not find any information about the requested doctor";
                SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
                PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
                return SpeechletResponse.newTellResponse(speech, card);
        }
        
        speechText = "This doctor has a rating of "+Double.toString(rating)+". and the address is. "+add+sp;
        if(k>1){
            SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
            Reprompt reprompt = getReprompt(speech);
            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }
        else{
            SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
    
    private SpeechletResponse getPermissionsResponse() {
        String speechText = "You have not given this skill permissions to access your address. " +
            "Please give this skill permissions to access your address.";
        AskForPermissionsConsentCard card = new AskForPermissionsConsentCard();
        card.setTitle(DOCTOR_CARD_TITLE);
        Set<String> permissions = new HashSet<>();
        permissions.add(ALL_ADDRESS_PERMISSION);
        card.setPermissions(permissions);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        return SpeechletResponse.newTellResponse(speech, card);
    }
    private SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }
    private SimpleCard getSimpleCard(String title, String content) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);
        return card;
    }
    private String getIntentName(Intent intent) {
        return (intent != null) ? intent.getName() : null;
    }
    private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        return speech;
    }
    private Reprompt getReprompt(OutputSpeech outputSpeech) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return reprompt;
    }
    private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        Reprompt reprompt = getReprompt(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    private SpeechletResponse getTellResponse(String cardTitle, String speechText) {

        SimpleCard card = getSimpleCard(DOCTOR_CARD_TITLE, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        return SpeechletResponse.newTellResponse(speech, card);
    }
}