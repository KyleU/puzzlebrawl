/* global define:false */
define(['utils/Status'], function (Status) {
  'use strict';

  var connection;
  var connectionEl = document.getElementById('status-connection');

  var latency;
  var latencyEl = document.getElementById('status-latency');

  var score;
  var scoreEl = document.getElementById('status-score');

  return {
    set: function(key, val) {
      switch(key) {
        case 'connection':
          connection = val;
          connectionEl.textContent = connection;
          break;
        case 'latency':
          latency = val;
          latencyEl.textContent = latency + 'ms';
          break;
        case 'score':
          score = val;
          scoreEl.textContent = score;
          break;
        default:
          break;
      }
    }
  };
});
