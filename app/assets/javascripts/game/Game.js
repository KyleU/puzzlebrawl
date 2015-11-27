/* global define:false */
/* global Phaser:false */
define(['game/GameInput', 'game/GameNetwork', 'utils/Config', 'state/InitialState'], function (GameInput, GameNetwork, Config, InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game() {
    this.connected = false;
    this.gameNetwork = new GameNetwork(this);
    this.gameInput = new GameInput(this);

    var configOptions = {
      width: '100%',
      height: '100%',
      renderer: Phaser.AUTO,
      parent: 'game-container',
      state: new InitialState(this),
      transparent: true,
      resolution: 2
    };

    Phaser.Game.call(this, configOptions);
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  Game.prototype.send = function(c, v) { this.gameNetwork.send(c, v); };
  Game.prototype.onInput = function(t) { this.gameInput.onInput(t); };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        this.state.getCurrentState().onMessage(c, v);
        break;
    }
  };

  return Game;
});
