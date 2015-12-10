/* global define:false */
/* global Phaser:false */
/* global PuzzleBrawl:false */
define(['game/GameInit', 'game/GameInput', 'game/GameNetwork'], function (GameInit, GameInput, GameNetwork) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game() {
    Phaser.Game.call(this, GameInit.configOptions);
    this.connected = false;
    this.gameNetwork = new GameNetwork(this);
    this.gameInput = new GameInput(this);
    this.localServer = this.createLocalServer();
    this.gameInit = GameInit;
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  Game.prototype.send = function(c, v) { this.gameNetwork.send(c, v); };
  Game.prototype.onInput = function(t) { this.gameInput.onInput(t); };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        var state = this.state.getCurrentState();
        state.onMessage(c, v);
        break;
    }
  };

  Game.prototype.createLocalServer = function() {
    var ret = new PuzzleBrawl();
    var self = this;
    var callback = function(json) {
      var ret = JSON.parse(json);
      self.onMessage(ret.c, ret.v);
    };
    ret.register(callback);
    return ret;
  };

  return Game;
});
