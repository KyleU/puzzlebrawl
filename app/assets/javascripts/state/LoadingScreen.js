/* global define:false */
define(['state/GameState', 'state/Sandbox', 'utils/Keyboard'], function (GameState, Sandbox, Keyboard) {
  'use strict';

  function LoadingScreen(client) {
    GameState.call(this, 'loading', client);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.client.load.spritesheet('gems', 'assets/images/game/gems.png', 128, 128);
    this.client.load.image('board-bg-a', 'assets/images/board/bg-a.png');

    var sandbox = new Sandbox(this.client);
    this.client.state.add('sandbox', sandbox);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.client.keyboard = new Keyboard(this.client);
    this.client.keyboard.init();

    this.client.state.start('sandbox');
  };

  return LoadingScreen;
});
