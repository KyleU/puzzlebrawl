/* global define:false */
define(['utils/Config', 'utils/DebugTrace', 'utils/Status', 'utils/Websocket'], function (Config, DebugTrace, Status, Websocket) {
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
    if(Config.offline) {
      this.game.localServer.receive(c, v);
    } else {
      this.ws.send(c, v);
    }
  };

  GameNetwork.prototype.onConnect = function() {
    Status.setConnection('Connected');
    this.connected = true;
  };

  GameNetwork.prototype.onDisconnect = function() {
    Status.setConnection('Disconnected');
    this.connected = false;

    console.info('Connection closed. Attempting to reconnect in five seconds.');
    var self = this;
    setTimeout(function() { self.ws.connect(self); }, 5000);
  };

  GameNetwork.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'SendTrace':
        var data = DebugTrace.getTrace(this.game);
        this.ws.send('DebugRequest', { 'data': JSON.stringify(data) });
        break;
      default:
        this.game.onMessage(c, v);
        break;
    }
  };

  return GameNetwork;
});
