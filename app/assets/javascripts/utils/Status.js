/* global define:false */
define(['utils/Status'], function (Status) {
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
    setScenario: function(val) {
      scenario = val;
      scenarioEl.textContent = scenario;
    }
  };
});
