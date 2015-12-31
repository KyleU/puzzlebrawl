/* global define:false */
define(['sandbox/ParticleTest', 'state/GameState', 'state/Testbed', 'utils/Config'], function (ParticleTest, GameState, Testbed, Config) {
  'use strict';

  function LoadingScreen(game) {
    GameState.call(this, 'loading', game);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.game.load.spritesheet('gems', 'assets/images/game/gems.png', Config.tile.size, Config.tile.size);

    ParticleTest.preload(this.game);

    var testbed = new Testbed(this.game);
    this.game.state.add('testbed', testbed);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.state.start('testbed');
  };

  return LoadingScreen;
});
