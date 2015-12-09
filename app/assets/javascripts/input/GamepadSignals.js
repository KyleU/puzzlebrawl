/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var GamepadSignals = function(game) {
    this.game = game;
    this.onAnalogLeft = new Phaser.Signal();
    this.onAnalogRight = new Phaser.Signal();
    this.onAnalogUp = new Phaser.Signal();
    this.onAnalogDown = new Phaser.Signal();

    this.onAnalogLeft.add(function() { game.onInput('active-left'); });
    this.onAnalogRight.add(function() { game.onInput('active-right'); });
    this.onAnalogUp.add(function() { game.onInput('active-step'); });
    this.onAnalogDown.add(function() { game.onInput('active-drop'); });
  };

  return GamepadSignals;
});
