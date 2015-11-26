/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  function toggleDebug() {
    var debugPanels = document.getElementsByClassName('pdebug');
    if(debugPanels.length === 1) {
      if(debugPanels[0].style.display === 'none' || debugPanels[0].style.display === '') {
        debugPanels[0].style.display = 'block';
      } else {
        debugPanels[0].style.display = 'none';
      }
    }
  }

  var Keyboard = function(game) {
    this.game = game;
  };

  Keyboard.prototype.init = function() {
    var g = this.game;

    var sandboxKey = g.input.keyboard.addKey(Phaser.Keyboard.X);
    sandboxKey.onDown.add(function() { g.sandbox(); });

    var debugKey = g.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
    debugKey.onDown.add(toggleDebug);
  };

  Keyboard.prototype.enable = function() {
    this.game.input.keyboard.enabled = true;
  };

  Keyboard.prototype.disable = function() {
    this.game.input.keyboard.enabled = false;
  };

  return Keyboard;
});
