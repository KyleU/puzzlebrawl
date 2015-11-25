/* global define:false */
/* global Phaser:false */
define(['state/GameState'], function (GameState) {
  'use strict';

  function InitialState(client) {
    GameState.call(this, 'initial', client);
  }

  InitialState.prototype = Object.create(GameState.prototype);
  InitialState.prototype.constructor = InitialState;

  InitialState.prototype.preload = function() {
    //this.game.load.image('load-bar', this.assetRoot + 'images/load/bar.png');
  };

  InitialState.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.client.time.advancedTiming = true;

    this.client.scale.scaleMode = Phaser.ScaleManager.NO_SCALE;

    var c = this.client;
    var resizeCallback = function() {
      var w = window.innerWidth > 2000 ? 2000 : window.innerWidth;
      c.scale.setGameSize(w, window.innerHeight);
      c.state.getCurrentState().resize();
    };

    window.addEventListener('resize', resizeCallback);
    resizeCallback();

    if(typeof Phaser.Plugin.Debug === 'function') {
      this.client.add.plugin(Phaser.Plugin.Debug);
    }
  };

  return InitialState;
});
