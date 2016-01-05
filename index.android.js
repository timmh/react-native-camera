var React = require('react-native');
var { requireNativeComponent, PropTypes, NativeModules } = React;

var ReactNativeCameraModule = NativeModules.ReactCameraModule;
var ReactCameraView = requireNativeComponent('ReactCameraView', {
    name: 'ReactCameraView',
    propTypes: {
        ...View.propTypes,
        scaleX: PropTypes.number,
        scaleY: PropTypes.number,
        translateX: PropTypes.number,
        translateY: PropTypes.number,
        rotation: PropTypes.number,
        type: PropTypes.oneOf(['back', 'front'])
    }
});

var constants = {
    'Aspect': {
        'stretch': 'stretch',
        'fit': 'fit',
        'fill': 'fill'
    },
    'BarCodeType': {
        'upca': 'upca',
        'upce': 'upce',
        'ean8': 'ean8',
        'ean13': 'ean13',
        'code39': 'code39',
        'code93': 'code93',
        'codabar': 'codabar',
        'itf': 'itf',
        'rss14': 'rss14',
        'rssexpanded': 'rssexpanded',
        'qr': 'qr',
        'datamatrix': 'datamatrix',
        'aztec': 'aztec',
        'pdf417': 'pdf417'
    },
    'Type': {
        'front': 'front',
        'back': 'back'
    },
    'CaptureMode': {
        'still': 'still',
        'video': 'video'
    },
    'CaptureTarget': {
        'memory': 'base64',
        'disk': 'disk',
        'cameraRoll': 'gallery'
    },
    'Orientation': {
        'auto': 'auto',
        'landscapeLeft': 'landscapeLeft',
        'landscapeRight': 'landscapeRight',
        'portrait': 'portrait',
        'portraitUpsideDown': 'portraitUpsideDown'
    },
    'FlashMode': {
        'off': 'off',
        'on': 'on',
        'auto': 'auto'
    },
    'TorchMode': {
        'off': 'off',
        'on': 'on',
        'auto': 'auto'
    }
};

var ReactCameraViewWrapper = React.createClass({

    getDefaultProps() {
        return ({
            type: constants.Type.back,
            captureTarget: constants.CaptureTarget.cameraRoll
        });
    },

    render () {
        return (
            <ReactCameraView {...this.props}></ReactCameraView>
        );
    },

    capture (options, callback) {
        var component = this;
        var defaultOptions = {
            type: component.props.type,
            target: component.props.captureTarget,
            sampleSize: 0,
            title: '',
            description: ''
        };
        return new Promise(function(resolve, reject) {
            if (!callback && typeof options === 'function') {
                callback = options;
                options = {};
            }
            ReactNativeCameraModule.capture(Object.assign(defaultOptions, options || {}), function(encoded) {
                if (typeof callback === 'function') callback(encoded);
                resolve(encoded);
            });
        });
    }
});

ReactCameraViewWrapper.constants = constants;

module.exports = ReactCameraViewWrapper;
