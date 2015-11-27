/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var Keyboard = function(game) {
    this.game = game;
  };

  Keyboard.prototype.init = function() {
    var g = this.game;

    var sandboxKey = g.input.keyboard.addKey(Phaser.Keyboard.X);
    sandboxKey.onDown.add(function() { g.onInput('sandbox'); });

    var dropKey = g.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
    dropKey.onDown.add(function() { g.onInput('drop-active'); });

    var debugKey = g.input.keyboard.addKey(Phaser.Keyboard.QUESTION_MARK);
    debugKey.onDown.add(function() { g.onInput('toggle-debug'); });
  };

  Keyboard.prototype.enable = function() {
    this.game.input.keyboard.enabled = true;
  };

  Keyboard.prototype.disable = function() {
    this.game.input.keyboard.enabled = false;
  };

  return Keyboard;
});
