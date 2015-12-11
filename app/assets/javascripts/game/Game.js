/* global define:false */
/* global Phaser:false */
/* global PuzzleBrawl:false */
define(['game/GameInput', 'state/InitialState'], function (GameInput, InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game() {
    Phaser.Game.call(this, {
      width: '100%',
      height: '100%',
      renderer: Phaser.AUTO,
      parent: 'game-container',
      state: new InitialState(this),
      transparent: true,
      resolution: 2
    });
    this.connected = false;
    this.gameInput = new GameInput(this);
    this.localServer = this.createLocalServer();
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  Game.prototype.send = function(c, v) { this.localServer.receive(c, v); };
  Game.prototype.onInput = function(t) { this.gameInput.onInput(t); };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        var state = this.state.getCurrentState();
        state.onMessage(c, v);
        break;
    }
  };

  Game.prototype.createLocalServer = function() {
    var ret = new PuzzleBrawl();
    var self = this;
    var callback = function(json) {
      var ret = JSON.parse(json);
      self.onMessage(ret.c, ret.v);
    };
    ret.register(callback);
    return ret;
  };

  return Game;
});
