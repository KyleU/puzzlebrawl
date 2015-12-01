/* global define:false */
define(['utils/Status'], function (Status) {
  'use strict';

  var connection;
  var connectionEl = document.getElementById('status-connection');

  var latency;
  var latencyEl = document.getElementById('status-latency');

  var scenario;
  var scenarioEl = document.getElementById('status-scenario');

  return {
    setConnection: function(val) {
      connection = val;
      connectionEl.textContent = connection;
    },
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
