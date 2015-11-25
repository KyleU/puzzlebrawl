/* global define:false */
/* global Phaser:false */
define(['state/InitialState'], function (InitialState) {
  'use strict';

  function GameClient(ws) {
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

  GameClient.prototype = Phaser.Game.prototype;
  GameClient.prototype.constructor = GameClient;

  return GameClient;
});
