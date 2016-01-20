/* global define:false */
define(['state/GameState', 'state/HomeState', 'utils/Config'], function (GameState, HomeState, Config) {
  'use strict';

  function LoadingScreen(game) {
    GameState.call(this, 'loading', game);
  }

  LoadingScreen.prototype = Object.create(GameState.prototype);
  LoadingScreen.prototype.constructor = LoadingScreen;

  LoadingScreen.prototype.preload = function() {
    this.game.load.spritesheet('gems', 'assets/images/game/gems.png', Config.tile.size, Config.tile.size);
    this.game.load.audio('audio', 'assets/audio/test.ogg');

    var homeState = new HomeState(this.game);
    this.game.state.add('home', homeState);
  };

  LoadingScreen.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.state.start('home');
  };

  return LoadingScreen;
});
