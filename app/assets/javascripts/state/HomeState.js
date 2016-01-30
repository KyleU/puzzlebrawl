/* global define:false */
define(['panels/Options', 'panels/Feedback', 'state/GameState', 'state/HomeMessageHandler'], function (Options, Feedback, GameState, handler) {
  'use strict';

  function HomeState(game) {
    GameState.call(this, 'home', game);
  }

  HomeState.prototype = Object.create(GameState.prototype);
  HomeState.prototype.constructor = HomeState;

  HomeState.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);
    this.game.init();
    this.options = new Options(this.game);
    Feedback.init(this.game);
  };

  HomeState.prototype.update = function() {
    this.game.gamepad.update();
    this.game.gesture.update();
  };

  HomeState.prototype.resize = function() {
    this.game.playmat.resizer.resize();
  };

  HomeState.prototype.initialStateReceived = function(state) {
    this.game.userId = state.userId;
    this.game.username = state.username;
    this.options.loadOptions(state.preferences);
    var h = window.location.hash;
    if(h.charAt(0) === '#') {
      h = h.substr(1);
    }
    this.game.navigation.navigate(h);
  };

  HomeState.prototype.onMessage = function(c, v) {
    handler(this, c, v);
  };

  return HomeState;
});
