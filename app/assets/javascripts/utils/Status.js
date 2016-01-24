/* global define:false */
define([], function () {
  'use strict';

  var latency;
  var latencyEl = document.getElementById('status-latency');

  var scenario;
  var scenarioEl = document.getElementById('status-scenario');

  return {
    setLatency: function(val) {
      latency = val;
      latencyEl.textContent = latency + 'ms';
    },
    setStatus: function(val) {
      scenario = val;
      scenarioEl.textContent = scenario;
    }
  };
});
