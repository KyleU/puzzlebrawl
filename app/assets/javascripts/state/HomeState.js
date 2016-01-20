/* global define:false */
define(['dialog/Modal', 'state/GameState', 'ui/Menu', 'utils/BrawlSync'], function (Modal, GameState, Menu, BrawlSync) {
  'use strict';

  function HomeState(game) {
    GameState.call(this, 'gameplay', game);
  }

  HomeState.prototype = Object.create(GameState.prototype);
  HomeState.prototype.constructor = HomeState;

  HomeState.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.init();
  };

  HomeState.prototype.update = function() {
    this.game.gamepad.update();
    this.game.gesture.update();
  };

  HomeState.prototype.resize = function() {
    this.game.playmat.resizer.resize();
  };

  HomeState.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'InitialState':
        console.log('InitialState', v);
        this.game.userId = v.user;
        this.game.menu = new Menu(v.menu);
        break;
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
      case 'PlayerLoss':
        this.game.playmat.onPlayerLoss(v.id);
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

  HomeState.prototype.startBrawl = function(self, brawl) {
    this.game.playmat.setBrawl(self, brawl);
  };

  HomeState.prototype.onPlayerUpdate = function(update) {
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

  return HomeState;
});