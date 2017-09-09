/**
 *  zmote device handler
 *
 * 	Author:
 *   Ben Roy
 *
 *  Copyright 2017
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */

metadata {
    definition (name: "zmote wi-fi universal remote", namespace: "benroy", author: "Ben Roy") {
        capability "Bridge"
        command "amp_power"
        command "amp_sleep"
        command "amp_aux"
        command "amp_vol_down"
        command "amp_vol_up"
    }
    preferences {
        input("zmote_ip", "string",
            title: "zmote IP address",
            description: "IP address of your zmote",
            required: true,
            displayDuringSetup: true
        )
        input("zmote_uuid", "string",
            title: "zmote UUID",
            description: "UUID of your zmote",
            required: true,
            displayDuringSetup: true
        )
    }

    simulator {}

    tiles(scale: 2) {
        standardTile("icon", "icon", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
			state "default", label: "OK"
		}
        standardTile("power", "device.power", inactiveLabel: false, width: 2, height: 2, decoration: "flat") {
            state "default", label: 'Amp Power', action: "amp_power"
        }
        standardTile("sleep", "device.sleep", inactiveLabel: false, width: 2, height: 2, decoration: "flat") {
            state "default", label: 'Amp Sleep', action: "amp_sleep"
        }
        standardTile("aux", "device.aux", inactiveLabel: false, width: 2, height: 2, decoration: "flat") {
            state "default", label: 'Amp AUX', action: "amp_aux"
        }
        standardTile("vol_down", "device.voldown", inactiveLabel: false, width: 2, height: 2, decoration: "flat") {
            state "default", label: 'Amp Vol Down', action: "amp_vol_down"
        }
        standardTile("vol_up", "device.volup", inactiveLabel: false, width: 2, height: 2, decoration: "flat") {
            state "default", label: 'Amp Vol Up', action: "amp_vol_up"
        }
        main (["icon"])
        details(["power", "sleep", "aux", "vol_down", "vol_up"])
    }
}

def amp_power() {
    log.debug "Sending power command to zmote"
    send('sendir,1:1,1164,38000,2,1,343,171,21,22,21,65BCCCCBCBCBBBBCCCCCCBBBBBBBBCCC21,1672')
}

def amp_sleep() {
    log.debug "Sending sleep command to zmote"
    send('sendir,1:1,7930,38000,2,1,343,171,21,22,21,65BCCCCBCBCBBBBCCCCBCBCBBBBCBCBC21,1672')
}

def amp_aux() {
    log.debug "Sending aux input command to zmote"
    send('sendir,1:1,6486,38000,2,1,343,171,21,22,21,65BCCCCBCBCBBBBCCCCBCBBBBBBCBCCC21,1672')
}

def amp_vol_down() {
    log.debug "Sending vol down command to zmote"
    send('sendir,1:1,7593,38000,2,1,343,171,21,22,21,65BCCCCBCBCBBBBCCCBCCBBBBBCBBCCC21,1672')
}

def amp_vol_up() {
    log.debug "Sending vol up command to zmote"
    send('sendir,1:1,9457,38000,2,1,343,171,21,22,21,65BCCCCBCBCBBBBCBCBCCBBBCBCBBCCC21,1672')
}


// Send command to the zmote
def send(zmote_command) {
    log.debug "PUT http://$zmote_ip:80/v2/$zmote_uuid"
    log.debug "${zmote_command}"


    def headers = [:]
    headers.put("HOST", "$zmote_ip:80")
    headers.put("Content-Type", "application/json")
    def hubAction = new physicalgraph.device.HubAction(
        [
            method: "PUT",
            headers: headers,
            path: "/v2/$zmote_uuid",
            body: "${zmote_command}"
        ],
        null,
        [callback: calledBackHandler]
    )
    hubAction
}

// handle any response from the zmote here
void calledBackHandler(physicalgraph.device.HubResponse hubResponse) {
    log.debug "Entered calledBackHandler()..."
    def body = hubResponse.xml
    log.debug "${hubResponse}"
    log.debug "body in calledBackHandler() is: ${body}"
}
