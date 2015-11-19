/* global define:false */
define(['utils/Config', 'utils/DebugInfo', 'utils/Status', 'utils/Websocket'], function (cfg, DebugInfo, Status, Websocket) {
  'use strict';

  function PuzzleBrawl() {
    this.ws = new Websocket(cfg.wsUrl, this);
    this.activeGame = null;
  }

  PuzzleBrawl.prototype.onConnect = function() {
    var self = this;
    function sendPing() {
      self.ws.send('Ping', { timestamp: new Date().getTime() });
      setTimeout(sendPing, 5000);
    }

    Status.set('connection', 'Connected.');
    setTimeout(sendPing, 1000);
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
