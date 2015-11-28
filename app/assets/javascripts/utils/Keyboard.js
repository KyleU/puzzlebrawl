/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var Keyboard = function(game) {
    this.game = game;
  };

  Keyboard.prototype.init = function() {
    var g = this.game;
    function input(s) { return function() { g.onInput(s); }; }

    g.input.keyboard.addKey(Phaser.Keyboard.LEFT).onDown.add(input('active-left'));
    g.input.keyboard.addKey(Phaser.Keyboard.RIGHT).onDown.add(input('active-right'));

    g.input.keyboard.addKey(Phaser.Keyboard.UP).onDown.add(input('active-clockwise'));
    g.input.keyboard.addKey(Phaser.Keyboard.DOWN).onDown.add(input('active-counter-clockwise'));

    g.input.keyboard.addKey(Phaser.Keyboard.PERIOD).onDown.add(input('active-step'));
    g.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR).onDown.add(input('active-drop'));

    g.input.keyboard.addKey(Phaser.Keyboard.X).onDown.add(input('sandbox'));
    g.input.keyboard.addKey(Phaser.Keyboard.QUESTION_MARK).onDown.add(input('toggle-debug'));
  };

  return Keyboard;
});
