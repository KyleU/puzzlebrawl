/* global define:false */
define(['state/GameState', 'playmat/Playmat', 'utils/Keyboard'], function (GameState, Playmat, Keyboard) {
  'use strict';

  function Sandbox(game) {
    GameState.call(this, 'sandbox', game);
  }

  Sandbox.prototype = Object.create(GameState.prototype);
  Sandbox.prototype.constructor = Sandbox;

  Sandbox.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.keyboard = new Keyboard(this.game);
    this.game.keyboard.init();

    var scenario;
    if(window.location.hash.length > 1) {
      scenario = window.location.hash.replace('#', '');
    } else {
      scenario = 'sandbox';
    }

    var self = this;
    function startWhenConnected() {
      if(self.game.network.connected) {
        self.game.send('StartBrawl', {scenario: scenario});
      } else {
        setTimeout(startWhenConnected, 20);
      }
    }
    startWhenConnected();
  };

  Sandbox.prototype.resize = function() {
    if(this.playmat !== undefined) {
      this.playmat.resizer.resize();
    }
  };

  Sandbox.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'BrawlFound':
        this.startBrawl(v.brawl);
        break;
      default:
        GameState.prototype.onMessage.call(this, c, v);
        break;
    }
  };

  Sandbox.prototype.startBrawl = function(brawl) {
    this.playmat = new Playmat(this.game);
    this.playmat.setBrawl(brawl);
  };

  return Sandbox;
});
