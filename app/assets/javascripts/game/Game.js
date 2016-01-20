/* global define:false */
/* global Phaser:false */
/* global PuzzleBrawl:false */
define([
  'game/GameInput', 'gem/GemTextures', 'input/Gamepad', 'input/Gesture', 'input/Keyboard', 'playmat/Playmat', 'state/InitialState'
], function (GameInput, GemTextures, Gamepad, Gesture, Keyboard, Playmat, InitialState) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function Game() {
    Phaser.Game.call(this, {
      width: '100%',
      height: '100%',
      renderer: Phaser.AUTO,
      parent: 'game-container',
      state: new InitialState(this),
      transparent: true,
      resolution: 2
    });
    this.connected = false;
    this.initialized = false;
    this.gameInput = new GameInput(this);
  }

  Game.prototype = Phaser.Game.prototype;
  Game.prototype.constructor = Game;

  Game.prototype.init = function() {
    if(this.initialized) {
      throw 'Game already initialized.';
    }

    this.keyboard = new Keyboard(this);
    this.keyboard.init();

    this.gamepad = new Gamepad(this);
    this.gamepad.init();

    this.gesture = new Gesture(this);
    this.gesture.init();

    this.gemTextures = new GemTextures(this);

    this.playmat = new Playmat(this);

    this.createLocalServer();
    this.localServer.start();
  };

  Game.prototype.send = function(c, v) { this.localServer.receive(c, v); };
  Game.prototype.onInput = function(t, param) { this.gameInput.onInput(t, param); };

  Game.prototype.onMessage = function(c, v) {
    switch(c) {
      default:
        var state = this.state.getCurrentState();
        state.onMessage(c, v);
        break;
    }
  };

  Game.prototype.createLocalServer = function() {
    var pb = new PuzzleBrawl();
    var self = this;
    var callback = function(json) {
      var ret = JSON.parse(json);
      self.onMessage(ret.c, ret.v);
    };
    pb.register(callback);
    this.localServer = pb;
  };

  return Game;
});
