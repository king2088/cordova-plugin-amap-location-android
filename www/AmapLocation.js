var exec = require('cordova/exec');

exports.getCurrentPosition = function (success, error) {
    exec(success, error, 'AmapLocation', 'getCurrentPosition', []);
};
