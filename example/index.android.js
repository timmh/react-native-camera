'use strict';

var React = require('react-native');
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Image,
  TouchableHighlight,
} = React;

var Camera = require('react-native-camera'); //require the camera component

var ReactNativeCameraExample = React.createClass({
    getInitialState: function() {
        return ({
            capturedBase64: '',
            type: Camera.constants.Type.back
        });
    },

    switchCamera: function() {
        this.setState({ type: this.state.type === Camera.constants.Type.back ? Camera.constants.Type.front : Camera.constants.Type.back });
    },

    render: function() {
        var component = this;
        return (
            <View style={styles.container}>
                <Camera style={styles.camera} ref="cam" type={this.state.type} captureTarget={Camera.constants.CaptureTarget.memory}></Camera>
                <Image
                source={{
                    isStatic: true,
                    uri: 'data:image/jpeg;base64,' + component.state.capturedBase64,
                }}
                style={styles.captured}
                />
            <TouchableHighlight style={styles.captureButton} onPress={function() {
                component.refs.cam.capture({ sampleSize: 10 }).then(function(capturedBase64) {
                    component.setState({ capturedBase64 });
                    setTimeout(() => component.setState({ capturedBase64: '' }), 5000);
                });
            }}>
                <Text style={{textAlign: 'center'}}>Capture</Text>
            </TouchableHighlight>


            <TouchableHighlight style={styles.switchButton} onPress={this.switchCamera}>
                <Text style={{textAlign: 'center'}}>Switch</Text>
            </TouchableHighlight>
            </View>
        );
    }
});

var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF'
    },
    camera: {
        position: 'absolute',
        top: 0, right: 0, bottom: 0, left: 0
    },
    captured: {
        width: 200,
        height: 400
    },
    captureButton: {
        position: 'absolute',
        height: 50,
        left: 50, bottom: 20, right: 50,
        backgroundColor: '#FFFFFF',
        borderRadius: 20,
    },
    switchButton: {
        position: 'absolute',
        height: 50,
        left: 50, top: 20, right: 50,
        backgroundColor: '#FFFFFF',
        borderRadius: 20,
    },
});

AppRegistry.registerComponent('ReactNativeCameraExample', () => ReactNativeCameraExample);
