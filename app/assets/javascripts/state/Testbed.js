/* global define:false */
define(['state/GameState', 'playmat/Playmat', 'utils/Keyboard'], function (GameState, Playmat, Keyboard) {
  'use strict';

  function Testbed(game) {
    GameState.call(this, 'testbed', game);
  }

  Testbed.prototype = Object.create(GameState.prototype);
  Testbed.prototype.constructor = Testbed;

  Testbed.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.keyboard = new Keyboard(this.game);
    this.game.keyboard.init();

    var scenario;
    if(window.location.hash.length > 1) {
      scenario = window.location.hash.replace('#', '');
    } else {
      scenario = 'testbed';
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

  Testbed.prototype.resize = function() {
    if(this.playmat !== undefined) {
      this.playmat.resizer.resize();
    }
  };

  Testbed.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'BrawlFound':
        this.startBrawl(v.brawl);
        break;
      default:
        GameState.prototype.onMessage.call(this, c, v);
        break;
    }
  };

  Testbed.prototype.startBrawl = function(brawl) {
    this.playmat = new Playmat(this.game);
    this.playmat.setBrawl(brawl);
  };

  return Testbed;
});
