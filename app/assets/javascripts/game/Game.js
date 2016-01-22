/* global define:false */
/* global Phaser:false */
define(['game/GameInit', 'game/GameInput', 'state/InitialState'], function (gameInit, GameInput, InitialState) {
  'use strict';

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
    this.initialized = false;
    this.gameInput = new GameInput(this);
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  Game.prototype.init = function() {
    gameInit(this);
  };

  Game.prototype.send = function(c, v) { this.localServer.receive(c, v); };
  Game.prototype.onInput = function(t, param) { this.gameInput.onInput(t, param); };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        var state = this.state.getCurrentState();
        state.onMessage(c, v);
        break;
    }
  };

  return Game;
});
