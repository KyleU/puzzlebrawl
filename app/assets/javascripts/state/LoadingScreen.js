/* global define:false */
define(['state/GameState', 'state/Sandbox'], function (GameState, Sandbox) {
  'use strict';

  function LoadingScreen(game) {
    GameState.call(this, 'loading', game);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.game.load.spritesheet('gems', 'assets/images/game/gems.png', 128, 128);
    this.game.load.image('board-bg-a', 'assets/images/board/bg-a.png');

    var sandbox = new Sandbox(this.game);
    this.game.state.add('sandbox', sandbox);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.state.start('sandbox');
  };

  return LoadingScreen;
});
