/* global define:false */
/* global Phaser:false */
define(['state/InitialState'], function (InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game(ws) {
    this.initialized = false;
    this.ws = ws;

    var initialState = new InitialState(this);

    var configOptions = {
      width: '100%',
      height: '100%',
      renderer: Phaser.AUTO,
      parent: 'game-container',
      state: initialState,
      transparent: true,
      resolution: 2
    };

    Phaser.Game.call(this, configOptions);
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  return Game;
});
