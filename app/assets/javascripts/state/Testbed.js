/* global define:false */
define(['dialog/Modal', 'state/GameState', 'utils/BrawlSync'], function (Modal, GameState, BrawlSync) {
  'use strict';

  function Testbed(game) {
    GameState.call(this, 'testbed', game);
  }

  Testbed.prototype = Object.create(GameState.prototype);
  Testbed.prototype.constructor = Testbed;

  Testbed.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.init();
  };

  Testbed.prototype.update = function() {
    this.game.gamepad.update();
    this.game.gesture.update();
  };

  Testbed.prototype.resize = function() {
    this.game.playmat.resizer.resize();
  };

  Testbed.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'BrawlJoined':
        this.startBrawl(v.self, v.brawl);
        break;
      case 'PlayerUpdate':
        this.onPlayerUpdate(v);
        break;
      case 'DebugResponse':
        if(v.key === 'sync') {
          BrawlSync.check(this.game.playmat.brawl, JSON.parse(v.data));
        } else {
          throw 'Unhandled debug response [' + v.key + '].';
        }
        break;
      case 'BrawlCompletionReport':
        Modal.show('Game Complete', JSON.stringify(v, null, 2), true);
        break;
      case 'ServerError':
        Modal.show('Server Error', v.reason + ': ' + v.content);
        break;
      default:
        GameState.prototype.onMessage.call(this, c, v);
        break;
    }
  };

  Testbed.prototype.startBrawl = function(self, brawl) {
    this.game.playmat.setBrawl(self, brawl);
  };

  Testbed.prototype.onPlayerUpdate = function(update) {
    var p = this.game.playmat;
    if(p === undefined || p === null) {
      throw 'Player update received with no active brawl.';
    }
    var board = p.players[update.id].board;
    if(board === undefined || board === null) {
      throw 'Player update received with invalid id [' + update.id + '].';
    }
    board.applyMutations(update.segments, function(delta) {
      p.changeScore(update.id, delta);
    });
  };

  return Testbed;
});
