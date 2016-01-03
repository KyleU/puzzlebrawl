/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var Keyboard = function(game) {
    this.game = game;
  };

  Keyboard.prototype.init = function() {
    var g = this.game;
    function input(s, param) { return function() { g.onInput(s, param); }; }

    g.input.keyboard.addKey(Phaser.Keyboard.LEFT).onDown.add(input('active-left'));
    g.input.keyboard.addKey(Phaser.Keyboard.RIGHT).onDown.add(input('active-right'));

    g.input.keyboard.addKey(Phaser.Keyboard.UP).onDown.add(input('active-clockwise'));
    g.input.keyboard.addKey(Phaser.Keyboard.DOWN).onDown.add(input('active-counter-clockwise'));

    g.input.keyboard.addKey(Phaser.Keyboard.PERIOD).onDown.add(input('active-step'));
    g.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR).onDown.add(input('active-drop'));

    g.input.keyboard.addKey(Phaser.Keyboard.ONE).onDown.add(input('target-select', 0));
    g.input.keyboard.addKey(Phaser.Keyboard.TWO).onDown.add(input('target-select', 1));
    g.input.keyboard.addKey(Phaser.Keyboard.THREE).onDown.add(input('target-select', 2));
    g.input.keyboard.addKey(Phaser.Keyboard.FOUR).onDown.add(input('target-select', 3));
    g.input.keyboard.addKey(Phaser.Keyboard.FIVE).onDown.add(input('target-select', 4));
    g.input.keyboard.addKey(Phaser.Keyboard.SIX).onDown.add(input('target-select', 5));
    g.input.keyboard.addKey(Phaser.Keyboard.SEVEN).onDown.add(input('target-select', 6));
    g.input.keyboard.addKey(Phaser.Keyboard.EIGHT).onDown.add(input('target-select', 7));

    g.input.keyboard.addKey(Phaser.Keyboard.OPEN_BRACKET).onDown.add(input('target-previous'));
    g.input.keyboard.addKey(Phaser.Keyboard.CLOSED_BRACKET).onDown.add(input('target-next'));

    g.input.keyboard.addKey(Phaser.Keyboard.X).onDown.add(input('sandbox'));
    g.input.keyboard.addKey(Phaser.Keyboard.QUESTION_MARK).onDown.add(input('toggle-debug'));
    g.input.keyboard.addKey(Phaser.Keyboard.EQUALS).onDown.add(input('debug', 'sync'));
    g.input.keyboard.addKey(Phaser.Keyboard.BACKWARD_SLASH).onDown.add(input('debug', 'dump'));
    g.input.keyboard.addKey(Phaser.Keyboard.V).onDown.add(input('debug', 'cheat-victory'));
  };

  return Keyboard;
});
