/*******************************************************
 * Copyright (C) 2016 Mario Di Dio, Sara Craba 
 * See the LICENSE file for details.
 *******************************************************/

/* Include Libraries */
#include <Bridge.h>
#include <BridgeServer.h>
#include <BridgeClient.h>

/* Declare Constants */
#define RELAYON 0 //Relay is active LOW
#define RELAYOFF 1

/* Declare Global Variables/Objects */
BridgeServer server;
const uint8_t relayFirstPin = 2; // Arduino I/O Pin Connected to the first relay
const uint8_t nZapSupported = 5; // Number of ZAP outlet supported
const uint8_t nZapCommands = 2;  // ON/OFF comands
uint8_t zapIdCommandToPinMap[nZapSupported][nZapCommands];
const uint8_t minZapId = 1;
const uint8_t maxZapId = minZapId + nZapSupported - 1;
const uint8_t nRelaySupported = nZapSupported * nZapCommands;
const uint8_t signalingPin = 13;

/* Setup Phase */
void setup() 
{
    //Build ZAP Relay/Pin Map
    // 2 => ZAP1ON; 3=>ZAP1OFF
    // 4 => ZAP2ON; 5=>ZAP2OFF 
    // 6 => ZAP2ON; 7=>ZAP2OFF 
    // 8 => ZAP2ON; 9=>ZAP2OFF 
    // 10 => ZAP2ON; 11=>ZAP2OFF 
    for(uint8_t i = 0; i < nZapSupported; i++)
    {
        for(uint8_t j = 0; j < nZapCommands; j++)
        {
            uint8_t pinId = i*nZapCommands+(nZapCommands-j-1)+relayFirstPin;
            zapIdCommandToPinMap[i][j] = pinId;
        }
    }
  
    //Initialize Arduino pins so that relays are inactive during startup phase
    for(uint8_t i = 0; i < nRelaySupported; i++)
    {
        //Check to not overwrite signalingPin
        if((relayFirstPin + i) < signalingPin) 
        {
            pinMode(relayFirstPin + i, OUTPUT); 
            digitalWrite(relayFirstPin + i, RELAYOFF);
        }
    }

    //Wait for all the relays to be inactive
    delay(2000);

    //Initialize Signaling Pin
    pinMode(signalingPin, OUTPUT); 
    digitalWrite(signalingPin, LOW);

    //Start Bridge Communication and 
    //set to HIGH the signaling pin when ready
    Bridge.begin();
    digitalWrite(signalingPin, HIGH);

    //Set server to listen to localhost (on port 5555)
    server.listenOnLocalhost();
    server.begin();
}

/* Loop Phase */
void loop() 
{
  //Declare a Bridge Client
  BridgeClient client = server.accept();

  //Check if a new client has come available
  if (client) 
  {
    //Process the client request
    process(client);
    // Close the connection and free resources
    client.stop();
  }

  //Throttle the polling in order to save CPU
  delay(50);
}

void process(BridgeClient client) 
{
  //Read the command
  String command = client.readStringUntil('/');

  //"zap" command
  if (command == "zap") 
  {
    zapCommand(client);
  }

  //"signalingPin" command
  if (command == "signalingPin") 
  {
    signalingPinCommand(client);
  }
}

void zapCommand(BridgeClient client)
{
    //Read zapId number
    uint8_t zapId = client.parseInt();

    if(zapId >= minZapId && zapId <= maxZapId)
    {
        if(client.read() == '/')
        {
            //Read ZAP Command (ON/OFF)
            uint8_t zapCommand = client.parseInt();
        
            if(zapCommand == 0 || zapCommand == 1)
            {
              uint8_t pin = zapIdCommandToPinMap[zapId-1][zapCommand];
              
              //Activate Relay
              digitalWrite(pin, RELAYON);  
              
              //Delay for giving the relay the time to activate the 
              //RF transmitter
              delay(200);
              
              //Release Relay
              digitalWrite(pin, RELAYOFF);
              
              //Send feedback to client
              client.print(F("ZAP#"));
              client.print(zapId);
              client.print(F(" Command:"));
              client.println(zapCommand);
            }
            else
            {
                //Send feedback to client
                client.print(F("ZAP#"));
                client.print(zapId);
                client.println(F(" Command: Wrong Command Value"));
            }
        }
        else
        {
            //Send feedback to client
            client.print(F("ZAP#"));
            client.print(zapId);
            client.println(F(" ZapId Not Supported"));
        }
    }
    else
    {
        //Send feedback to client
        client.print(F("ZAP#"));
        client.print(zapId);
        client.println(F(" ZapId Not Supported"));
    }
}

void signalingPinCommand(BridgeClient client)
{
    //Read pin value
    uint8_t value = client.parseInt();
  
    if(value == 0 || value == 1)
    {
        //Set Signaling Pin to value
        digitalWrite(signalingPin, value);
      
        //Send feedback to client
        client.print(F("signalingPin"));
        client.print(F(" set to "));
        client.println(value);
    }
    else
    { 
        //Send feedback to client
        client.println(F("signalingPin Command: Wrong Command Value"));
    }
}
