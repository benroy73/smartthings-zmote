# smartthings-zmote

This is a device handler for SmartThings to use a zmote IR transmitter.

1. To use it go to https://graph.api.smartthings.com/ and create a device handler with this code.

2. Then create a new device with the type of zmote.

3. Finally edit the device to set the IP address and MAC address for your zmote device.

The code needs to be customized for the IR device you want to control.
I found the zmote commands by using the browser web inspector and examining what was sent when I use the standard zmote web interface here http://v1.zmote.io/
