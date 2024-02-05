# PEV

## Prerequisites

- Software
  - Android Studio
  - Docker
  - Python 3
  - VTube Studio: https://denchisoft.com/
- Hardware
  - An Android phone
  - A Ubuntu server

## Build

### Mobile device

- change the IP address at [line 70](https://github.com/hwsel/PEV/blob/main/android/StreamPack/demos/camera2/src/main/java/io/github/thibaultbee/streampack/camera2/ui/fragments/CameraFragment.kt#L70) to your server.
- Compile the project and install the application `camera2` to the phone.

### Server

- Content server
  - Run the docker image `docker run --rm -it -p 1935:1935 -p 1985:1985 -p 8080:8080 -p 10080:10080/udp ossrs/srs:5 ./objs/srs -c conf/srt.conf`.
  - Config the firewall if needed to allow the traffic through port 10080.
- Rendering server
  - Get and run VTube Studio from Steam
  - Config the VTube Studio to the virtual camera
  - Run the [script](https://github.com/hwsel/PEV/blob/main/server/virtual_camera/virtual_camera.py) to receive from the stream and push it to the virtual camera
