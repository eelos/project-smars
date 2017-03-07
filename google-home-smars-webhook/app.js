/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */

'use strict';

process.env.DEBUG = 'actions-on-google:*';
let Assistant = require('actions-on-google').ApiAiAssistant;
let express = require('express');
let bodyParser = require('body-parser');

let app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json({type: 'application/json'}));

// we comunicate with arduino with post requests
var request = require('request');
// require basic auth
var auth = require('basic-auth');
// configuration file
var config = require('./configurations/config');

app.post('/', function (req, res) {
	// check autentication
	var user = auth(req)
	if (user.name === config.auth.user && user.pass === config.auth.password) {
		const assistant = new Assistant({request: req, response: res});
		
		// fulfill action business logic
		function responseHandler (assistant) {
			var multiple = req.body.result.parameters.multiple_lights;
			var single = req.body.result.parameters.single_light;
			var light = req.body.result.parameters.lightNumber;
			
			var address = 'http://' + config.server.host + ':' + config.server.port + '/arduino/zap/';
			var on = '/1';
			var off =  '/0';
			
			switch(multiple){
				case 'bed':
			  		request.post( address + '1' + off);
			  		request.post( address + '2' + off);
			  		request.post( address + '3' + off);
			  		request.post( address + '4' + on);
			  		request.post( address + '5' + off);
					break;
				case 'tv':
			  		request.post( address + '1' + on);
			  		request.post( address + '2' + on);
			  		request.post( address + '3' + off);
			  		request.post( address + '4' + off);
			  		request.post( address + '5' + off);
					break;
				case 'goodnight':
			  		request.post( address + '1' + off);
			  		request.post( address + '2' + off);
			  		request.post( address + '3' + off);
			  		request.post( address + '4' + off);
			  		request.post( address + '5' + off);
					break;
				case 'christmas':
			  		request.post( address + '1' + on);
			  		request.post( address + '2' + on);
			  		request.post( address + '3' + on);
			  		request.post( address + '4' + on);
			  		request.post( address + '5' + on);
					break;
			}
			switch(single){
				case 'on':
					for ( var i = 0; i < light.length; i++ ) {
						switch(light[i]){
							case '1':
								request.post( address + '1' + on);
								break;
							case '2':
								request.post( address + '2' + on);
								break;
							case '3':
								request.post( address + '3' + on);
								break;
							case '4':
								request.post( address + '4' + on);
								break;
							case '5':
								request.post( address + '5' + on);
								break;
						}
					}
					break;
				case 'off':
					for ( var i = 0; i < light.length; i++ ) {
						switch(light[i]){
							case '1':
								request.post( address + '1' + off);
								break;
							case '2':
								request.post( address + '2' + off);
								break;
							case '3':
								request.post( address + '3' + off);
								break;
							case '4':
								request.post( address + '4' + off);
								break;
							case '5':
								request.post( address + '5' + off);
								break;
						}
					}
			}
		 	assistant.tell('We swear to serve the master of the Precious.');
		};
 
		assistant.handleRequest(responseHandler);
		
	} else {
		console.log('User not autenticated');
		res.send('Bad user/pass');
	}
});

if (module === require.main) {
  // Start the server
  let server = app.listen(process.env.PORT || 8080, function () {
    let port = server.address().port;
    console.log('App listening on port %s', port);
  });
}

module.exports = app;
