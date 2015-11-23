This is a fork of [lwansbrough's react-native-camera](https://github.com/lwansbrough/react-native-camera) that adds the missing support for android.
However this support is not yet production-ready and missing many important features. Please take a look at the properties and methods below to see the currently implemented features and take a look at the roadmap to see what is planned.

## Getting started

* `npm install https://github.com/timmh/react-native-camera.git`
* add to your `settings.gradle`:
```
include ':com.lwansbrough.ReactCamera'
project(':com.lwansbrough.ReactCamera').projectDir = new File(settingsDir, '../node_modules/react-native-camera/android')
```
* add to your `app/build.gradle`:
```
dependencies {
	...
	compile project(':com.lwansbrough.ReactCamera')
}
```
* add to your `MainActivity.java`:
  * `import com.facebook.react.CompositeReactPackage;`
  * `import com.lwansbrough.ReactCamera.ReactCameraPackage;`
  * in `onCreate`:
  ```
  mReactInstanceManager = ReactInstanceManager.builder()
	...
	.addPackage(new ReactCameraPackage(this))
	...
  ```

* profit
```
var Camera = require('react-native-camera'); //require the camera component
...
render () {
	return (
		<Camera style={{width: 200, height: 200}}></Camera>
	);
}
```
Or take a look at the [example project](https://github.com/timmh/react-native-camera/tree/master/example)


## Properties
These properties are a subset of the original ones. The goal is to implement all of them to get the same api on both platforms.


#### `captureTarget`

Values: `Camera.constants.CaptureTarget.cameraRoll` (default), ~~`Camera.constants.CaptureTarget.memory`~~ (deprecated),

This property allows you to specify the target output of the captured image data. By default the image binary is sent back as a base 64 encoded string.


#### `type`

Values: `Camera.constants.Type.front` or `"front"`, `Camera.constants.Type.back` or `"back"` (default)

Use the `type` property to specify which camera to use.


## Component methods

You can access component methods by adding a `ref` (ie. `ref="camera"`) prop to your `<Camera>` element, then you can use `this.refs.camera.capture(cb)`, etc. inside your component.

#### `capture([options,] callback)`

Captures photos from the camera. Set the captureTarget property to specify how to save it.


## Subviews
This component supports subviews, so if you wish to use the camera view as a background or if you want to layout buttons/images/etc. inside the camera then you can do that.


## Roadmap
1. camera view
	* add aspect property
	* add orientation property
2. capture support
	* improve quality of captured photos
	* correct captured photo rotation and add support for the rotation option
	* add disk captureTarget
	* video capturing & captureAudio option
3. zoom & flash
4. barcode reading
