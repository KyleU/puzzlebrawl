/* global define:false */
/* global Phaser:false */
define(['game/GameNetwork', 'utils/Config', 'state/InitialState'], function (GameNetwork, Config, InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game() {
    this.initialized = false;
    this.connected = false;
    this.network = new GameNetwork(this);

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

  Game.prototype.send = function(c, v) {
    this.network.send(c, v);
  };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        console.log('Message [' + c + '] received over websocket: ' + JSON.stringify(v, null, 2));
        break;
    }
  };

  return Game;
});
