/* global define:false */
/* global PuzzleBrawl:false */
define([
  'gem/GemTextures', 'input/Gamepad', 'input/Gesture', 'input/Keyboard', 'playmat/Playmat', 'state/GameState'
], function (GemTextures, Gamepad, Gesture, Keyboard, Playmat, GameState) {
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

    this.game.gemTextures = new GemTextures(this.game);

    var scenario;
    if(window.location.hash.length > 1) {
      scenario = window.location.hash.replace('#', '');
    } else {
      scenario = 'normal';
    }

    var self = this;
    function startWhenConnected() {
      if(self.game.gameNetwork.connected) {
        self.game.send('StartBrawl', { scenario: scenario });
      } else {
        setTimeout(startWhenConnected, 20);
      }
    }

    if(scenario === 'offline') {
      console.log('Starting offline game...');
      var puzzleBrawl = new PuzzleBrawl();
      console.log(puzzleBrawl);
    } else {
      startWhenConnected();
    }
  };

  Testbed.prototype.update = function() {
    this.game.gesture.update();
  };

  Testbed.prototype.render = function() {
    //this.game.gesture.renderDebug();
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
      case 'DebugResponse':
        if(v.key === 'sync') {
          this.sync(JSON.parse(v.data));
        } else {
          throw 'Unhandled debug response [' + v.key + '].';
        }
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
    var board = p.players[update.id].board;
    if(board === undefined || board === null) {
      throw 'Player update received with invalid id [' + update.id + '].';
    }
    board.applyMutations(update.segments, function(delta) {
      p.changeScore(update.id, delta);
    });
  };

  Testbed.prototype.sync = function(tgt) {
    var src = this.playmat.brawl;
    var errors = [];
    function e(key, s, t) {
      if(s !== t) {
        errors.push(key + ': ' + s + ' !== ' + t);
      }
    }

    console.log(src);
    console.log(tgt);

    e('id', src.id, tgt.id);
    e('player-count', src.players.length, tgt.players.length);
    for(var playerIdx = 0; playerIdx < src.players.length; playerIdx++) {
      var srcPlayer = src.players[playerIdx];
      var tgtPlayer = tgt.players[playerIdx];
      e('player-' + playerIdx + '-id', srcPlayer.id, tgtPlayer.id);
      e('player-' + playerIdx + '-name', srcPlayer.name, tgtPlayer.name);
      e('player-' + playerIdx + '-gemstream-seed', srcPlayer.gemStream.seed, tgtPlayer.gemStream.seed);
      e('player-' + playerIdx + '-score', srcPlayer.score, tgtPlayer.score);

      var srcBoard = srcPlayer.board;
      var tgtBoard = tgtPlayer.board;

      e('player-' + playerIdx + '-board-width', srcBoard.width, tgtBoard.width);
      e('player-' + playerIdx + '-board-height', srcBoard.height, tgtBoard.height);

      for(var y = 0; y < srcBoard.height; y++) {
        for(var x = 0; x < srcBoard.width; x++) {
          var srcG = srcBoard.spaces[x][y];
          var tgtG = tgtBoard.spaces[x][y];
          if(srcG === null || tgtG === null) {
            e('player-' + playerIdx + '-board-' + x + '-' + y, srcG === null, tgtG === null);
          } else {
            e('player-' + playerIdx + '-board-' + x + '-' + y, srcG.id, tgtG.id);
          }
        }
      }

    }

    console.log('Sync completed with [' + errors.length + '] errors.');
    for(var errorIdx = 0; errorIdx < errors.length; errorIdx++) {
      console.log('  - ' + errors[errorIdx]);
    }
  };

  return Testbed;
});
