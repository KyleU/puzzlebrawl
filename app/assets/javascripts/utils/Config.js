/* global define:false */
define(function () {
  'use strict';

  var wsUrl;
  if(document.location.href.indexOf('https') === 0) {
    wsUrl = 'wss://' + document.location.host + '/websocket';
  } else {
    wsUrl = 'ws://' + document.location.host + '/websocket';
  }

  return {
    offline: false,
    wsUrl: wsUrl,

    input: {
      tapMaxDistancePx: 10,
      holdTimeMs: 250,
      swipeMinTimeMs: 50,
      swipeMaxTimeMs: 250,
      swipeDistancePx: 100
    }
  };
});
