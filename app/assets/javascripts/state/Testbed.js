/* global define:false */
/* global _:false */
define(['state/GameState', 'playmat/Playmat', 'utils/Gamepad', 'utils/Gesture', 'utils/Keyboard'], function (GameState, Playmat, Gamepad, Gesture, Keyboard) {
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

    this.game.gamepad = new Gamepad(this.game);
    this.game.gamepad.init();

    this.game.gesture = new Gesture(this.game);
    this.game.gesture.init();

    var scenario;
    if(window.location.hash.length > 1) {
      scenario = window.location.hash.replace('#', '');
    } else {
      scenario = 'testbed';
    }

    var self = this;
    function startWhenConnected() {
      if(self.game.gameNetwork.connected) {
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
      case 'BrawlJoined':
        this.startBrawl(v.brawl);
        break;
      case 'PlayerUpdate':
        this.onPlayerUpdate(v);
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

  Testbed.prototype.onPlayerUpdate = function(update) {
    var p = this.playmat;
    if(p === undefined || p === null) {
      throw 'Player update received with no active brawl.';
    }
    var board = p.boardsByPlayer[update.id];
    if(board === undefined || board === null) {
      throw 'Player update received with invalid id [' + update.id + '].';
    }
    _.each(update.mutations, function(m) {
      switch(m.t) {
        case 'a':
          board.addGem(m.v.gem, m.v.x, m.v.y);
          break;
        case 'g':
          board.setActiveGems(m.v.gems);
          break;
        default:
          console.log('Unhandled mutation [' + m.t + '].');
          break;
      }
    });
  };

  return Testbed;
});
