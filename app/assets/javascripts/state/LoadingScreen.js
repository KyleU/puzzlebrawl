/* global define:false */
define(['dialog/Modal', 'state/GameState', 'state/Testbed'], function (Modal, GameState, Testbed) {
  'use strict';

  function LoadingScreen(game) {
    GameState.call(this, 'loading', game);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.game.load.spritesheet('gems', 'assets/images/game/gems.png', 256, 256);
    this.game.load.image('board-bg', 'assets/images/board/bg-a.png');

    var testbed = new Testbed(this.game);
    this.game.state.add('testbed', testbed);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    Modal.init(this.game);

    this.game.state.start('testbed');
  };

  return LoadingScreen;
});
