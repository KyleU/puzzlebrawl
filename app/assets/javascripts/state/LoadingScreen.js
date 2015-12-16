/* global define:false */
define(['state/GameState', 'state/Testbed'], function (GameState, Testbed) {
  'use strict';

  function LoadingScreen(game) {
    GameState.call(this, 'loading', game);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.game.load.spritesheet('gems', 'assets/images/game/gems.png', 256, 256);
    this.game.load.image('board-bg-a', 'assets/images/board/bg-a.png');

    var testbed = new Testbed(this.game);
    this.game.state.add('testbed', testbed);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.state.start('testbed');
  };

  return LoadingScreen;
});
