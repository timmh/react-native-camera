var React = require('react-native');
var { requireNativeComponent, PropTypes, NativeModules } = React;

var ReactNativeCameraModule = NativeModules.ReactCameraModule;
var ReactCameraView = requireNativeComponent('ReactCameraView', {
    name: 'ReactCameraView',
    propTypes: {
        scaleX: PropTypes.number,
        scaleY: PropTypes.number,
        translateX: PropTypes.number,
        translateY: PropTypes.number,
        rotation: PropTypes.number
    }
});

var ReactCameraViewWrapper = React.createClass({

    render () {
        return (
            <ReactCameraView {...this.props}></ReactCameraView>
        );
    },

    capture (options, callback) {
        return new Promise(function(resolve, reject) {
            if (!callback && typeof options === 'function') callback = options;
            ReactNativeCameraModule.capture(function(encoded) {
                if (typeof callback === 'function') callback(encoded);
                resolve(encoded);
            });
        });
    }
});

module.exports = ReactCameraViewWrapper;
