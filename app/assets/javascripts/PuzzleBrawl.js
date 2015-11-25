/* global define:false */
define(['utils/Config', 'utils/DebugInfo', 'utils/Status', 'utils/Websocket'], function (cfg, DebugInfo, Status, Websocket) {
  'use strict';

  function PuzzleBrawl() {
    this.started = false;
    this.activeGame = null;
    this.ws = new Websocket(cfg.wsUrl, this);
    this.start();
  }

  PuzzleBrawl.prototype.onConnect = function() {
    Status.set('connection', 'Connected');
  };

  PuzzleBrawl.prototype.onDisconnect = function() {
    Status.set('connection', 'Disconnected');

    console.info('Connection closed. Attempting to reconnect in five seconds.');
    var self = this;
    setTimeout(function() { self.ws.connect(self); }, 5000);
  };

  PuzzleBrawl.prototype.start = function() {
    var self = this;
    function sendPing() {
      if(self.ws.connected) {
        self.ws.send('Ping', { timestamp: new Date().getTime() });
      }
      setTimeout(sendPing, 5000);
    }
    setTimeout(sendPing, 1000);

    this.started = true;
  };

  PuzzleBrawl.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'Pong':
        var delta = new Date().getTime() - v.timestamp;
        Status.set('latency', delta);
        break;
      case 'SendDebugInfo':
        var data = DebugInfo.getDebugInfo(this.activeGame);
        this.ws.send('DebugInfo', { 'data': JSON.stringify(data) });
        break;
      default:
        console.log('Message [' + c + '] received over websocket: ' + JSON.stringify(v, null, 2));
        break;
    }
  };

  return PuzzleBrawl;
});
