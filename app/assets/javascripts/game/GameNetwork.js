/* global define:false */
define(['utils/Config', 'utils/DebugInfo', 'utils/Status', 'utils/Websocket'], function (Config, DebugInfo, Status, Websocket) {
  'use strict';

  function GameNetwork(game) {
    this.connected = false;
    this.game = game;
    this.ws = new Websocket(Config.wsUrl, this);

    var self = this;
    function sendPing() {
      if(self.connected) {
        self.send('Ping', { timestamp: new Date().getTime() });
      }
      setTimeout(sendPing, 5000);
    }
    setTimeout(sendPing, 1000);
  }

  GameNetwork.prototype.send = function(c, v) {
    this.ws.send(c, v);
  };

  GameNetwork.prototype.onConnect = function() {
    Status.set('connection', 'Connected');
    this.connected = true;
  };

  GameNetwork.prototype.onDisconnect = function() {
    Status.set('connection', 'Disconnected');
    this.connected = false;

    console.info('Connection closed. Attempting to reconnect in five seconds.');
    var self = this;
    setTimeout(function() { self.ws.connect(self); }, 5000);
  };

  GameNetwork.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'Pong':
        var delta = new Date().getTime() - v.timestamp;
        Status.set('latency', delta);
        break;
      case 'SendDebugInfo':
        var data = DebugInfo.getDebugInfo(this.game);
        this.ws.send('DebugInfo', { 'data': JSON.stringify(data) });
        break;
      default:
        this.game.onMessage(c, v);
        break;
    }
  };

  return GameNetwork;
});
