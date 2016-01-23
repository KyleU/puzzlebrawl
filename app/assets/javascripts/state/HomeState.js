/* global define:false */
define(['dialog/Modal', 'state/GameState', 'state/HomeMessageHandler'], function (Modal, GameState, handler) {
  'use strict';

  function HomeState(game) {
    GameState.call(this, 'home', game);
  }

  HomeState.prototype = Object.create(GameState.prototype);
  HomeState.prototype.constructor = HomeState;

  HomeState.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.init();
    this.game.localServer.start();

    this.showPanel('splash');
  };

  HomeState.prototype.update = function() {
    this.game.gamepad.update();
    this.game.gesture.update();
  };

  HomeState.prototype.resize = function() {
    this.game.playmat.resizer.resize();
  };

  HomeState.prototype.showPanel = function(key) {
    console.log('Show: ' + key);
    this.game.splashPanel.visible = key === 'splash';
    this.game.playmat.visible = key === 'playmat';
  };

  HomeState.prototype.onMessage = function(c, v) {
    handler(this, c, v);
  };

  return HomeState;
});
