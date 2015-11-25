/* global define:false */
define(['state/GameState', 'state/Sandbox'], function (GameState, Sandbox) {
  'use strict';

  function LoadingScreen(client) {
    GameState.call(this, 'loading', client);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.client.load.spritesheet('gems', 'assets/images/game/gems.png', 128, 128);

    var sandbox = new Sandbox(this.client);
    this.game.state.add('sandbox', sandbox);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.state.start('sandbox');
  };

  return LoadingScreen;
});
