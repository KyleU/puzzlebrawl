/* global define:false */
/* global Phaser:false */
define(['game/GameInput', 'game/GameNetwork', 'utils/Config', 'state/InitialState'], function (GameInput, GameNetwork, Config, InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  return {
    init: function(game) {
      game.connected = false;
      game.gameNetwork = new GameNetwork(this);
      game.gameInput = new GameInput(this);
    },
    configOptions: {
      width: '100%',
      height: '100%',
      renderer: Phaser.AUTO,
      parent: 'game-container',
      state: new InitialState(this),
      transparent: true,
      resolution: 2
    }
  };
});
