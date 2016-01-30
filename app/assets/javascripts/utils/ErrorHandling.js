/* global define:false */
/* global Phaser:false */
define(['dialog/Modal'], function(Modal) {
  'use strict';

  function sendError(msg) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/error');
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
      if (xhr.status === 200) {
        console.log('200 OK: ' + xhr.responseText);
      }
    };
    var json = JSON.stringify(msg, null, 2);
    xhr.send(json);
  }

  function handleError(message, url, line, col, error) {
    var msg = {
      'error': {
        'message': message,
        'url': url,
        'line': line,
        'col': col,
        'stack': error.stack
      },
      'device': Phaser.Device,
      'occurred': new Date()
    };
    sendError(msg);

    var alert = 'An error has occurred. This is almost certainly my fault. I\'ve reported the error, thanks for your patience.' +
      '\n\n[' + message + ']  at [' + url.substring(url.lastIndexOf('/') + 1) + ':' + line + '].';
    Modal.show('Script Error', alert);

    return true;
  }

  function init() {
    window.onerror = handleError;
  }

  return {
    'init': init
  };
});
